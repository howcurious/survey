package cn.nbbandxdd.survey.usrinfo.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.nbbandxdd.survey.login.service.vo.LoginVO;
import cn.nbbandxdd.survey.usrinfo.service.vo.UsrInfoVO;

@SpringBootTest
@AutoConfigureMockMvc
class UsrInfoServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void test() throws JsonProcessingException, Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		// /login
		LoginVO loginSv = new LoginVO();
		loginSv.setCode("testcodeX");
		
		String loginRes = mockMvc.perform(
			post("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginSv))
		)
		.andReturn().getResponse().getContentAsString();
		
		LoginVO loginVO = objectMapper.readValue(loginRes, LoginVO.class);
		
		// /usrinfo/insert
		UsrInfoVO svi = new UsrInfoVO();
		svi.setDprtNam("testdprt");
		svi.setGrpNam("testgrp");
		svi.setUsrNam("testusr");
		
		mockMvc.perform(
			post("/usrinfo/insert")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svi))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /usrinfo/load
		mockMvc.perform(
			post("/usrinfo/load")
			.header("authorization", "Bearer " + loginVO.getToken())
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /usrinfo/update
		UsrInfoVO svu = new UsrInfoVO();
		svu.setGrpNam("testgrp2");
		svu.setUsrNam("testusr2");
		
		mockMvc.perform(
			post("/usrinfo/update")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svu))
		)
		.andExpect(status().isOk()).andDo(print());
	}
}
