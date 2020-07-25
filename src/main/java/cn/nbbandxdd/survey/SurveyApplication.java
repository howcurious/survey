package cn.nbbandxdd.survey;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>SpringBootApplication
 * 
 * @author howcurious
 */
@SpringBootApplication
@EnableScheduling
public class SurveyApplication {

	@PostConstruct
	void setDefaultTimeZone() {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	/**
	 * <p>main
	 * 
	 * @param args args
	 */
	public static void main(String[] args) {

		SpringApplication.run(SurveyApplication.class, args);
	}
}
