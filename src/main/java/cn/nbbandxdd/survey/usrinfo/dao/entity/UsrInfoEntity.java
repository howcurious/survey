package cn.nbbandxdd.survey.usrinfo.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

/**
 * <p>用户信息Entity。
 * 
 * @author howcurious
 */
@Data
public class UsrInfoEntity {

	/**
	 * <p>微信小程序 openId。
	 */
	private String openId;
	
	/**
	 * <p>部门名。
	 */
	private String dprtNam;
	
	/**
	 * <p>职能组名。
	 */
	private String grpNam;
	
	/**
	 * <p>用户名。
	 */
	private String usrNam;
	
	/**
	 * <p>注册时间戳。
	 */
	private Timestamp regTmstp;
}
