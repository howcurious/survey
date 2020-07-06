package cn.nbbandxdd.survey.ques.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.common.validation.group.DeleteGroup;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import cn.nbbandxdd.survey.common.validation.group.UpdateGroup;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByExpEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByPronEntity;
import cn.nbbandxdd.survey.ques.logic.QuesLogic;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByExpVO;
import cn.nbbandxdd.survey.ques.service.vo.QuesByPronVO;

@RestController
@RequestMapping("/ques")
public class QuesService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private QuesLogic quesLogic;
	
	@PostMapping("/insert")
	public QuesByPronVO insert(@RequestBody @Validated(InsertGroup.class) QuesByPronVO sv) {
		
		return modelMapper.map(
			quesLogic.insert(modelMapper.map(sv, QuesByPronEntity.class)), QuesByPronVO.class);
	}
	
	@PostMapping("/delete")
	public void delete(@RequestBody @Validated(DeleteGroup.class) QuesByPronVO sv) {
		
		quesLogic.delete(modelMapper.map(sv, QuesByPronEntity.class));
	}
	
	@PostMapping("/update")
	public void update(@RequestBody @Validated(UpdateGroup.class) QuesByPronVO sv) {
		
		quesLogic.update(modelMapper.map(sv, QuesByPronEntity.class));
	}

	@PostMapping("/findByPron")
	public QuesByPronVO findByPron(@RequestBody @Validated(SelectGroup.class) QuesByPronVO sv) {
		
		QuesByPronEntity se = quesLogic.findByPron(modelMapper.map(sv, QuesByPronEntity.class));
		
		if (null == se) {
			
			return null;
		}
		return modelMapper.map(se, QuesByPronVO.class);
	}

	@PostMapping("/findByResp")
	public QuesByRespVO findByResp(@RequestBody @Validated(SelectGroup.class) QuesByRespVO sv) {
		
		QuesByRespEntity se = quesLogic.findByResp(modelMapper.map(sv, QuesByRespEntity.class));
		
		if (null == se) {
			
			return null;
		}
		return modelMapper.map(se, QuesByRespVO.class);
	}
	
	@PostMapping("/findByExp")
	public Page<QuesByExpVO> findByExp(
		@RequestBody @Validated(SelectGroup.class) QuesByExpVO sv, Integer pageNum, Integer pageSize) {
		
		Page<QuesByExpEntity> sel = quesLogic.findByExp(
			modelMapper.map(sv, QuesByExpEntity.class), pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<QuesByExpVO>>() {}.getType());
	}
	
	@PostMapping("/findList")
	public Page<QuesByRespVO> findList(Integer pageNum, Integer pageSize) {
		
		Page<QuesByRespEntity> sel = quesLogic.findList(pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<QuesByRespVO>>() {}.getType());
	}
	
	@PostMapping("/findCommonList")
	public Page<QuesByRespVO> findCommonList(Integer pageNum, Integer pageSize) {
		
		Page<QuesByRespEntity> sel = quesLogic.findCommonList(pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<QuesByRespVO>>() {}.getType());
	}
}
