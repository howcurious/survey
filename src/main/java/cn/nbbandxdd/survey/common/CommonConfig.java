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

@Configuration
public class CommonConfig {

	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}
	
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
	
	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory() {

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(15000);
		factory.setReadTimeout(5000);
		
		return factory;
	}
}
