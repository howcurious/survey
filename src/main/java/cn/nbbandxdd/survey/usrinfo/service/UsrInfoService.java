package cn.nbbandxdd.survey.usrinfo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.UpdateGroup;
import cn.nbbandxdd.survey.usrinfo.dao.entity.UsrInfoEntity;
import cn.nbbandxdd.survey.usrinfo.logic.UsrInfoLogic;
import cn.nbbandxdd.survey.usrinfo.service.vo.UsrInfoVO;

@RestController
@RequestMapping("/usrinfo")
public class UsrInfoService {
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UsrInfoLogic usrInfoLogic;
	
	@PostMapping("/insert")
	public void insert(@RequestBody @Validated(InsertGroup.class) UsrInfoVO sv) {
		
		usrInfoLogic.insert(modelMapper.map(sv, UsrInfoEntity.class));
	}
	
	@PostMapping("/update")
	public void update(@RequestBody @Validated(UpdateGroup.class) UsrInfoVO sv) {
		
		usrInfoLogic.update(modelMapper.map(sv, UsrInfoEntity.class));
	}
	
	@PostMapping("/load")
	public UsrInfoVO load() {
		
		UsrInfoEntity se = usrInfoLogic.loadByKey();
		
		if (null == se) {
			
			return null;
		}
		return modelMapper.map(se, UsrInfoVO.class);
	}
}
