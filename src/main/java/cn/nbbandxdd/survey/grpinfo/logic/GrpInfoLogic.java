package cn.nbbandxdd.survey.grpinfo.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.nbbandxdd.survey.grpinfo.dao.GrpInfoDAO;
import cn.nbbandxdd.survey.grpinfo.dao.entity.GrpInfoEntity;

@Component
public class GrpInfoLogic {

	@Autowired
	private GrpInfoDAO grpInfoDAO;
	
	public List<GrpInfoEntity> findGrpList(GrpInfoEntity entity) {
		
		return grpInfoDAO.findGrpList(entity);
	}
	
	public List<GrpInfoEntity> findDprtList() {
		
		return grpInfoDAO.findDprtList();
	}
	
	public GrpInfoEntity loadByKey(GrpInfoEntity entity) {
		
		return grpInfoDAO.loadByKey(entity);
	}
	
	public List<GrpInfoEntity> findByGrp(GrpInfoEntity entity) {
		
		return grpInfoDAO.findByGrp(entity);
	}
}
