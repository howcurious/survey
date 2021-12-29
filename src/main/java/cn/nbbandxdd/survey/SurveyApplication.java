package cn.nbbandxdd.survey;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>SpringBootApplication
 * 
 * @author howcurious
 */
@SpringBootApplication
public class SurveyApplication {

    /**
     * <p>main
     * 
     * @param args args
     */
    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));

        SpringApplication.run(SurveyApplication.class, args);
    }
}
