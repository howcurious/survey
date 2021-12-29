package cn.nbbandxdd.survey.grpinfo.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <p>分组信息Entity。
 *
 * @author howcurious
 */
@Data
@Table("GRP_INFO")
public class GrpInfoEntity {

    /**
     * <p>部门名，与分组名{@code grpNam}组成组合主键。
     */
    @Column("DPRT_NAM")
    private String dprtNam;

    /**
     * <p>分组名，与部门名{@code dprtNam}组成组合主键。
     */
    @Column("GRP_NAM")
    private String grpNam;

    /**
     * <p>序号。
     */
    @Column("SEQ_NO")
    private Integer seqNo;
}
