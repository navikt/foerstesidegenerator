package no.nav.foerstesidegenerator.consumer.dokmet;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.foerstesidegenerator.config.properties.FoerstesidegeneratorProperties;
import no.nav.foerstesidegenerator.constants.NavHeadersFilter;
import no.nav.foerstesidegenerator.exception.DokmetFunctionalException;
import no.nav.foerstesidegenerator.exception.DokmetTechnicalException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.function.Consumer;

import static java.lang.String.format;
import static no.nav.foerstesidegenerator.config.cache.CacheConfig.DOKMET_CACHE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class DokmetConsumer {

	private final WebClient webClient;

	public DokmetConsumer(FoerstesidegeneratorProperties foerstesidegeneratorProperties, WebClient webClient) {
		this.webClient = webClient.mutate()
				.baseUrl(foerstesidegeneratorProperties.getEndpoints().getDokmet().getUrl())
				.filter(new NavHeadersFilter())
				.defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.build();
	}

	@Cacheable(DOKMET_CACHE)
	@Retryable(retryFor = DokmetTechnicalException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
	public Dokumentproduksjonsinfo hentDokumentproduksjonsinfo(final String dokumenttypeId) {
		log.info("hentDokumentproduksjonsinfo henter dokumentproduksjonsinfo for dokumenttypeId={}", dokumenttypeId);

		var result = webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/{dokumenttypeId}")
						.build(dokumenttypeId))
				.retrieve()
				.bodyToMono(DokumenttypeInfoTo.class)
				.mapNotNull(this::mapResponse)
				.doOnError(handleError(dokumenttypeId))
				.block();

		log.info("hentDokumentproduksjonsinfo har hentet dokumentproduksjonsinfo for dokumenttypeId={}", dokumenttypeId);

		return result;
	}

	private Dokumentproduksjonsinfo mapResponse(DokumenttypeInfoTo dokumenttypeinfo) {
		if (manglerDokumentproduksjonsinfo(dokumenttypeinfo)) {
			return null;
		}

		return new Dokumentproduksjonsinfo(
				dokumenttypeinfo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(),
				dokumenttypeinfo.getDokumentProduksjonsInfo().getMalLogikkFil()
		);
	}

	private Consumer<Throwable> handleError(String dokumenttypeId) {
		return error -> {
			if (error instanceof WebClientResponseException response && response.getStatusCode().is4xxClientError()) {
				throw new DokmetFunctionalException(format("Dokmet feilet med statuskode=%s. Fant ingen dokumenttypeInfo med dokumenttypeId=%s. Feilmelding=%s",
						response.getStatusCode(),
						dokumenttypeId,
						response.getResponseBodyAsString()),
						error);
			} else {
				throw new DokmetTechnicalException(format("Dokmet feilet teknisk for dokumenttypeId=%s med feilmelding=%s",
						dokumenttypeId,
						error.getMessage()),
						error);
			}
		};
	}

	private boolean manglerDokumentproduksjonsinfo(DokumenttypeInfoTo response) {
		return response == null || response.getDokumentProduksjonsInfo() == null;
	}

}