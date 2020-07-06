package cn.nbbandxdd.survey.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.common.jwt.JwtUtils;
import cn.nbbandxdd.survey.common.wechat.code2session.Code2Session;
import cn.nbbandxdd.survey.common.wechat.code2session.Code2SessionDTO;
import cn.nbbandxdd.survey.login.service.vo.LoginVO;

@RestController
public class LoginService {

	@Autowired
	private Code2Session code2Session;
	
	@PostMapping("/login")
	public LoginVO login(@RequestBody @Validated LoginVO sv) {
		
		Code2SessionDTO dto = code2Session.get(sv.getCode());
		if (dto.getErrcode() != null && !"0".equals(dto.getErrcode())) {
			
			throw new SurveyValidationException(String.format("微信登录校验失败，%s：%s。", dto.getErrcode(), dto.getErrmsg()));
		}
		
		LoginVO loginVO = new LoginVO();
		loginVO.setToken(JwtUtils.generateTokenFromOpenid(dto.getOpenid()));
		loginVO.setIsRegistered(CommonUtils.validateUsrInfo(dto.getOpenid()));
		
		return loginVO;
	}
}
