package cn.nbbandxdd.survey.usrinfo.logic;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.common.jwt.JwtHolder;
import cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO;
import cn.nbbandxdd.survey.usrinfo.dao.entity.UsrInfoEntity;

@Component
public class UsrInfoLogic {

	@Autowired
	private UsrInfoDAO usrInfoDAO;
	
	@Transactional
	public int insert(UsrInfoEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		entity.setRegTmstp(new Timestamp(new Date().getTime()));
		
		if (loadByKey(entity) != null) {
			
			throw new SurveyValidationException("用户已实名注册。");
		}
		
		return usrInfoDAO.insert(entity);
	}
	
	@Transactional
	public int update(UsrInfoEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		entity.setRegTmstp(new Timestamp(new Date().getTime()));
		
		return usrInfoDAO.update(entity);
	}
	
	/**
	 * 内部接口，查询特定用户信息
	 * 因不获取当前请求的openid，不能用于对外服务
	 */
	public UsrInfoEntity loadByKey(UsrInfoEntity entity) {
		
		return usrInfoDAO.loadByKey(entity);
	}

	public UsrInfoEntity loadByKey() {

		UsrInfoEntity infoEntity = new UsrInfoEntity();
		infoEntity.setOpenId(JwtHolder.get());
		
		return usrInfoDAO.loadByKey(infoEntity);
	}
}
