package cn.nbbandxdd.survey.common.jwt;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JwtConfig implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		
		FilterRegistrationBean<JwtFilter> filterRegistrationBean =
			new FilterRegistrationBean<>(new JwtFilter());
		filterRegistrationBean.addUrlPatterns("/grpinfo/*", "/usrinfo/*", "/exam/*", "/ques/*", "/resprec/*");
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return filterRegistrationBean;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new JwtInterceptor()).addPathPatterns("/**");
	}
}
