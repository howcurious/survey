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

    @Column("OFF_CNT_I")
    private Integer offCntI;

    @Column("LEAVE_CNT_I")
    private Integer leaveCntI;

    @Column("GRAY_CNT_I")
    private Integer grayCntI;

    @Column("ON_CNT_E")
    private Integer onCntE;

    @Column("OFF_CNT_E")
    private Integer offCntE;

    @Column("LEAVE_CNT_E")
    private Integer leaveCntE;

    @Column("GRAY_CNT_E")
    private Integer grayCntE;
}
