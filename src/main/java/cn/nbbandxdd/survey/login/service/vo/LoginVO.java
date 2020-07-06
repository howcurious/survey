package cn.nbbandxdd.survey.login.service.vo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginVO {

	@NotBlank(message = "微信登录code不能为空。")
	private String code;
	
	private String token;
	
	private Boolean isRegistered;
}
