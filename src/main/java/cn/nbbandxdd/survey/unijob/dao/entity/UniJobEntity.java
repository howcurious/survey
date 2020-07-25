package cn.nbbandxdd.survey.unijob.dao.entity;

import lombok.Data;

/**
 * <p>唯一任务Entity。
 * 
 * @author howcurious
 */
@Data
public class UniJobEntity {

	/**
	 * <p>任务类型。
	 */
	private String jobTyp;
	
	/**
	 * <p>上次执行日期。
	 */
	private String srcDat;

	/**
	 * <p>此次执行日期。
	 */
	private String dstDat;
}
