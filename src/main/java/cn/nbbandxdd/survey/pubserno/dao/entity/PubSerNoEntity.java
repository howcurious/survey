package cn.nbbandxdd.survey.pubserno.dao.entity;

import lombok.Data;

/**
 * <p>公共序列号Entity。
 * 
 * @author howcurious
 */
@Data
public class PubSerNoEntity {

	/**
	 * <p>序列号类型。
	 */
	private String serNoTyp;
	
	/**
	 * <p>开始序列号。
	 */
	private Integer bgnSerNo;

	/**
	 * <p>当前序列号。
	 */
	private Integer curSerNo;
	
	/**
	 * <p>结束序列号。
	 */
	private Integer endSerNo;
	
	/**
	 * <p>序列号增加步幅。
	 */
	private Integer stpSprd;
	
	/**
	 * <p>序列号长度。因使用{@code Integer}保存序列号，故序列号长度不应超过9。
	 */
	private Integer fmtOutLen;
}
