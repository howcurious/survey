package cn.nbbandxdd.survey.common.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.ExceptionEntity;
import io.jsonwebtoken.JwtException;

/**
 * <p>Jwt过滤器。校验请求中是否包含形如“authorization: Bearer myToken”的报文头，并校验“myToken”。
 * 
 * @author howcurious
 */
public class JwtFilter extends OncePerRequestFilter {
	
	/**
	 * <p>校验Jwt。
	 * 如果报文头authorization缺失或格式错误，则抛出{@code RuntimeException}；
	 * 如果Jwt校验失败，则抛出{@code JwtException}。
	 */
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		try {
			
			HttpServletRequest req = (HttpServletRequest)request;
			String authHeader = req.getHeader("authorization");
			if (null == authHeader || !authHeader.startsWith("Bearer ")) {
				
				throw new RuntimeException("Missing or invalid authorization header: " + request.getRequestURI());
			}
			
			String token = authHeader.substring(7);
			JwtHolder.set(JwtUtils.fillbackOpenidFromToken(token));
			
			chain.doFilter(request, response);
		} catch (JwtException e) {
			
			ExceptionEntity entity = new ExceptionEntity();
			entity.setErrCode(ICommonConstDefine.SYS_ERROR_JWT_NOT_VALID_COD);
			entity.setErrMsg(ICommonConstDefine.SYS_ERROR_JWT_NOT_VALID_MSG);

			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.getWriter().write(new ObjectMapper().writeValueAsString(entity));
		}
	}
}
