package cn.nbbandxdd.survey.exam.service;

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
import cn.nbbandxdd.survey.exam.dao.entity.ExamEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamQuesRlnEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamStatusEntity;
import cn.nbbandxdd.survey.exam.logic.ExamLogic;
import cn.nbbandxdd.survey.exam.service.vo.ExamQuesRlnVO;
import cn.nbbandxdd.survey.exam.service.vo.ExamStatusVO;
import cn.nbbandxdd.survey.exam.service.vo.ExamVO;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;

@RestController
@RequestMapping("/exam")
public class ExamService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ExamLogic examLogic;
	
	@PostMapping("/insert")
	public ExamVO insert(
		@RequestBody @Validated(InsertGroup.class) ExamVO sv) {
		
		return modelMapper.map(
			examLogic.insert(modelMapper.map(sv, ExamEntity.class)), ExamVO.class);
	}
	
	@PostMapping("/delete")
	public void delete(@RequestBody @Validated(DeleteGroup.class) ExamVO sv) {
		
		examLogic.delete(modelMapper.map(sv, ExamEntity.class));
	}
	
	@PostMapping("/update")
	public void update(@RequestBody @Validated(UpdateGroup.class) ExamVO sv) {
		
		examLogic.update(modelMapper.map(sv, ExamEntity.class));
	}
	
	@PostMapping("/findStatus")
	public ExamStatusVO findStatus(@RequestBody @Validated(SelectGroup.class) ExamStatusVO sv) {
		
		return modelMapper.map(
			examLogic.findStatus(modelMapper.map(sv, ExamStatusEntity.class)), ExamStatusVO.class);
	}
	
	@PostMapping("/findDetail")
	public ExamVO findDetail(@RequestBody @Validated(SelectGroup.class) ExamVO sv) {
		
		ExamEntity se = examLogic.findDetail(modelMapper.map(sv, ExamEntity.class));
		
		if (null == se) {
			
			return null;
		}
		return modelMapper.map(se, ExamVO.class);
	}
	
	@PostMapping("/findQuesList")
	public Page<QuesByRespVO> findQuesList(@RequestBody @Validated(SelectGroup.class) ExamVO sv,
		Integer pageNum, Integer pageSize) {

		Page<QuesByRespEntity> sel = examLogic.findQuesList(
			modelMapper.map(sv, ExamEntity.class), pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<QuesByRespVO>>() {}.getType());
	}
	
	@PostMapping("/findToAnsw")
	public ExamVO findToAnsw(@RequestBody @Validated(SelectGroup.class) ExamVO sv) {
		
		ExamEntity se = examLogic.findToAnsw(modelMapper.map(sv, ExamEntity.class));
		
		if (null == se) {
			
			return null;
		}
		return modelMapper.map(se, ExamVO.class);
	}
	
	@PostMapping("/findList")
	public Page<ExamVO> findList(Integer pageNum, Integer pageSize) {
		
		Page<ExamEntity> sel = examLogic.findList(pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<ExamVO>>() {}.getType());
	}
	
	@PostMapping("/findCommonList")
	public Page<ExamVO> findCommonList(Integer pageNum, Integer pageSize) {
		
		Page<ExamEntity> sel = examLogic.findCommonList(pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<ExamVO>>() {}.getType());
	}
	
	@PostMapping("/insertQues")
	public void insertQues(@RequestBody @Validated(InsertGroup.class) ExamQuesRlnVO sv) {
		
		examLogic.insertQues(modelMapper.map(sv, ExamQuesRlnEntity.class));
	}
	
	@PostMapping("/deleteQues")
	public void deleteQues(@RequestBody @Validated(DeleteGroup.class) ExamQuesRlnVO sv) {
		
		examLogic.deleteQues(modelMapper.map(sv, ExamQuesRlnEntity.class));
	}
	
	@PostMapping("/findAvaQues")
	public Page<QuesByRespVO> findAvaQues(@RequestBody @Validated(SelectGroup.class) ExamQuesRlnVO sv,
		Integer pageNum, Integer pageSize) {
		
		Page<QuesByRespEntity> sel = examLogic.findAvaQues(
			modelMapper.map(sv, ExamQuesRlnEntity.class), pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel,new TypeToken<Page<QuesByRespVO>>() {}.getType());
	}
}
