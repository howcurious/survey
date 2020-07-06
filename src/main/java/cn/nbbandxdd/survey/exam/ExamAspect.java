package cn.nbbandxdd.survey.exam;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cn.nbbandxdd.survey.common.exception.SurveyMsgSecCheckException;
import cn.nbbandxdd.survey.common.wechat.msgseccheck.MsgSecCheck;
import cn.nbbandxdd.survey.common.wechat.msgseccheck.MsgSecCheckDTO;
import cn.nbbandxdd.survey.exam.service.vo.ExamVO;
import cn.nbbandxdd.survey.ques.service.vo.AnswVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("prod")
@Component
@Aspect
public class ExamAspect {

	@Autowired
	private MsgSecCheck msgSecCheck;
	
	@Pointcut("execution (public * cn.nbbandxdd.survey.exam.service.ExamService.insert (..)) || " +
		"execution (public * cn.nbbandxdd.survey.exam.service.ExamService.update (..))")
	public void msgSecCheckPointcut() {};
	
	@Before("msgSecCheckPointcut() && args(sv)")
	public void msgSecCheck(JoinPoint joinPoint, Object sv) {
	
		ExamVO vo = (ExamVO)sv;
		if (vo != null) {
		
			StringBuilder sbExam = new StringBuilder(StringUtils.join(
				vo.getExamCd(), vo.getTypCd(), vo.getTtl(), vo.getDsc(), vo.getRpetInd(),
				null == vo.getBgnTime() ? StringUtils.EMPTY : String.valueOf(vo.getBgnTime()),
				null == vo.getEndTime() ? StringUtils.EMPTY : String.valueOf(vo.getEndTime())));

			List<QuesByRespVO> lisQues = vo.getQuesList();
			if (lisQues != null) {
				
				for (QuesByRespVO ques : lisQues) {
					
					StringBuilder sbQues = new StringBuilder(StringUtils.join(
						ques.getQuesCd(), ques.getTypCd(), ques.getDsc()));
					
					List<AnswVO> lisAnsw = ques.getAnswList();
					if (lisAnsw != null) {
						
						StringBuilder sbAnsw = new StringBuilder();
						
						for (AnswVO answ : lisAnsw) {
							
							sbAnsw.append(StringUtils.join(
								answ.getTypCd(), answ.getDsc(),
								null == answ.getScre() ? StringUtils.EMPTY : String.valueOf(answ.getScre())));
						}
						
						sbQues.append(sbAnsw);
					}
					
					sbExam.append(sbQues);
				}
			}
			
			MsgSecCheckDTO dto = msgSecCheck.get(sbExam.toString());
			if (dto.getErrcode() != null && !"0".equals(dto.getErrcode())) {
				
				log.error("问卷内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
				throw new SurveyMsgSecCheckException();
			}
		}
	}
}
