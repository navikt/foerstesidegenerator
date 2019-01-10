package no.nav.foerstesidegenerator.consumer.dokkat;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokkat.api.tkat020.v4.DokumentProduksjonsInfoToV4;
import no.nav.dokkat.api.tkat020.v4.DokumentTypeInfoToV4;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentProduksjonsInfoTo;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentTypeInfoTo;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

//	@Cacheable(value = HENT_DOKKAT_INFO, key = "#dokumenttypeId+'-dokkat'")
//	@Retryable(include = FoerstesideGeneratorTechnicalException.class, exclude = {FoerstesideGeneratorFunctionalException.class}, maxAttempts = 5, backoff = @Backoff(delay = 200))
	public DokumentTypeInfoTo hentDokumenttypeInfo(final String dokumenttypeId) {
		try {
			DokumentTypeInfoToV4 response = restTemplate.getForObject(this.dokumenttypeInfoUrl + "/" + dokumenttypeId, DokumentTypeInfoToV4.class);
			return mapResponse(response);
		} catch (HttpClientErrorException e) {
			throw new FoerstesideGeneratorTechnicalException(String.format("TKAT020 feilet med statusKode=%s. Fant ingen dokumenttypeInfo med dokumenttypeId=%s. Feilmelding=%s", e
					.getStatusCode(), dokumenttypeId, e
					.getResponseBodyAsString()), e);
		} catch (HttpServerErrorException e) {
			throw new FoerstesideGeneratorTechnicalException(String.format("TKAT020 feilet teknisk med statusKode=%s, feilmelding=%s", e.getStatusCode(), e
					.getResponseBodyAsString()), e);
		} finally {
//			requestTimer.observeDuration();
		}
	}

	private DokumentTypeInfoTo mapResponse(final DokumentTypeInfoToV4 response) {
		// finn ut hva som må være med:
		final DokumentProduksjonsInfoToV4 produksjonsInfoToV4 = response.getDokumentProduksjonsInfo();

		final DokumentProduksjonsInfoTo dokumentProduksjonsInfoTo = DokumentProduksjonsInfoTo.builder()
				.eksternVedlegg(produksjonsInfoToV4.getEksternVedlegg())
				.vedlegg(produksjonsInfoToV4.getVedlegg())
				.ikkeRedigerbarMalId(produksjonsInfoToV4.getIkkeRedigerbarMalId())
				.redigerbarMalId(produksjonsInfoToV4.getRedigerbarMalId())
				.malLogikkFil(produksjonsInfoToV4.getMalLogikkFil())
				.malXsdReferanse(produksjonsInfoToV4.getMalXsdReferanse())
				.build();

		return DokumentTypeInfoTo.builder()
				.dokumentTypeId(response.getDokumenttypeId())
				.dokumentTittel(response.getDokumentTittel())
				.arkivsystem(response.getArkivSystem())
				.dokumentProduksjonsInfo(dokumentProduksjonsInfoTo)
				.dokumentKategori(response.getDokumentKategori())
				.sensitivt(response.getSensitivt())
				.build();
	}

}