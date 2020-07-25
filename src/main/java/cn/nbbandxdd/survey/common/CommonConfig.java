package cn.nbbandxdd.survey.common;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * <p>公共配置。
 * 
 * <ul>
 * <li>{@code ModelMapper}配置，使用{@link #modelMapper()}。</li>
 * <li>{@code RestTemplate}配置，使用{@link #textPlainRestTemplate(ClientHttpRequestFactory)}。</li>
 * <li>{@code ClientHttpRequestFactory}配置，使用{@link #simpleClientHttpRequestFactory()}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Configuration
public class CommonConfig {

	/**
	 * <p>{@code ModelMapper}配置。用于Service层中VO和Entity的转换。
	 * 
	 * @return {@code ModelMapper}
	 */
	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}
	
	/**
	 * <p>{@code RestTemplate}配置。用于调用微信小程序服务端Restful API。
	 * 
	 * @param factory {@code ClientHttpRequestFactory}
	 * @return {@code RestTemplate}
	 */
	@Bean
	public RestTemplate textPlainRestTemplate(ClientHttpRequestFactory factory) {
	
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter() {
			{
				List<MediaType> mediaTypes = new ArrayList<>();
				mediaTypes.add(MediaType.TEXT_PLAIN);
				setSupportedMediaTypes(mediaTypes);
			}
		});

		return restTemplate;
	}
	
	/**
	 * <p>{@code ClientHttpRequestFactory}配置。用于作为参数注入方法{@link #textPlainRestTemplate(ClientHttpRequestFactory)}。
	 * 
	 * @return {@code ClientHttpRequestFactory}
	 */
	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory() {

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(15000);
		factory.setReadTimeout(5000);
		
		return factory;
	}
}
