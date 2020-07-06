package cn.nbbandxdd.survey.ques.logic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.jwt.JwtHolder;
import cn.nbbandxdd.survey.ques.dao.QuesDAO;
import cn.nbbandxdd.survey.ques.dao.entity.AnswEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByPronEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByExpEntity;

@Component
public class QuesLogic {

	@Autowired
	private QuesDAO quesDAO;
	
	@Transactional
	public QuesByPronEntity insert(QuesByPronEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		Date date = new Date();
		entity.setLastMantDat(new SimpleDateFormat("yyyyMMdd").format(date));
		entity.setLastMantTmstp(new Timestamp(date.getTime()));
		
		// PK
		entity.setQuesCd(
			entity.getLastMantDat() + CommonUtils.fillbackPubSerNo(ICommonConstDefine.PUB_SER_NO_QUES_QUES_CD));
		
		// ANSW
		genAnswList(entity.getAnswList());
		
		quesDAO.insert(entity);
		
		return entity;
	}
	
	@Transactional
	public void delete(QuesByPronEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		quesDAO.delete(entity);
	}
	
	@Transactional
	public void update(QuesByPronEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		Date date = new Date();
		entity.setLastMantDat(new SimpleDateFormat("yyyyMMdd").format(date));
		entity.setLastMantTmstp(new Timestamp(date.getTime()));
		
		// ANSW
		genAnswList(entity.getAnswList());
		
		quesDAO.update(entity);
	}
	
	public QuesByPronEntity findByPron(QuesByPronEntity entity) {
		
		entity.setLastMantUsr(JwtHolder.get());
		
		return quesDAO.findByPron(entity);
	}
	
	public QuesByRespEntity findByResp(QuesByRespEntity entity) {
		
		return quesDAO.findByResp(entity);
	}
	
	public Page<QuesByExpEntity> findByExp(QuesByExpEntity entity, Integer pageNum, Integer pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		return quesDAO.findByExp(entity);
	}
	
	public Page<QuesByRespEntity> findList(Integer pageNum, Integer pageSize) {
		
		QuesByRespEntity infoEntity = new QuesByRespEntity();
		infoEntity.setLastMantUsr(JwtHolder.get());
		
		PageHelper.startPage(pageNum, pageSize);
		return quesDAO.findList(infoEntity);
	}
	
	public Page<QuesByRespEntity> findCommonList(Integer pageNum, Integer pageSize) {
		
		QuesByRespEntity infoEntity = new QuesByRespEntity();
		infoEntity.setLastMantUsr(ICommonConstDefine.USER_EVERYONE);
		
		PageHelper.startPage(pageNum, pageSize);
		return quesDAO.findList(infoEntity);
	}

	public int findScre(String quesCd, List<String> answList) {
		
		if (CollectionUtils.isEmpty(answList)) {
			
			return 0;
		}
		
		List<AnswEntity> lisAnsw = quesDAO.findAnswWithScreList(quesCd);
		if (CollectionUtils.isEmpty(lisAnsw)) {
			
			return 0;
		}
		
		List<String> rightAnswList = lisAnsw.stream()
			.filter(a -> a.getScre() > 0)
			.map(a -> a.getAnswCd())
			.collect(Collectors.toList());
		
		return CollectionUtils.isEqualCollection(answList, rightAnswList)
			? lisAnsw.stream().mapToInt(a -> a.getScre()).max().orElseGet(() -> 0)
			: 0;
	}
	
	public List<String> findRightAnsw(String quesCd) {
		
		return quesDAO.findRightAnsw(quesCd);
	}
	
	private void genAnswList(List<AnswEntity> answList) {
		
		int cnt = 0;
		for (AnswEntity one : answList) {

			one.setAnswCd(StringUtils.leftPad(String.valueOf(cnt++), 2, '0'));
			one.setScre(one.getScre());
		}
	}
}
