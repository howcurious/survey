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

/**
 * 
 * <p>用户信息Logic。
 * 
 * <ul>
 * <li>新增实名登记，对外服务接口，使用 {@link #insert(UsrInfoEntity)}。</li>
 * <li>修改实名登记，对外服务接口，使用 {@link #update(UsrInfoEntity)}。</li>
 * <li>查看实名登记，对外服务接口，使用 {@link #loadByKey()}。</li>
 * <li>查询特定用户信息，<strong>内部使用接口</strong>，使用{@link #loadByKey(UsrInfoEntity)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Component
public class UsrInfoLogic {

	@Autowired
	private UsrInfoDAO usrInfoDAO;
	
	/**
	 * <p>新增实名登记，对外服务接口。补齐{@code openId}和注册时间戳{@code regTmstp}，校验用户是否已实名注册。
	 * 
	 * @param entity 用户信息Entity
	 * @return 插入影响记录数
	 * @see UsrInfoEntity
	 */
	@Transactional
	public int insert(UsrInfoEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		entity.setRegTmstp(new Timestamp(new Date().getTime()));
		
		if (loadByKey(entity) != null) {
			
			throw new SurveyValidationException("用户已实名注册。");
		}
		
		return usrInfoDAO.insert(entity);
	}
	
	/**
	 * <p>修改实名登记，对外服务接口。补齐{@code openId}和注册时间戳{@code regTmstp}。
	 * 
	 * @param entity 用户信息Entity
	 * @return 更新匹配记录数
	 * @see UsrInfoEntity
	 */
	@Transactional
	public int update(UsrInfoEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		entity.setRegTmstp(new Timestamp(new Date().getTime()));
		
		return usrInfoDAO.update(entity);
	}
	
	/**
	 * <p>查询特定用户信息，<strong>内部使用接口</strong>。因不获取当前请求对应的openId，不能用于对外服务。
	 * 
	 * @param entity 用户信息Entity
	 * @return UsrInfoEntity 用户信息Entity
	 * @see UsrInfoEntity
	 */
	public UsrInfoEntity loadByKey(UsrInfoEntity entity) {
		
		return usrInfoDAO.loadByKey(entity);
	}

	/**
	 * <p>查看实名登记，对外服务接口。补齐{@code openId}。
	 * 
	 * @return UsrInfoEntity 用户信息Entity
	 * @see UsrInfoEntity
	 */
	public UsrInfoEntity loadByKey() {

		UsrInfoEntity infoEntity = new UsrInfoEntity();
		infoEntity.setOpenId(JwtHolder.get());
		
		return usrInfoDAO.loadByKey(infoEntity);
	}
}
