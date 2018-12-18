package no.nav.foerstesidegenerator.domain;

import static no.nav.foerstesidegenerator.TestUtils.ADR_LINJE_1;
import static no.nav.foerstesidegenerator.TestUtils.AVSENDER;
import static no.nav.foerstesidegenerator.TestUtils.BREVKODE_NAV;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER;
import static no.nav.foerstesidegenerator.TestUtils.ENHET_9999;
import static no.nav.foerstesidegenerator.TestUtils.NAVN;
import static no.nav.foerstesidegenerator.TestUtils.NETS;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.SAK_REF;
import static no.nav.foerstesidegenerator.TestUtils.TEMA_FAR;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_1;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_2;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithNetsPostboks;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutBruker;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_1;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_2;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_3;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_NAVN;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BREVKODE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_TYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ENHETSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDETYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NETS_POSTBOKS;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.OVERSKRIFTSTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTSTED;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SAKSREFERANSE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SAKSYSTEM;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SPRAAKKODE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.TEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.VEDLEGG_LISTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest.Spraakkode;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class FoerstesideMapperTest {

	@InjectMocks
	private FoerstesideMapper mapper;

	@Test
	void shouldMapRequestWithAdresse() {
		PostFoerstesideRequest request = createRequestWithAdresse();

		Foersteside domain = mapper.map(request);

		assertEquals(ADR_LINJE_1, getValueForKey(domain, ADRESSELINJE_1));
		assertNull(getValueForKey(domain, ADRESSELINJE_2));
		assertNull(getValueForKey(domain, ADRESSELINJE_3));
		assertEquals(POSTNR, getValueForKey(domain, POSTNUMMER));
		assertEquals(OSLO, getValueForKey(domain, POSTSTED));
		assertNull(getValueForKey(domain, NETS_POSTBOKS));
		assertEquals(AVSENDER, getValueForKey(domain, AVSENDER_ID));
		assertEquals(NAVN, getValueForKey(domain, AVSENDER_NAVN));
		assertEquals(BRUKER, getValueForKey(domain, BRUKER_ID));
		assertEquals(Bruker.BrukerType.PERSON.value(), getValueForKey(domain, BRUKER_TYPE));
		assertNull(getValueForKey(domain, UKJENT_BRUKER_PERSONINFO));
		assertEquals(TEMA_FAR, getValueForKey(domain, TEMA));
		assertEquals(TITTEL, getValueForKey(domain, ARKIVTITTEL));
		assertEquals(BREVKODE_NAV, getValueForKey(domain, BREVKODE));
		assertEquals(TITTEL, getValueForKey(domain, OVERSKRIFTSTITTEL));
		assertEquals(Spraakkode.NB.value(), getValueForKey(domain, SPRAAKKODE));
		assertEquals(Foerstesidetype.SKJEMA.value(), getValueForKey(domain, FOERSTESIDETYPE));
		assertEquals(String.join(";", VEDLEGG_1, VEDLEGG_2), getValueForKey(domain, VEDLEGG_LISTE));
		assertEquals(ENHET_9999, getValueForKey(domain, ENHETSNUMMER));
		assertEquals(Sak.Saksystem.PSAK.value(), getValueForKey(domain, SAKSYSTEM));
		assertEquals(SAK_REF, getValueForKey(domain, SAKSREFERANSE));
	}

	@Test
	void shouldMapRequestWithNetsPostboks() {
		PostFoerstesideRequest request = createRequestWithNetsPostboks();

		Foersteside domain = mapper.map(request);
		// TODO: skal adressefelter settes til defaultverdier?
		assertNull(getValueForKey(domain, ADRESSELINJE_1));
		assertNull(getValueForKey(domain, ADRESSELINJE_2));
		assertNull(getValueForKey(domain, ADRESSELINJE_3));
		assertNull(getValueForKey(domain, POSTNUMMER));
		assertNull(getValueForKey(domain, POSTSTED));
		assertEquals(NETS, getValueForKey(domain, NETS_POSTBOKS));
	}

	@Test
	void shouldMapUkjentBrukerPersoninfoIfBrukersIsAbsent() {
		PostFoerstesideRequest request = createRequestWithoutBruker("something");

		Foersteside domain = mapper.map(request);
		assertNull(getValueForKey(domain, BRUKER_ID));
		assertNull(getValueForKey(domain, BRUKER_TYPE));
		assertNotNull(getValueForKey(domain, UKJENT_BRUKER_PERSONINFO));
	}

	private String getValueForKey(Foersteside domain, String key) {
		return domain.getFoerstesideMetadata().stream().filter(a -> a.getKey().equals(key)).findFirst().map(FoerstesideMetadata::getValue).orElse(null);
	}


}