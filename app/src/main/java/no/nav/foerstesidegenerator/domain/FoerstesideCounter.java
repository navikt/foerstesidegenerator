package no.nav.foerstesidegenerator.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.leftPad;

@Entity
@Table(name = FoerstesideCounter.TABLE_NAME)
public class FoerstesideCounter {

    public static final String TABLE_NAME = "FOERSTESIDECOUNTER";
    private static final String SEQUENCE_NAME = TABLE_NAME + "_SEQ";

    @Version
    @Column(name = "versjon", nullable = false)
    @Getter
    private short version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "foerstesidecounter_id", unique = true, nullable = false, updatable = false)
    private Long foerstesideId;

    @Column(name = "dato", unique = true, updatable = false)
    @Getter
    private String date;

    @Column(name = "antall")
    @Getter
    private Integer antall;

    public FoerstesideCounter() {
        antall = 0;
        this.date = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public void count() {
        antall++;
    }

    public String generateLoepenummer() {
        return date + leftPad(Integer.toString(antall), 5, "0");
    }
}
