package no.nav.foerstesidegenerator.itest;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.TestUtils;
import no.nav.foerstesidegenerator.service.FoerstesideService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

public class FoerstesideServiceITest extends AbstractIT {

    @Autowired
    private FoerstesideService service;

    @Test
    @DisplayName("Synkroniser alle genereringer av løpenummer")
    void createSeveralFoerstesider() throws Exception {

        PostFoerstesideRequest request = TestUtils.createRequestWithAdresse();

        ArrayList<CompletableFuture<PostFoerstesideResponse>> responses = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            responses.add(CompletableFuture.supplyAsync(() -> service.createFoersteside(request)));
        }

        ArrayList<String> loepenummers = new ArrayList<>();
        for (CompletableFuture<PostFoerstesideResponse> r : responses) {
            loepenummers.add(r.get().getLoepenummer());
        }
        assertThat(loepenummers).doesNotHaveDuplicates();
    }
}
