package no.nav.foerstesidegenerator.consumer.dokkat;

import static no.nav.foerstesidegenerator.metrics.MetricLabels.CONSUMER;
import static no.nav.foerstesidegenerator.metrics.MetricLabels.DOK_REQUEST_CONSUMER;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokkat.api.tkat020.v4.DokumentProduksjonsInfoToV4;
import no.nav.dokkat.api.tkat020.v4.DokumentTypeInfoToV4;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentProduksjonsInfoTo;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentTypeInfoTo;
import no.nav.foerstesidegenerator.exception.DokkatConsumerFunctionalException;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import no.nav.foerstesidegenerator.metrics.Metrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@Service
@Slf4j
public class DokumentTypeInfoConsumer {

	private final RestTemplate restTemplate;

	private final String dokumenttypeInfoUrl;

	@Inject
	public DokumentTypeInfoConsumer(RestTemplate restTemplate,
									@Value("${dokumenttypeInfo_v4_url}") String dokumenttypeInfoV4Url) {
		this.restTemplate = restTemplate;
		this.dokumenttypeInfoUrl = dokumenttypeInfoV4Url;
	}

	@Metrics(value = DOK_REQUEST_CONSUMER, extraTags = {CONSUMER, "tkat020"}, percentiles = {0.5, 0.95}, histogram = true)
	@Retryable(include = FoerstesideGeneratorTechnicalException.class, maxAttempts = 5, backoff = @Backoff(delay = 200))
	public DokumentTypeInfoTo hentDokumenttypeInfo(final String dokumenttypeId) {
		DokumentTypeInfoToV4 dokumentTypeInfo;
		try {
			dokumentTypeInfo = restTemplate.getForObject(this.dokumenttypeInfoUrl + "/" + dokumenttypeId, DokumentTypeInfoToV4.class);
		} catch (HttpClientErrorException e) {
			throw new DokkatConsumerFunctionalException(String.format("TKAT020 feilet med statusKode=%s. Fant ingen dokumenttypeInfo med dokumenttypeId=%s. Feilmelding=%s",
					e.getStatusCode(), dokumenttypeId, e.getResponseBodyAsString()), e);
		} catch (HttpServerErrorException e) {
			throw new FoerstesideGeneratorTechnicalException(String.format("TKAT020 feilet teknisk med statusKode=%s, feilmelding=%s",
					e.getStatusCode(), e.getResponseBodyAsString()), e);
		}
		return mapResponse(dokumentTypeInfo);
	}

	private DokumentTypeInfoTo mapResponse(final DokumentTypeInfoToV4 dokumentTypeInfo) {
		Assert.notNull(dokumentTypeInfo, "DokumentTypeInfo is null");

		final DokumentProduksjonsInfoToV4 produksjonsInfoToV4 = dokumentTypeInfo.getDokumentProduksjonsInfo();

		final DokumentProduksjonsInfoTo dokumentProduksjonsInfoTo = DokumentProduksjonsInfoTo.builder()
				.eksternVedlegg(produksjonsInfoToV4.getEksternVedlegg())
				.vedlegg(produksjonsInfoToV4.getVedlegg())
				.ikkeRedigerbarMalId(produksjonsInfoToV4.getIkkeRedigerbarMalId())
				.malLogikkFil(produksjonsInfoToV4.getMalLogikkFil())
				.malXsdReferanse(produksjonsInfoToV4.getMalXsdReferanse())
				.build();

		return DokumentTypeInfoTo.builder()
				.dokumentTypeId(dokumentTypeInfo.getDokumenttypeId())
				.dokumentTittel(dokumentTypeInfo.getDokumentTittel())
				.arkivsystem(dokumentTypeInfo.getArkivSystem())
				.dokumentProduksjonsInfo(dokumentProduksjonsInfoTo)
				.dokumentKategori(dokumentTypeInfo.getDokumentKategori())
				.sensitivt(dokumentTypeInfo.getSensitivt())
				.build();
	}

}