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

public class JwtFilter extends OncePerRequestFilter {
	
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
