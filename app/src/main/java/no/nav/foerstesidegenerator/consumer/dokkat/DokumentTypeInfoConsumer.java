package no.nav.foerstesidegenerator.consumer.dokkat;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokkat.api.tkat020.v4.DokumentProduksjonsInfoToV4;
import no.nav.dokkat.api.tkat020.v4.DokumentTypeInfoToV4;
import no.nav.foerstesidegenerator.azure.AzureTokenConsumer;
import no.nav.foerstesidegenerator.azure.TokenResponse;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentProduksjonsInfoTo;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentTypeInfoTo;
import no.nav.foerstesidegenerator.exception.DokkatConsumerFunctionalException;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import no.nav.foerstesidegenerator.metrics.Metrics;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static no.nav.foerstesidegenerator.constants.FoerstesidegeneratorConstants.*;
import static no.nav.foerstesidegenerator.metrics.MetricLabels.DOK_CONSUMER;
import static no.nav.foerstesidegenerator.metrics.MetricLabels.PROCESS_CODE;
import static org.springframework.http.HttpMethod.GET;

@Service
@Slf4j
public class DokumentTypeInfoConsumer {

	private final RestTemplate restTemplate;
	private final String dokmetScope;
	private final String dokumenttypeInfoUrl;
	private final AzureTokenConsumer tokenConsumer;

	@Autowired
	public DokumentTypeInfoConsumer(RestTemplate restTemplate,
									@Value("${dokmet_scope}") String dokmetScope,
									@Value("${dokumenttypeInfo_url}") String dokumenttypeInfoUrl,
									AzureTokenConsumer tokenConsumer) {
		this.restTemplate = restTemplate;
		this.dokmetScope = dokmetScope;
		this.dokumenttypeInfoUrl = dokumenttypeInfoUrl;
		this.tokenConsumer = tokenConsumer;
	}

	@Metrics(value = DOK_CONSUMER, extraTags = {PROCESS_CODE, "tkat020"}, percentiles = {0.5, 0.95}, histogram = true)
	@Retryable(include = FoerstesideGeneratorTechnicalException.class, maxAttempts = 5, backoff = @Backoff(delay = 200))
	public DokumentTypeInfoTo hentDokumenttypeInfo(final String dokumenttypeId) {
		HttpHeaders headers = createHeaders();

		DokumentTypeInfoToV4 response;
		try {
			HttpEntity<String> request = new HttpEntity<>(headers);
			response = restTemplate.exchange(this.dokumenttypeInfoUrl + "/" + dokumenttypeId, GET, request, DokumentTypeInfoToV4.class).getBody();
		} catch (HttpClientErrorException e) {
			throw new DokkatConsumerFunctionalException(String.format("TKAT020 feilet med statusKode=%s. Fant ingen dokumenttypeInfo med dokumenttypeId=%s. Feilmelding=%s",
					e.getStatusCode(), dokumenttypeId, e.getResponseBodyAsString()), e);
		} catch (HttpServerErrorException e) {
			throw new FoerstesideGeneratorTechnicalException(String.format("TKAT020 feilet teknisk med statusKode=%s, feilmelding=%s",
					e.getStatusCode(), e.getResponseBodyAsString()), e);
		}
		return mapResponse(response);
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

	private HttpHeaders createHeaders() {
		TokenResponse clientCredentialToken = tokenConsumer.getClientCredentialToken(dokmetScope);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(clientCredentialToken.getAccess_token());
		headers.add(NAV_CONSUMER_ID, APP_NAME);
		headers.add(NAV_CALL_ID, MDC.get(CALL_ID));
		return headers;
	}
}