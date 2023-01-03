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

    @Column("AWAY_CNT_I")
    private Integer awayCntI;

    @Column("GRAY_CNT_I")
    private Integer grayCntI;

    @Column("APOS_CNT_I")
    private Integer aposCntI;

    @Column("NPOS_CNT_I")
    private Integer nposCntI;

    @Column("POS_CNT_I")
    private Integer posCntI;

    @Column("SUS_CNT_I")
    private Integer susCntI;

    @Column("OTH_CNT_I")
    private Integer othCntI;

    @Column("ON_CNT_E")
    private Integer onCntE;

    @Column("ON_POS_CNT_E")
    private Integer onPosCntE;

    @Column("OFF_CNT_E")
    private Integer offCntE;

    @Column("LEAVE_CNT_E")
    private Integer leaveCntE;

    @Column("AWAY_CNT_E")
    private Integer awayCntE;

    @Column("GRAY_CNT_E")
    private Integer grayCntE;

    @Column("APOS_CNT_E")
    private Integer aposCntE;

    @Column("NPOS_CNT_E")
    private Integer nposCntE;

    @Column("POS_CNT_E")
    private Integer posCntE;

    @Column("SUS_CNT_E")
    private Integer susCntE;

    @Column("OTH_CNT_E")
    private Integer othCntE;

    @Column("ON_CNT_V")
    private Integer onCntV;

    @Column("ON_POS_CNT_V")
    private Integer onPosCntV;

    @Column("OFF_CNT_V")
    private Integer offCntV;

    @Column("LEAVE_CNT_V")
    private Integer leaveCntV;

    @Column("AWAY_CNT_V")
    private Integer awayCntV;

    @Column("GRAY_CNT_V")
    private Integer grayCntV;

    @Column("APOS_CNT_V")
    private Integer aposCntV;

    @Column("NPOS_CNT_V")
    private Integer nposCntV;

    @Column("POS_CNT_V")
    private Integer posCntV;

    @Column("SUS_CNT_V")
    private Integer susCntV;

    @Column("OTH_CNT_V")
    private Integer othCntV;
}
