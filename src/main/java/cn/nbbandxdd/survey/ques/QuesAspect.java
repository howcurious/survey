package cn.nbbandxdd.survey.ques;

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
import cn.nbbandxdd.survey.ques.service.vo.AnswVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByPronVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("prod")
@Component
@Aspect
public class QuesAspect {

	@Autowired
	private MsgSecCheck msgSecCheck;
	
	@Pointcut("execution (public * cn.nbbandxdd.survey.ques.service.QuesService.insert (..)) || " +
		"execution (public * cn.nbbandxdd.survey.ques.service.QuesService.update (..))")
	public void msgSecCheckPointcut() {};
	
	@Before("msgSecCheckPointcut() && args(sv)")
	public void msgSecCheck(JoinPoint joinPoint, Object sv) {
	
		QuesByPronVO vo = (QuesByPronVO)sv;
		if (vo != null) {
		
			StringBuilder sbQues = new StringBuilder(StringUtils.join(
				vo.getQuesCd(), vo.getTypCd(), vo.getDsc()));

			List<AnswVO> lisAnsw = vo.getAnswList();
			if (lisAnsw != null) {
				
				for (AnswVO answ : lisAnsw) {
					
					sbQues.append(StringUtils.join(
						answ.getTypCd(), answ.getDsc(),
						null == answ.getScre() ? StringUtils.EMPTY : String.valueOf(answ.getScre())));
				}
			}
			
			MsgSecCheckDTO dto = msgSecCheck.get(sbQues.toString());
			if (dto.getErrcode() != null && !"0".equals(dto.getErrcode())) {
				
				log.error("题目内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
				throw new SurveyMsgSecCheckException();
			}
		}
	}
}
