package cn.nbbandxdd.survey.common.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * <p>Jwt拦截器。清除{@code JwtHolder}中保存的openId。
 * 
 * @author howcurious
 */
public class JwtInterceptor implements AsyncHandlerInterceptor {

	/**
	 * 清除{@code JwtHolder}中保存的openId。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		
		JwtHolder.remove();
	}
}
