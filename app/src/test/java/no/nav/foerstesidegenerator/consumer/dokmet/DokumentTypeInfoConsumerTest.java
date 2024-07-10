package no.nav.foerstesidegenerator.consumer.dokmet;

import no.nav.dokkat.api.tkat020.v4.DokumentProduksjonsInfoToV4;
import no.nav.dokkat.api.tkat020.v4.DokumentTypeInfoToV4;
import no.nav.foerstesidegenerator.azure.AzureTokenConsumer;
import no.nav.foerstesidegenerator.azure.TokenResponse;
import no.nav.foerstesidegenerator.consumer.dokmet.to.DokumentTypeInfoTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DokumentTypeInfoConsumerTest {

	private static final String DOKTYPE = "12345678910";
	private static final String ARKIVSYSTEM = "JOARK";
	private static final String DOKUMENTKATEGORI = "KATEGORI";
	private static final String DOKUMENTTITTEL = "TITTEL";
	private static final String MALFIL = "malfil";
	private static final String MAL_XSD_REF = "mal_xsd_ref";
	private static final String MAL_ID = "mal_id";

	@Mock
	private AzureTokenConsumer tokenConsumer;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DokumentTypeInfoConsumer dokumentTypeInfoConsumer;

	@BeforeEach
	void setUp() {
		reset(restTemplate);
		when(tokenConsumer.getClientCredentialToken(any()))
				.thenReturn(getTokenResponse());
	}

	@Test
	void shouldRunOK() {
		DokumentTypeInfoToV4 response = createResponse();
		ResponseEntity<DokumentTypeInfoToV4> responseEntity = new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		response.getDokumentProduksjonsInfo().setDistribusjonInfo(null);

		when(restTemplate.exchange(
				anyString(),
				any(HttpMethod.class),
				any(HttpEntity.class),
				eq(DokumentTypeInfoToV4.class))
		).thenReturn(responseEntity);

		DokumentTypeInfoTo dokumentTypeInfoTo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(DOKTYPE);
		assertEquals(DOKTYPE, dokumentTypeInfoTo.getDokumentTypeId());
		assertEquals(ARKIVSYSTEM, dokumentTypeInfoTo.getArkivsystem());
		assertEquals(DOKUMENTTITTEL, dokumentTypeInfoTo.getDokumentTittel());
		assertEquals(DOKUMENTKATEGORI, dokumentTypeInfoTo.getDokumentKategori());
		assertEquals(MAL_ID, dokumentTypeInfoTo.getDokumentProduksjonsInfo().ikkeRedigerbarMalId());
		assertEquals(MAL_XSD_REF, dokumentTypeInfoTo.getDokumentProduksjonsInfo().malXsdReferanse());
		assertEquals(MALFIL, dokumentTypeInfoTo.getDokumentProduksjonsInfo().malLogikkFil());
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

	private TokenResponse getTokenResponse() {
		return TokenResponse.builder()
				.access_token("abc")
				.build();
	}
}