package cn.nbbandxdd.survey.resprec.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.exam.service.vo.ExamVO;
import cn.nbbandxdd.survey.login.service.vo.LoginVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;
import cn.nbbandxdd.survey.resprec.service.vo.DtlRecVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecExamStatVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecGrpStatVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecUsrRankVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecVO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class RespRecServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void test() throws UnsupportedEncodingException, Exception {
		
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
		
		// /exam/findList
		String examRes = mockMvc.perform(
			post("/exam/findList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.param("pageNum", "1")
			.param("pageSize", "10")
		)
		.andReturn().getResponse().getContentAsString();
		
		Page<ExamVO> examPage = objectMapper.readValue(examRes,
			objectMapper.getTypeFactory().constructCollectionType(Page.class, ExamVO.class));
		ExamVO examVO = examPage.get(0);
		
		// /exam/findToAnsw
		ExamVO svfta = new ExamVO();
		svfta.setExamCd(examVO.getExamCd());
		
		String examDetailRes = mockMvc.perform(
			post("/exam/findToAnsw")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svfta))
		)
		.andReturn().getResponse().getContentAsString();
		
		ExamVO examDetailVO = objectMapper.readValue(examDetailRes, ExamVO.class);

		// /resprec/insertDtl
		DtlRecVO svdi = new DtlRecVO();
		svdi.setExamCd(examDetailVO.getExamCd());
		for (QuesByRespVO one : examDetailVO.getQuesList()) {
			
			List<String> answList = new ArrayList<>();
			answList.add("00");
			
			svdi.setAnswList(answList);
			svdi.setQuesCd(one.getQuesCd());
			
			mockMvc.perform(
				post("/resprec/insertDtl")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svdi))
			)
			.andExpect(status().isOk()).andDo(print());
		}

		// /resprec/insertResp
		RespRecVO svri = new RespRecVO();
		svri.setExamCd(examDetailVO.getExamCd());
		
		mockMvc.perform(
			post("/resprec/insertResp")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svri))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /resprec/findRespList
		mockMvc.perform(
			post("/resprec/findRespList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /resprec/findUsrRank
		RespRecUsrRankVO svfur = new RespRecUsrRankVO();
		svfur.setExamCd(examVO.getExamCd());
		
		mockMvc.perform(
			post("/resprec/findUsrRank")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svfur))
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /resprec/findGrpStat
		RespRecGrpStatVO svfs = new RespRecGrpStatVO();
		svfs.setExamCd(examVO.getExamCd());
		svfs.setDprtNam("testdprt");
		
		mockMvc.perform(
			post("/resprec/findGrpStat")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svfs))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /resprec/findExamStat
		RespRecExamStatVO svrres = new RespRecExamStatVO();
		svrres.setExamCd(examVO.getExamCd());
		
		mockMvc.perform(
			post("/resprec/findExamStat")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svrres))
		)
		.andExpect(status().isOk()).andDo(print());
	}
}
