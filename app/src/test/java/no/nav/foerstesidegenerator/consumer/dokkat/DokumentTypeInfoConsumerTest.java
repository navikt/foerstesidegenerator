package no.nav.foerstesidegenerator.consumer.dokkat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import no.nav.dokkat.api.tkat020.v4.DokumentProduksjonsInfoToV4;
import no.nav.dokkat.api.tkat020.v4.DokumentTypeInfoToV4;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentTypeInfoTo;
import no.nav.foerstesidegenerator.exceptions.FoerstesideGeneratorTechnicalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class DokumentTypeInfoConsumerTest {

	private static final String DOKTYPE = "***gammelt_fnr***";
	private static final String ARKIVSYSTEM = "JOARK";
	private static final String DOKUMENTKATEGORI = "KATEGORI";
	private static final String DOKUMENTTITTEL = "TITTEL";
	private static final String MALFIL = "malfil";
	private static final String MAL_XSD_REF = "mal_xsd_ref";
	private static final String MAL_ID = "mal_id";

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DokumentTypeInfoConsumer dokumentTypeInfoConsumer;

	@BeforeEach
	void setUp() {
		reset(restTemplate);
	}

	@Test
	void shouldRunOK() {
		DokumentTypeInfoToV4 response = createResponse();
		response.getDokumentProduksjonsInfo().setDistribusjonInfo(null);

		when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

		DokumentTypeInfoTo dokumentTypeInfoTo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(DOKTYPE);
		assertEquals(DOKTYPE, dokumentTypeInfoTo.getDokumentTypeId());
		assertEquals(ARKIVSYSTEM, dokumentTypeInfoTo.getArkivsystem());
		assertEquals(DOKUMENTTITTEL, dokumentTypeInfoTo.getDokumentTittel());
		assertEquals(DOKUMENTKATEGORI, dokumentTypeInfoTo.getDokumentKategori());
		assertEquals(MAL_ID, dokumentTypeInfoTo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId());
		assertEquals(MAL_XSD_REF, dokumentTypeInfoTo.getDokumentProduksjonsInfo().getMalXsdReferanse());
		assertEquals(MALFIL, dokumentTypeInfoTo.getDokumentProduksjonsInfo().getMalLogikkFil());

	}

	@Test
	void shouldThrowTechnicalExceptionWhenBadRequest() {
		when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

		assertThrows(FoerstesideGeneratorTechnicalException.class, () -> dokumentTypeInfoConsumer.hentDokumenttypeInfo(DOKTYPE));
	}

	@Test
	void shouldThrowTechnicalExceptionWhenServerException() {
		when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

		assertThrows(FoerstesideGeneratorTechnicalException.class, () -> dokumentTypeInfoConsumer.hentDokumenttypeInfo(DOKTYPE));
	}

	@Test
	void shouldThrowTechnicalExceptionWhenUnauthorized() {
		when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

		assertThrows(FoerstesideGeneratorTechnicalException.class, () -> dokumentTypeInfoConsumer.hentDokumenttypeInfo(DOKTYPE));
	}

	private DokumentTypeInfoToV4 createResponse() {
		DokumentTypeInfoToV4 response = new DokumentTypeInfoToV4();
		response.setDokumenttypeId(DOKTYPE);
		response.setArkivSystem(ARKIVSYSTEM);
		response.setDokumentTittel(DOKUMENTTITTEL);
		response.setDokumentKategori(DOKUMENTKATEGORI);
		DokumentProduksjonsInfoToV4 dokumentProduksjonsInfoToV4 = new DokumentProduksjonsInfoToV4();
		dokumentProduksjonsInfoToV4.setMalLogikkFil(MALFIL);
		dokumentProduksjonsInfoToV4.setMalXsdReferanse(MAL_XSD_REF);
		dokumentProduksjonsInfoToV4.setIkkeRedigerbarMalId(MAL_ID);
		response.setDokumentProduksjonsInfo(dokumentProduksjonsInfoToV4);
		return response;
	}
}