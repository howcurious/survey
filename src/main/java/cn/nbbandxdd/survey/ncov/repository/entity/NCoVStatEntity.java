package cn.nbbandxdd.survey.ncov.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class NCoVStatEntity {

    @Column("DPRT_NAM")
    private String dprtNam;

    @Column("GRP_NAM")
    private String grpNam;

    @Column("ON_CNT_I")
    private Integer onCntI;

    @Column("ON_POS_CNT_I")
    private Integer onPosCntI;

    @Column("OFF_CNT_I")
    private Integer offCntI;

    @Column("LEAVE_CNT_I")
    private Integer leaveCntI;

    @Column("POS_CNT_I")
    private Integer posCntI;

    @Column("SUS_CNT_I")
    private Integer susCntI;

    @Column("ON_CNT_E")
    private Integer onCntE;

    @Column("ON_POS_CNT_E")
    private Integer onPosCntE;

    @Column("OFF_CNT_E")
    private Integer offCntE;

    @Column("LEAVE_CNT_E")
    private Integer leaveCntE;

    @Column("POS_CNT_E")
    private Integer posCntE;

    @Column("SUS_CNT_E")
    private Integer susCntE;

    @Column("ON_CNT_V")
    private Integer onCntV;

    @Column("ON_POS_CNT_V")
    private Integer onPosCntV;

    @Column("OFF_CNT_V")
    private Integer offCntV;

    @Column("LEAVE_CNT_V")
    private Integer leaveCntV;

    @Column("POS_CNT_V")
    private Integer posCntV;

    @Column("SUS_CNT_V")
    private Integer susCntV;
}
