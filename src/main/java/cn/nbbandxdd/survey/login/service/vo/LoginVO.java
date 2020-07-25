package cn.nbbandxdd.survey.login.service.vo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * <p>登录VO。
 * 
 * @author howcurious
 */
@Data
public class LoginVO {

	/**
	 * <p>临时登录凭证。
	 */
	@NotBlank(message = "微信登录code不能为空。")
	private String code;
	
	/**
	 * <p>token。
	 */
	private String token;
	
	/**
	 * <p>已注册标识。0：否，1：是。
	 */
	private Boolean isRegistered;
}
