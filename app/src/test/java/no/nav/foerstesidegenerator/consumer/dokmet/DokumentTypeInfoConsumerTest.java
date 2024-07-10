package no.nav.foerstesidegenerator.consumer.dokmet;

import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.foerstesidegenerator.azure.AzureTokenConsumer;
import no.nav.foerstesidegenerator.azure.TokenResponse;
import no.nav.foerstesidegenerator.consumer.dokmet.to.DokumentTypeInfo;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

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
		DokumenttypeInfoTo response = createResponse();
		ResponseEntity<DokumenttypeInfoTo> responseEntity = new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		response.getDokumentProduksjonsInfo().setDistribusjonInfo(null);

		when(restTemplate.exchange(
				anyString(),
				any(HttpMethod.class),
				any(HttpEntity.class),
				eq(DokumenttypeInfoTo.class))
		).thenReturn(responseEntity);

		DokumentTypeInfo dokumentTypeInfo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(DOKTYPE);
		assertEquals(DOKTYPE, dokumentTypeInfo.getDokumentTypeId());
		assertEquals(ARKIVSYSTEM, dokumentTypeInfo.getArkivsystem());
		assertEquals(DOKUMENTTITTEL, dokumentTypeInfo.getDokumentTittel());
		assertEquals(DOKUMENTKATEGORI, dokumentTypeInfo.getDokumentKategori());
		assertEquals(MAL_ID, dokumentTypeInfo.getDokumentProduksjonsInfo().ikkeRedigerbarMalId());
		assertEquals(MAL_XSD_REF, dokumentTypeInfo.getDokumentProduksjonsInfo().malXsdReferanse());
		assertEquals(MALFIL, dokumentTypeInfo.getDokumentProduksjonsInfo().malLogikkFil());
	}

	private DokumenttypeInfoTo createResponse() {
		DokumenttypeInfoTo response = new DokumenttypeInfoTo();
		response.setDokumenttypeId(DOKTYPE);
		response.setArkivSystem(ARKIVSYSTEM);
		response.setDokumentTittel(DOKUMENTTITTEL);
		response.setDokumentKategori(DOKUMENTKATEGORI);

		DokumentProduksjonsInfoTo dokumentproduksjonsinfo = new DokumentProduksjonsInfoTo();
		dokumentproduksjonsinfo.setMalLogikkFil(MALFIL);
		dokumentproduksjonsinfo.setMalXsdReferanse(MAL_XSD_REF);
		dokumentproduksjonsinfo.setIkkeRedigerbarMalId(MAL_ID);
		response.setDokumentProduksjonsInfo(dokumentproduksjonsinfo);

		return response;
	}

	private TokenResponse getTokenResponse() {
		return TokenResponse.builder()
				.access_token("abc")
				.build();
	}
}