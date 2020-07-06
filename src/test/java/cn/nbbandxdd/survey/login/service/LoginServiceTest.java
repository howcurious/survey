package cn.nbbandxdd.survey.login.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.nbbandxdd.survey.login.service.vo.LoginVO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class LoginServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void test() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		LoginVO loginSv = new LoginVO();
		loginSv.setCode("testcode");
		
		mockMvc.perform(
			post("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginSv))
		)
		.andExpect(status().isOk())
		.andDo(print());
	}
}
