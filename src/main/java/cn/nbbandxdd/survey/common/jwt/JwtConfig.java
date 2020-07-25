package cn.nbbandxdd.survey.common.jwt;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>Jwt相关配置。
 * 
 * <ul>
 * <li>Jwt过滤器，使用{@link #jwtFilter()}。</li>
 * <li>Jwt拦截器，使用{@link #addInterceptors(InterceptorRegistry)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Configuration
public class JwtConfig implements WebMvcConfigurer {

	/**
	 * <p>Jwt过滤器。对于/login/*以外的请求，校验Jwt。
	 * 同时，保存其中的openId至{@code JwtHolder}。
	 * 
	 * @return {@code FilterRegistrationBean<JwtFilter>}
	 * @see JwtFilter
	 */
	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		
		FilterRegistrationBean<JwtFilter> filterRegistrationBean =
			new FilterRegistrationBean<>(new JwtFilter());
		filterRegistrationBean.addUrlPatterns("/grpinfo/*", "/usrinfo/*", "/exam/*", "/ques/*", "/resprec/*");
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return filterRegistrationBean;
	}
	
	/**
	 * <p>Jwt拦截器。
	 * 对于所有请求，清除{@code JwtHolder}中openId。
	 * 
	 * @param registry {@code InterceptorRegistry}
	 * @see JwtInterceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new JwtInterceptor()).addPathPatterns("/**");
	}
}
