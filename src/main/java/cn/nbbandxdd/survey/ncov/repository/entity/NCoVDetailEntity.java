package cn.nbbandxdd.survey.ncov.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class NCoVDetailEntity {

    @Column("DPRT_NAM")
    private String dprtNam;

    @Column("GRP_NAM")
    private String grpNam;

    @Column("USR_NAM")
    private String usrNam;

    @Column("HAM_CD")
    private String hamCd;

    @Column("Q01")
    private String q01;
}
