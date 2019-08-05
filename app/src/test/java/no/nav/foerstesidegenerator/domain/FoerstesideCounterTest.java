package no.nav.foerstesidegenerator.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class FoerstesideCounterTest {

    @Test
    public void shouldGenerateOnlyDate() throws Exception {
        LocalDate now = LocalDate.now();
        LocalDate localDate = Instant.now().truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(now, localDate);

    }

}