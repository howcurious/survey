package cn.nbbandxdd.survey.ques.service;

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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.exam.service.vo.ExamVO;
import cn.nbbandxdd.survey.login.service.vo.LoginVO;
import cn.nbbandxdd.survey.ques.service.vo.AnswVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByExpVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByPronVO;

@SpringBootTest
@AutoConfigureMockMvc
class QuesServiceTest {

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
		
		// /ques/insert
		List<AnswVO> answListi = new ArrayList<>();
		for (int i = 0; i < 1; ++i) {
			
			AnswVO tmp = new AnswVO();
			tmp.setDsc("testanswX" + i);
			tmp.setScre(i);
			
			answListi.add(tmp);
		}

		QuesByPronVO svi = new QuesByPronVO();
		svi.setDsc("testquesX");
		svi.setComm("testcommX");
		svi.setTypCd("1");
		svi.setAnswList(answListi);
		
		String insertRes = mockMvc.perform(
			post("/ques/insert")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svi))
		)
		.andExpect(status().isOk()).andDo(print())
		.andReturn().getResponse().getContentAsString();
		
		QuesByPronVO quesVO = objectMapper.readValue(insertRes, QuesByPronVO.class);
		
		// /ques/update
		List<AnswVO> answListu = new ArrayList<>();
		for (int i = 0; i < 2; ++i) {
			
			AnswVO tmp = new AnswVO();
			tmp.setDsc("testanswXX" + i);
			tmp.setScre(i);
			
			answListu.add(tmp);
		}

		QuesByPronVO svu = new QuesByPronVO();
		svu.setQuesCd(quesVO.getQuesCd());
		svu.setDsc("testquesXX");
		svu.setComm("testcommXX");
		svu.setTypCd("2");
		svu.setAnswList(answListu);
		
		mockMvc.perform(
			post("/ques/update")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svu))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /exam/findList
		String examPageRes = mockMvc.perform(
			post("/exam/findList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
		)
		.andReturn().getResponse().getContentAsString();
		
		Page<ExamVO> examPage = objectMapper.readValue(examPageRes,
			objectMapper.getTypeFactory().constructCollectionType(Page.class, ExamVO.class));
		ExamVO examVO = examPage.get(0);
		
		// /exam/findQuesList
		ExamVO svfql = new ExamVO();
		svfql.setExamCd(examVO.getExamCd());
		
		String quesListRes = mockMvc.perform(
			post("/exam/findQuesList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svfql))
		)
		.andReturn().getResponse().getContentAsString();
		
		Page<QuesByRespVO> quesPage = objectMapper.readValue(quesListRes,
			objectMapper.getTypeFactory().constructCollectionType(Page.class, QuesByRespVO.class));
		
		// /ques/findByPron
		QuesByPronVO svbp = new QuesByPronVO();
		svbp.setQuesCd(quesPage.get(0).getQuesCd());
		
		mockMvc.perform(
			post("/ques/findByPron")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svbp))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /ques/findByResp
		QuesByRespVO svbr = new QuesByRespVO();
		svbr.setQuesCd(quesPage.get(0).getQuesCd());
		
		mockMvc.perform(
			post("/ques/findByResp")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svbr))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /ques/findByExp
		QuesByExpVO svbe = new QuesByExpVO();
		svbe.setExamCd(examVO.getExamCd());

		mockMvc.perform(
			post("/ques/findByExp")
			.header("authorization", "Bearer " + loginVO.getToken())
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svbe))
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /ques/findList
		mockMvc.perform(
			post("/ques/findList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /ques/findCommonList
		mockMvc.perform(
			post("/ques/findCommonList")
			.header("authorization", "Bearer " + loginVO.getToken())
			.queryParam("pageNum", "1")
			.queryParam("pageSize", "10")
		)
		.andExpect(status().isOk()).andDo(print());
		
		// /ques/delete
		QuesByPronVO svd = new QuesByPronVO();
		svd.setQuesCd(quesVO.getQuesCd());
		
		mockMvc.perform(
			post("/ques/delete")
			.header("authorization", "Bearer " + loginVO.getToken())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(svd))
		)
		.andExpect(status().isOk()).andDo(print());
	}
}
