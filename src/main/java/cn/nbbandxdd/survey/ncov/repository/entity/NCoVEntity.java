package cn.nbbandxdd.survey.ncov.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("NCoV")
public class NCoVEntity implements Persistable<String> {

    @Id
    @Column("OPEN_ID")
    private String openId;

    @Column("Q01")
    private String q01;

    @Column("Q02")
    private String q02;

    @Column("Q03")
    private String q03;

    @Column("Q04")
    private String q04;

    @Column("Q05")
    private String q05;

    @Column("Q06")
    private String q06;

    @Column("Q07")
    private String q07;

    @Column("Q08")
    private String q08;

    @Column("Q09")
    private String q09;

    @Column("Q10")
    private String q10;

    @Column("Q11")
    private String q11;

    @Column("Q12")
    private String q12;

    @Column("Q13")
    private String q13;

    @Column("Q14")
    private String q14;

    @Column("Q15")
    private String q15;

    @Column("Q16")
    private String q16;

    @Column("LAST_MANT_TMSTP")
    private LocalDateTime lastMantTmstp;

    @Transient
    private boolean isNew;

    @Override
    public String getId() {

        return openId;
    }

    @Override
    public boolean isNew() {

        return isNew;
    }
}
