package cn.nbbandxdd.survey.unijob.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.nbbandxdd.survey.unijob.dao.UniJobDAO;
import cn.nbbandxdd.survey.unijob.dao.entity.UniJobEntity;

@Component
public class UniJobLogic {

	@Autowired
	private UniJobDAO uniJobDAO;
	
	public int update(UniJobEntity entity) {
		
		return uniJobDAO.update(entity);
	}
}
