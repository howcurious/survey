package cn.nbbandxdd.survey.grpinfo.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.nbbandxdd.survey.grpinfo.service.vo.GrpInfoVO;
import cn.nbbandxdd.survey.login.service.vo.LoginVO;

@SpringBootTest
@AutoConfigureMockMvc
class GrpInfoServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void test() throws UnsupportedEncodingException, JsonProcessingException, Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		// /login
		LoginVO loginSv = new LoginVO();
		loginSv.setCode("testcode");
		
		String loginRes = mockMvc.perform(
			post("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginSv))
		)
		.andReturn().getResponse().getContentAsString();
		
		LoginVO loginVO = objectMapper.readValue(loginRes, LoginVO.class);

		// /grpinfo/findDprtList
		mockMvc.perform(
			post("/grpinfo/findDprtList")
			.header("authorization", "Bearer " + loginVO.getToken())
		)
		.andExpect(status().isOk()).andDo(print());

		// /grpinfo/findGrpList
		GrpInfoVO fglsv = new GrpInfoVO();
		fglsv.setDprtNam("testdprt");
		
		mockMvc.perform(
			post("/grpinfo/findGrpList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(fglsv))
		)
		.andExpect(status().isOk()).andDo(print());
	}
}
