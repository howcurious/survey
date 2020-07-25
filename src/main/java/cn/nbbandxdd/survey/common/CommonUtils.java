package cn.nbbandxdd.survey.common;

import java.sql.Timestamp;
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

/**
 * <p>公共工具类。跨模块调用时，需通过此类调用其他模块Logic层方法。
 * 
 * <ul>
 * <li>获取公共工具类单例，使用{@link #instance()}。</li>
 * <li>获取公共序列号，使用{@link #fillbackPubSerNo(String)}。</li>
 * <li>获取问卷起始时间，使用{@link #fillbackBgnTime(String)}。</li>
 * <li>获取问卷截止时间，使用{@link #fillbackEndTime(String)}。</li>
 * <li>获取问卷类型，使用{@link #fillbackTypCd(String)}。</li>
 * <li>获取问卷重复作答标识，使用{@link #fillbackRpetInd(String)}。</li>
 * <li>获取问卷立即展示答案标识，使用{@link #fillbackAnswImmInd(String)}。</li>
 * <li>获取特定期间内，特定用户的问卷数量，使用{@link #fillbackExamCntByIntv(String, java.sql.Timestamp, java.sql.Timestamp)}。</li>
 * <li>获取特定题目，特定答案列表的分数，使用{@link #fillbackQuesScre(String, List)}。</li>
 * <li>获取特定题目的正确答案列表，使用{@link #fillbackRightAnsw(String)}。</li>
 * <li>校验当前用户是否作答特定问卷，使用{@link #validateRespRec(String)}。</li>
 * <li>校验当前用户是否作答特定问卷的特定题目，使用{@link #validateDtlRec(String, String)}。</li>
 * <li>依据职能组名猜测部门名，使用{@link #guessDprtNam(String)}。</li>
 * <li>校验部门名和职能组名是否匹配，使用{@link #validateGrpInfo(String, String)}。</li>
 * <li>校验用户是否实名登记，使用{@link #validateUsrInfo(String)}。</li>
 * <li>获取当前用户的部门名，使用{@link #fillbackDprtNam()}。</li>
 * <li>校验是否能够获取唯一任务，使用{@link #validateUniJob(String, String, String)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Slf4j
@Component
public class CommonUtils implements InitializingBean {

	private static CommonUtils instance;
	
	/**
	 * <p>获取公共工具类单例。
	 * 
	 * @return {@code CommonUtils}
	 */
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

	/**
	 * afterPropertiesSet。
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		instance = this;
	}

	/**
	 * <p>获取公共序列号。序列号可以循环使用。
	 * 
	 * @param typ 序列号类型。
	 * 问卷序列号：{@link #cn.nbbandxdd.survey.common.ICommonConstDefine.PUB_SER_NO_EXAM_EXAM_CD}，
	 * 题目序列号：{@link #cn.nbbandxdd.survey.common.ICommonConstDefine.PUB_SER_NO_QUES_QUES_CD}。
	 * @return String 序列号
	 * @throws SurveyValidationException 当序列号不存在时抛出异常
	 * @see cn.nbbandxdd.survey.common.ICommonConstDefine
	 */
	public static String fillbackPubSerNo(String typ) {
		
		try {
			
			return instance().pubSerNoLogic.getPubSerNo(typ);
		} catch (Exception e) {
			
			log.error("生成发号器“{}”失败，错误信息：“{}”。", typ, e.getMessage());
			throw new SurveyValidationException(ICommonConstDefine.SYS_ERROR_DEFAULT_MSG);
		}
	}
	
	/**
	 * <p>获取问卷起始时间。
	 * 
	 * @param examCd 问卷编号
	 * @return java.sql.Timestamp 起始时间
	 * @throws SurveyValidationException 当问卷不存在时抛出异常
	 */
	public static Timestamp fillbackBgnTime(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getBgnTime();
	}
	
	/**
	 * <p>获取问卷截止时间。
	 * 
	 * @param examCd 问卷编号
	 * @return java.sql.Timestamp 截止时间
	 * @throws SurveyValidationException 当问卷不存在时抛出异常
	 */
	public static Timestamp fillbackEndTime(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getEndTime();
	}
	
	/**
	 * <p>获取问卷类型。
	 * 
	 * @param examCd 问卷编号
	 * @return String 问卷类型。
	 * 默认类型（同“手动选题”）：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.EXAM_TYP_CD_DEFAULT}，
	 * 手动选题：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.EXAM_TYP_CD_DEFINITE}，
	 * 自动选题：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.EXAM_TYP_CD_RANDOM}。
	 * @throws SurveyValidationException 当问卷不存在时抛出异常
	 */
	public static String fillbackTypCd(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getTypCd();
	}
	
	/**
	 * <p>获取问卷重复作答标识。
	 * 
	 * @param examCd 问卷编号
	 * @return String 问卷重复作答标识。
	 * 是：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.COMMON_IND_YES}，
	 * 否：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.COMMON_IND_NO}。
	 * @throws SurveyValidationException 当问卷不存在时抛出异常
	 * @see cn.nbbandxdd.survey.common.ICommonConstDefine
	 */
	public static String fillbackRpetInd(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getRpetInd();
	}

	/**
	 * <p>获取问卷立即展示答案标识。
	 * 
	 * @param examCd 问卷编号
	 * @return String 问卷立即展示答案标识。
	 * 是：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.COMMON_IND_YES}，
	 * 否：{@link cn.nbbandxdd.survey.common.ICommonConstDefine.COMMON_IND_NO}。
	 * @throws SurveyValidationException 当问卷不存在时抛出异常
	 */
	public static String fillbackAnswImmInd(String examCd) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(examCd);

		ExamEntity entity = instance().examLogic.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new SurveyValidationException("问卷已删除。");
		}
		return entity.getAnswImmInd();
	}

	/**
	 * <p>获取特定期间内，特定用户的问卷数量。
	 * 
	 * @param lastMantUsr 最后维护用户
	 * @param bgnTimeBgn 起始时间开始
	 * @param bgnTimeEnd 起始时间结束
	 * @return Integer 问卷数量
	 */
	public static Integer fillbackExamCntByIntv(String lastMantUsr, Timestamp bgnTimeBgn, Timestamp bgnTimeEnd) {
		
		ExamIntvEntity infoEntity = new ExamIntvEntity();
		infoEntity.setLastMantUsr(lastMantUsr);
		infoEntity.setBgnTimeBgn(bgnTimeBgn);
		infoEntity.setBgnTimeEnd(bgnTimeEnd);
		
		return instance().examLogic.findExamCntByIntv(infoEntity);
	}

	/**
	 * <p>获取特定题目，特定答案列表的分数。
	 * 
	 * @param quesCd 题目编号
	 * @param answList 答案列表
	 * @return 分数
	 */
	public static Integer fillbackQuesScre(String quesCd, List<String> answList) {
		
		return instance().quesLogic.findScre(quesCd, answList);
	}
	
	/**
	 * <p>获取特定题目的正确答案列表。
	 * 
	 * @param quesCd 题目编号
	 * @return List<String> 正确答案列表
	 */
	public static List<String> fillbackRightAnsw(String quesCd) {
		
		return instance().quesLogic.findRightAnsw(quesCd);
	}
	
	/**
	 * <p>校验当前用户是否作答特定问卷。
	 * 
	 * @param examCd 问卷编号
	 * @return Boolean 是否作答
	 */
	public static Boolean validateRespRec(String examCd) {
		
		RespRecEntity infoEntity = new RespRecEntity();
		infoEntity.setExamCd(examCd);
		
		return instance().respRecLogic.loadRespByKey(infoEntity) != null;
	}
	
	/**
	 * <p>校验当前用户是否作答特定问卷的特定题目。
	 * 
	 * @param examCd 问卷编号
	 * @param quesCd 题目编号
	 * @return Boolean 是否作答
	 */
	public static Boolean validateDtlRec(String examCd, String quesCd) {
		
		DtlRecEntity infoEntity = new DtlRecEntity();
		infoEntity.setExamCd(examCd);
		infoEntity.setQuesCd(quesCd);
		
		return instance().respRecLogic.loadDtlByKey(infoEntity) != null;
	}
	
	/**
	 * <p>依据职能组名猜测部门名。
	 * 
	 * @param grpNam 职能组名
	 * @return String 部门名
	 */
	public static String guessDprtNam(String grpNam) {
		
		GrpInfoEntity infoEntity = new GrpInfoEntity();
		infoEntity.setGrpNam(grpNam);

		List<GrpInfoEntity> sel = instance().grpInfoLogic.findByGrp(infoEntity);
		if (null == sel || sel.isEmpty()) {
			
			return StringUtils.EMPTY;
		}
		
		return  sel.get(0).getDprtNam();
	}
	
	/**
	 * <p>校验部门名和职能组名是否匹配。
	 * 
	 * @param dprtNam 部门名
	 * @param grpNam 职能组名
	 * @return Boolean 是否匹配
	 */
	public static Boolean validateGrpInfo(String dprtNam, String grpNam) {
		
		GrpInfoEntity infoEntity = new GrpInfoEntity();
		infoEntity.setDprtNam(dprtNam);
		infoEntity.setGrpNam(grpNam);

		return instance().grpInfoLogic.loadByKey(infoEntity) != null;
	}

	/**
	 * <p>校验用户是否实名登记。
	 * 
	 * @param openId openId
	 * @return Boolean 是否实名登记
	 */
	public static Boolean validateUsrInfo(String openId) {
		
		UsrInfoEntity infoEntity = new UsrInfoEntity();
		infoEntity.setOpenId(openId);

		return instance().usrInfoLogic.loadByKey(infoEntity) != null;
	}
	
	/**
	 * <p>获取当前用户的部门名。
	 * 
	 * @return String 部门名
	 * @throws SurveyValidationException 当实名登记不存在时抛出异常
	 */
	public static String fillbackDprtNam() {
		
		UsrInfoEntity entity = instance().usrInfoLogic.loadByKey();
		if (null == entity) {
			
			throw new SurveyValidationException("用户未实名。");
		}
		return entity.getDprtNam();
	}
	
	/**
	 * <p>校验是否能够获取唯一任务。
	 * 
	 * @param jobTyp 任务类型
	 * @param srcDat 上次执行日期
	 * @param dstDat 此次执行日期
	 * @return Boolean 是否能够获取
	 */
	public static Boolean validateUniJob(String jobTyp, String srcDat, String dstDat) {
		
		UniJobEntity infoEntity = new UniJobEntity();
		infoEntity.setJobTyp(jobTyp);
		infoEntity.setSrcDat(srcDat);
		infoEntity.setDstDat(dstDat);

		return 1 == instance().uniJobLogic.update(infoEntity);
	}
}
