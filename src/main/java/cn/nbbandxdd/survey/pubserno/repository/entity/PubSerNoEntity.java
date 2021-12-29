package cn.nbbandxdd.survey.pubserno.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <p>公共序列号Entity。
 *
 * @author howcurious
 */
@Data
@Table("PUB_SER_NO")
public class PubSerNoEntity {

    /**
     * <p>序列号类型。
     */
    @Id
    @Column("SER_NO_TYP")
    private String serNoTyp;

    /**
     * <p>开始序列号。
     */
    @Column("BGN_SER_NO")
    private Integer bgnSerNo;

    /**
     * <p>当前序列号。
     */
    @Column("CUR_SER_NO")
    private Integer curSerNo;

    /**
     * <p>结束序列号。
     */
    @Column("END_SER_NO")
    private Integer endSerNo;

    /**
     * <p>序列号增加步幅。
     */
    @Column("STP_SPRD")
    private Integer stpSprd;

    /**
     * <p>序列号长度。因使用{@code Integer}保存序列号，故序列号长度不应超过9。
     */
    @Column("FMT_OUT_LEN")
    private Integer fmtOutLen;
}
