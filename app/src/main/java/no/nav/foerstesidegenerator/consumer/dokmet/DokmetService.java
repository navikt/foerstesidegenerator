package no.nav.foerstesidegenerator.consumer.dokmet;

import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.foerstesidegenerator.exception.DokmetTechnicalException;
import no.nav.foerstesidegenerator.exception.ManglerDokumentproduksjonsinfoException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import static java.lang.String.format;
import static no.nav.foerstesidegenerator.config.cache.CacheConfig.DOKMET_CACHE;

@Component
public class DokmetService {

	private static final String FOERSTESIDE_DOKUMENTTYPE_ID = "000124";
	private final DokmetClient dokmetClient;

	public DokmetService(DokmetClient dokmetClient) {
		this.dokmetClient = dokmetClient;
	}

	@Cacheable(DOKMET_CACHE)
	public Dokumentproduksjonsinfo hentDokumentproduksjonsinfo() {
		try {
			DokumenttypeInfoTo dokumenttypeInfo = dokmetClient.getDokumenttypeInfo(FOERSTESIDE_DOKUMENTTYPE_ID);
			return mapResponse(dokumenttypeInfo);
		} catch (HttpServerErrorException e) {
			throw new DokmetTechnicalException("Dokmet feilet teknisk for dokumenttypeId=" + FOERSTESIDE_DOKUMENTTYPE_ID, e);
		}
	}

	private Dokumentproduksjonsinfo mapResponse(DokumenttypeInfoTo dokumenttypeinfo) {
		if (manglerDokumentproduksjonsinfo(dokumenttypeinfo)) {
			throw new ManglerDokumentproduksjonsinfoException(format("Dokumentproduksjonsinfo mangler for dokument med dokumenttypeId=%s.", FOERSTESIDE_DOKUMENTTYPE_ID));
		}

		return new Dokumentproduksjonsinfo(
				dokumenttypeinfo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(),
				dokumenttypeinfo.getDokumentProduksjonsInfo().getMalLogikkFil()
		);
	}

	private boolean manglerDokumentproduksjonsinfo(DokumenttypeInfoTo response) {
		return response == null || response.getDokumentProduksjonsInfo() == null;
	}
}
