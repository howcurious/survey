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

/**
 * <p>登录Service。
 * 
 * <ul>
 * <li>登录，使用{@link #login(LoginVO)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@RestController
public class LoginService {

	@Autowired
	private Code2Session code2Session;
	
	/**
	 * <p>登录。
	 * 
	 * <p>输入字段：
	 * <ul>
	 * <li>{@code code}：临时登录凭证（必输），微信小程序端通过接口wx.login获得。校验失败情形：空白字段。</li>
	 * </ul>
	 * 
	 * <p>输出字段：
	 * <ul>
	 * <li>{@code token}：除登录接口外，其它接口在被调用前均校验token，微信小程序端需在请求报文头中添加“authorization: Bearer myToken”。</li>
	 * <li>{@code isRegistered}：已注册标识。0：否，1：是。</li>
	 * </ul>
	 * 
	 * @param sv 登录VO
	 * @return LoginVO 登录VO
	 * @see LoginVO
	 */
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
