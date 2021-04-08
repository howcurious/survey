package cn.nbbandxdd.survey.exam.logic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.jwt.JwtHolder;
import cn.nbbandxdd.survey.exam.dao.ExamDAO;
import cn.nbbandxdd.survey.exam.dao.entity.ExamEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamIntvEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamQuesRlnEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamStatusEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;

@Component
public class ExamLogic {

	@Autowired
	private ExamDAO examDAO;
	
	@Transactional
	public ExamEntity insert(ExamEntity entity) {

		if (null == entity.getTypCd()) {
			
			entity.setTypCd(ICommonConstDefine.EXAM_TYP_CD_DEFAULT);
		}

		if (null == entity.getCntdwnInd()) {
			
			entity.setCntdwnInd(ICommonConstDefine.COMMON_IND_YES);
		}

		if (null == entity.getAnswImmInd()) {
			
			entity.setAnswImmInd(ICommonConstDefine.COMMON_IND_YES);
		}

		entity.setLastMantUsr(JwtHolder.get());
		
		Date now = new Date();
		entity.setLastMantDat(new SimpleDateFormat("yyyyMMdd").format(now));
		entity.setLastMantTmstp(new Timestamp(now.getTime()));
		
		if (null == entity.getBgnTime()) {
			
			entity.setBgnTime(new Timestamp(now.getTime()));
		}
		
		if (null == entity.getEndTime()) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 0);
			
			entity.setEndTime(new Timestamp(cal.getTimeInMillis()));
		}
		
		// PK
		entity.setExamCd(
			entity.getLastMantDat() + CommonUtils.fillbackPubSerNo(ICommonConstDefine.PUB_SER_NO_EXAM_EXAM_CD));
		
		// SEQ_NO
		entity.setSeqNo(ICommonConstDefine.EXAM_TYP_CD_RANDOM.equals(entity.getTypCd()) ? -1 : 0);

		examDAO.insert(entity);

		return entity;
	}
	
	@Transactional
	public int delete(ExamEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		return examDAO.delete(entity);
	}
	
	@Transactional
	public int update(ExamEntity entity) {
		
		if (null == entity.getCntdwnInd()) {
			
			entity.setCntdwnInd(ICommonConstDefine.COMMON_IND_YES);
		}

		if (null == entity.getAnswImmInd()) {
			
			entity.setAnswImmInd(ICommonConstDefine.COMMON_IND_YES);
		}
		
		entity.setLastMantUsr(JwtHolder.get());
		
		return examDAO.update(entity);
	}
	
	public ExamStatusEntity findStatus(ExamStatusEntity entity) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setExamCd(entity.getExamCd());
		ExamEntity examEntity = loadByKey(infoEntity);
		
		Date now = new Date();
		if (null == examEntity) {
			
			entity.setStatus(ICommonConstDefine.EXAM_STATUS_DELETED);
		} else if (now.before(examEntity.getBgnTime())) {
			
			entity.setStatus(ICommonConstDefine.EXAM_STATUS_NOTSTART);
		} else if (now.after(examEntity.getEndTime())) {
			
			entity.setStatus(ICommonConstDefine.EXAM_STATUS_FINISHED);
		} else if (CommonUtils.validateRespRec(entity.getExamCd())) {
			
			if (ICommonConstDefine.COMMON_IND_YES.equals(examEntity.getRpetInd())) {
				
				entity.setStatus(ICommonConstDefine.EXAM_STATUS_COMPLETED_AND_REPEATABLE);
			} else {
				
				entity.setStatus(ICommonConstDefine.EXAM_STATUS_COMPLETED_AND_UNREPEATABLE);
			}
		} else {
			
			entity.setStatus(ICommonConstDefine.EXAM_STATUS_TO_BE_COMPLETED);
		}
		
		return entity;
	}
	
	public ExamEntity loadByKey(ExamEntity entity) {
		
		return examDAO.loadByKey(entity);
	}

	public ExamEntity findDetail(ExamEntity entity) {

		return examDAO.findDetail(entity);
	}
	
	public Page<QuesByRespEntity> findQuesList(ExamEntity entity, Integer pageNum, Integer pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		return examDAO.findQuesList(entity);
	}

	public ExamEntity findToAnsw(ExamEntity entity) {

		ExamEntity se = examDAO.findToAnsw(entity);
		if (ICommonConstDefine.COMMON_IND_NO.equals(CommonUtils.fillbackRpetInd(entity.getExamCd())) ||
			CollectionUtils.isNotEmpty(se.getQuesList())) {
			
			return se;
		}
		
		se.setQuesList(examDAO.findQuesList(entity));
		return se;
	}
	
	public Page<ExamEntity> findList(Integer pageNum, Integer pageSize) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setLastMantUsr(JwtHolder.get());
		
		PageHelper.startPage(pageNum, pageSize);
		return examDAO.findList(infoEntity);
	}
	
	public Page<ExamEntity> findCommonList(Integer pageNum, Integer pageSize) {
		
		ExamEntity infoEntity = new ExamEntity();
		infoEntity.setLastMantUsr(ICommonConstDefine.USER_EVERYONE);

		PageHelper.startPage(pageNum, pageSize);
		return examDAO.findList(infoEntity);
	}
	
	@Transactional
	public int insertQues(ExamQuesRlnEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		Date now = new Date();
		entity.setLastMantDat(new SimpleDateFormat("yyyyMMdd").format(now));
		entity.setLastMantTmstp(new Timestamp(now.getTime()));
		
		ExamEntity examEntity = new ExamEntity();
		examEntity.setExamCd(entity.getExamCd());
		examEntity.setLastMantUsr(JwtHolder.get());
		entity.setSeqNo(examDAO.loadSeqByKey(examEntity));
		
		return examDAO.insertQues(entity);
	}
	
	@Transactional
	public int deleteQues(ExamQuesRlnEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		return examDAO.deleteQues(entity);
	}
	
	public Page<QuesByRespEntity> findAvaQues(ExamQuesRlnEntity entity, int pageNum, int pageSize) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		PageHelper.startPage(pageNum, pageSize);
		return examDAO.findAvaQues(entity);
	}
	
	/**
	 * 内部接口，查询特定时间范围内特定用户建立问卷的数量
	 * 因不获取当前请求的openid，不能用于对外服务
	 */
	public int findExamCntByIntv(ExamIntvEntity entity) {
		
		return examDAO.findExamCntByIntv(entity);
	}
}
