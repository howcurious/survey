package cn.nbbandxdd.survey.grpinfo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import cn.nbbandxdd.survey.grpinfo.dao.entity.GrpInfoEntity;
import cn.nbbandxdd.survey.grpinfo.logic.GrpInfoLogic;
import cn.nbbandxdd.survey.grpinfo.service.vo.GrpInfoVO;

@RestController
@RequestMapping("/grpinfo")
public class GrpInfoService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private GrpInfoLogic grpInfoLogic;
	
	@PostMapping("/findGrpList")
	public List<GrpInfoVO> findGrpList(@RequestBody @Validated(SelectGroup.class) GrpInfoVO sv) {
		
		List<GrpInfoEntity> sel = grpInfoLogic.findGrpList(modelMapper.map(sv, GrpInfoEntity.class));
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<List<GrpInfoVO>>() {}.getType());
	}
	
	@PostMapping("/findDprtList")
	public List<GrpInfoVO> findDprtList() {
		
		List<GrpInfoEntity> sel = grpInfoLogic.findDprtList();
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<List<GrpInfoVO>>() {}.getType());
	}
}
