package cn.nbbandxdd.survey.exam.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.exam.service.vo.ExamQuesRlnVO;
import cn.nbbandxdd.survey.exam.service.vo.ExamQuesTypRlnVO;
import cn.nbbandxdd.survey.exam.service.vo.ExamStatusVO;
import cn.nbbandxdd.survey.exam.service.vo.ExamVO;
import cn.nbbandxdd.survey.login.service.vo.LoginVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ExamServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void test() throws Exception {
		
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
		
		// typCd = 1
		{
			// /exam/insert
			ExamVO svi = new ExamVO();
			svi.setTtl("testexamX");
			svi.setDsc("testdscX");
			svi.setRpetInd("0");
			
			String insertRes = mockMvc.perform(
				post("/exam/insert")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svi))
			)
			.andExpect(status().isOk()).andDo(print())
			.andReturn().getResponse().getContentAsString();
			
			ExamVO examVO = objectMapper.readValue(insertRes, ExamVO.class);
			
			// /exam/update
			ExamVO svu = new ExamVO();
			svu.setExamCd(examVO.getExamCd());
			svu.setTtl("testexamXX");
			svu.setDsc("testdscXX");
			svu.setRpetInd("0");

			mockMvc.perform(
				post("/exam/update")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svu))
			)
			.andExpect(status().isOk()).andDo(print());

			// /exam/findAvaList
			ExamQuesRlnVO sveqr = new ExamQuesRlnVO();
			sveqr.setExamCd(examVO.getExamCd());
			
			String quesRes = mockMvc.perform(
				post("/exam/findAvaQues")
				.header("authorization", "Bearer " + loginVO.getToken())
				.queryParam("pageNum", "1")
				.queryParam("pageSize", "10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sveqr))
			)
			.andExpect(status().isOk()).andDo(print())
			.andReturn().getResponse().getContentAsString();

			Page<QuesByRespVO> quesPage = objectMapper.readValue(quesRes,
				objectMapper.getTypeFactory().constructCollectionType(Page.class, QuesByRespVO.class));
			List<String> quesCds = quesPage.stream().map(a -> a.getQuesCd()).collect(Collectors.toList());
			
			// /exam/insertQues
			ExamQuesRlnVO svii = new ExamQuesRlnVO();
			svii.setExamCd(examVO.getExamCd());
			svii.setQuesCds(quesCds);
			
			mockMvc.perform(
				post("/exam/insertQues")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svii))
			)
			.andExpect(status().isOk()).andDo(print());

			// /exam/findQuesList
			ExamVO svfql = new ExamVO();
			svfql.setExamCd(examVO.getExamCd());
			
			mockMvc.perform(
				post("/exam/findQuesList")
				.header("authorization", "Bearer " + loginVO.getToken())
				.queryParam("pageNum", "1")
				.queryParam("pageSize", "10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svfql))
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/findStatus
			ExamStatusVO sves = new ExamStatusVO();
			sves.setExamCd(examVO.getExamCd());
			
			mockMvc.perform(
				post("/exam/findStatus")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sves))
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/findDetail
			ExamVO svfd = new ExamVO();
			svfd.setExamCd(examVO.getExamCd());
			
			mockMvc.perform(
				post("/exam/findDetail")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svfd))
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/findToAnsw
			ExamVO svfta1 = new ExamVO();
			svfta1.setExamCd(examVO.getExamCd());

			mockMvc.perform(
				post("/exam/findToAnsw")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svfta1))
			)
			.andExpect(status().isOk()).andDo(print());

			// /exam/findToAnsw
			ExamVO svfta2 = new ExamVO();
			svfta2.setExamCd("19700101000000000");

			mockMvc.perform(
				post("/exam/findToAnsw")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svfta2))
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/findList
			mockMvc.perform(
				post("/exam/findList")
				.header("authorization", "Bearer " + loginVO.getToken())
				.queryParam("pageNum", "1")
				.queryParam("pageSize", "10")
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/findCommonList
			mockMvc.perform(
				post("/exam/findCommonList")
				.header("authorization", "Bearer " + loginVO.getToken())
				.queryParam("pageNum", "1")
				.queryParam("pageSize", "10")
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/deleteQues
			ExamQuesRlnVO svdd = new ExamQuesRlnVO();
			svdd.setExamCd(examVO.getExamCd());
			svdd.setQuesCds(quesCds);
			
			mockMvc.perform(
				post("/exam/deleteQues")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svdd))
			)
			.andExpect(status().isOk()).andDo(print());
			
			// /exam/delete
			ExamVO svd = new ExamVO();
			svd.setExamCd(examVO.getExamCd());
			
			mockMvc.perform(
				post("/exam/delete")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svd))
			)
			.andExpect(status().isOk()).andDo(print());
		}
		
		// typCd = 2
		{
			
			// /exam/insert
			ExamVO svi = new ExamVO();
			svi.setTtl("testexamX");
			svi.setDsc("testdscX");
			svi.setRpetInd("0");
			
			// random
			ExamQuesTypRlnVO lisSi1 = new ExamQuesTypRlnVO();
			lisSi1.setQuesTypCd("1");
			lisSi1.setScre(2);
			lisSi1.setCnt(4);
			ExamQuesTypRlnVO lisSi2 = new ExamQuesTypRlnVO();
			lisSi2.setQuesTypCd("2");
			lisSi2.setScre(4);
			lisSi2.setCnt(2);
			List<ExamQuesTypRlnVO> lisSi = new ArrayList<>();
			lisSi.add(lisSi1);
			lisSi.add(lisSi2);
			svi.setQuesTypRlnList(lisSi);
			svi.setTypCd("2");
			
			String insertRes = mockMvc.perform(
				post("/exam/insert")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svi))
			)
			.andExpect(status().isOk()).andDo(print())
			.andReturn().getResponse().getContentAsString();
			
			ExamVO examVO = objectMapper.readValue(insertRes, ExamVO.class);
			
			// /exam/update
			ExamVO svu = new ExamVO();
			svu.setExamCd(examVO.getExamCd());
			svu.setTtl("testexamXX");
			svu.setDsc("testdscXX");
			svu.setRpetInd("0");
	
			mockMvc.perform(
				post("/exam/update")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svu))
			)
			.andExpect(status().isOk()).andDo(print());
	
			// /exam/delete
			ExamVO svd = new ExamVO();
			svd.setExamCd(examVO.getExamCd());
			
			mockMvc.perform(
				post("/exam/delete")
				.header("authorization", "Bearer " + loginVO.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(svd))
			)
			.andExpect(status().isOk()).andDo(print());
		}
	}
}
