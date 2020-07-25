package cn.nbbandxdd.survey.unijob.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.nbbandxdd.survey.unijob.dao.UniJobDAO;
import cn.nbbandxdd.survey.unijob.dao.entity.UniJobEntity;

/**
 * <p>唯一任务Logic。保证有且仅有一个实例执行特定定时任务。
 * 
 * <ul>
 * <li>更新唯一任务最近执行日期，<strong>内部使用接口</strong>，使用{@link #update(UniJobEntity)}</li>
 * </ul>
 * 
 * @author howcurious
 */
@Component
public class UniJobLogic {

	@Autowired
	private UniJobDAO uniJobDAO;
	
	/**
	 * <p>更新唯一任务的最近执行日期，<strong>内部使用接口</strong>。如果返回值为1，则实例执行特定定时任务；否则不执行。
	 * 
	 * @param entity 唯一任务Entity
	 * @return 更新匹配记录数
	 * @see UniJobEntity
	 */
	public int update(UniJobEntity entity) {
		
		return uniJobDAO.update(entity);
	}
}
