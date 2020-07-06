package cn.nbbandxdd.survey.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.nbbandxdd.survey.pubserno.logic.PubSerNoLogic;
import cn.nbbandxdd.survey.ques.logic.QuesLogic;
import cn.nbbandxdd.survey.resprec.dao.entity.DtlRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecEntity;
import cn.nbbandxdd.survey.resprec.logic.RespRecLogic;
import cn.nbbandxdd.survey.unijob.dao.entity.UniJobEntity;
import cn.nbbandxdd.survey.unijob.logic.UniJobLogic;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.exam.dao.entity.ExamEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamIntvEntity;
import cn.nbbandxdd.survey.exam.logic.ExamLogic;
import cn.nbbandxdd.survey.grpinfo.dao.entity.GrpInfoEntity;
import cn.nbbandxdd.survey.grpinfo.logic.GrpInfoLogic;
import cn.nbbandxdd.survey.usrinfo.dao.entity.UsrInfoEntity;
import cn.nbbandxdd.survey.usrinfo.logic.UsrInfoLogic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonUtils implements InitializingBean {

	private static CommonUtils instance;
	
	public static CommonUtils instance() {
		
		return instance;
	}
	
	@Autowired
	private PubSerNoLogic pubSerNoLogic;
	
	@Autowired
	private ExamLogic examLogic;
	
	@Autowired
	private QuesLogic quesLogic;
	
	@Autowired
	private RespRecLogic respRecLogic;
	
	@Autowired
	private GrpInfoLogic grpInfoLogic;
	
	@Autowired
	private UsrInfoLogic usrInfoLogic;
	
	@Autowired
	private UniJobLogic uniJobLogic;

	@Override
	public void afterPropertiesSet() throws Exception {
		
		instance = this;
	}

	public static String fillbackPubSerNo(String typ) {
		
		try {
			
			return instance().pubSerNoLogic.getPubSerNo(typ);
		} catch (Exception e) {
			
			log.error("生成发号器“{}”失败，错误信息：“{}”。", typ, e.getMessage());
			throw new SurveyValidationException(ICommonConstDefine.SYS_ERROR_DEFAULT_MSG);
		}
	}
	
	public static Timestamp fillbackBgnTime(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getBgnTime();
	}
	
	public static Timestamp fillbackEndTime(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getEndTime();
	}
	
	public static String fillbackTypCd(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getTypCd();
	}
	
	public static String fillbackRpetInd(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getRpetInd();
	}

	public static String fillbackAnswImmInd(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getAnswImmInd();
	}

	public static int fillbackExamCntByIntv(String lastMantUsr, Timestamp bgnTimeBgn, Timestamp bgnTimeEnd) {
		
		try {
			
			ExamIntvEntity infoEntity = new ExamIntvEntity();
			infoEntity.setLastMantUsr(lastMantUsr);
			infoEntity.setBgnTimeBgn(bgnTimeBgn);
			infoEntity.setBgnTimeEnd(bgnTimeEnd);
			
			return instance().examLogic.findExamCntByIntv(infoEntity);
		} catch (Exception e) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			log.error("获取{}至{}间问卷数量失败，错误信息：“{}”。",
				sdf.format(bgnTimeBgn), sdf.format(bgnTimeEnd), e.getMessage());
			
			throw new SurveyValidationException("获取问卷信息出错。");
		}
	}
	
	public static Integer fillbackQuesScre(String quesCd, List<String> answList) {
		
		try {
		
			return instance().quesLogic.findScre(quesCd, answList);
		} catch (Exception e) {
			
			log.error("计算题目“{}”得分失败，错误信息：“{}”。", quesCd, e.getMessage());
			throw new SurveyValidationException("获取题目信息出错。");
		}
	}
	
	public static List<String> fillbackRightAnsw(String quesCd) {
		
		try {
		
			return instance().quesLogic.findRightAnsw(quesCd);
		} catch (Exception e) {
			
			log.error("获取问题“{}”正确答案失败，错误信息：“{}”。", quesCd, e.getMessage());
			throw new SurveyValidationException("获取题目信息出错。");
		}
	}
	
	public static Boolean validateRespRec(String examCd) {
		
		try {
			
			RespRecEntity infoEntity = new RespRecEntity();
			infoEntity.setExamCd(examCd);
			
			return instance().respRecLogic.loadRespByKey(infoEntity) != null;
		} catch (Exception e) {
			
			log.error("校验答题记录“{}”失败，错误信息：“{}”。", examCd, e.getMessage());
			throw new SurveyValidationException("获取答题记录出错。");
		}
	}
	
	public static Boolean validateDtlRec(String examCd, String quesCd) {
		
		try {
			
			DtlRecEntity infoEntity = new DtlRecEntity();
			infoEntity.setExamCd(examCd);
			infoEntity.setQuesCd(quesCd);
			
			return instance().respRecLogic.loadDtlByKey(infoEntity) != null;
		} catch (Exception e) {
			
			log.error("校验答题明细“{}-{}”失败，错误信息：“{}”。", examCd, quesCd, e.getMessage());
			throw new SurveyValidationException("获取答题明细出错。");
		}
	}
	
	public static String guessDprtNam(String grpNam) {
		
		try {
			
			GrpInfoEntity infoEntity = new GrpInfoEntity();
			infoEntity.setGrpNam(grpNam);

			List<GrpInfoEntity> sel = instance().grpInfoLogic.findByGrp(infoEntity);
			if (null == sel || sel.isEmpty()) {
				
				return StringUtils.EMPTY;
			}
			
			return  sel.get(0).getDprtNam();
		} catch (Exception e) {
			
			log.error("推测分组“{}”部门名称失败，错误信息：“{}”。", grpNam, e.getMessage());
			throw new SurveyValidationException("获取分组信息出错。");
		}
	}
	
	public static Boolean validateGrpInfo(String dprtNam, String grpNam) {
		
		try {
			
			GrpInfoEntity infoEntity = new GrpInfoEntity();
			infoEntity.setDprtNam(dprtNam);
			infoEntity.setGrpNam(grpNam);

			return instance().grpInfoLogic.loadByKey(infoEntity) != null;
		} catch (Exception e) {
			
			log.error("校验分组“{}-{}”失败，错误信息：“{}”。", dprtNam, grpNam, e.getMessage());
			throw new SurveyValidationException("获取分组信息出错。");
		}
	}

	public static Boolean validateUsrInfo(String openId) {
		
		try {
			
			UsrInfoEntity infoEntity = new UsrInfoEntity();
			infoEntity.setOpenId(openId);

			return instance().usrInfoLogic.loadByKey(infoEntity) != null;
		} catch (Exception e) {
			
			log.error("校验用户“{}”失败，错误信息：“{}”。", openId, e.getMessage());
			throw new SurveyValidationException("获取注册信息出错。");
		}
	}
	
	public static String fillbackDprtNam() {
		
		UsrInfoEntity entity = instance().usrInfoLogic.loadByKey();
		if (null == entity) {
			
			throw new SurveyValidationException("用户未实名。");
		}
		return entity.getDprtNam();
	}
	
	public static boolean validateUniJob(String jobTyp, String srcDat, String dstDat) {
		
		UniJobEntity infoEntity = new UniJobEntity();
		infoEntity.setJobTyp(jobTyp);
		infoEntity.setSrcDat(srcDat);
		infoEntity.setDstDat(dstDat);

		return 1 == instance().uniJobLogic.update(infoEntity);
	}
}
