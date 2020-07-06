package cn.nbbandxdd.survey.resprec.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import cn.nbbandxdd.survey.resprec.dao.entity.DtlRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecExamStatEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecGrpStatEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecUsrRankEntity;
import cn.nbbandxdd.survey.resprec.logic.RespRecLogic;
import cn.nbbandxdd.survey.resprec.service.vo.DtlRecVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecExamStatVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecGrpStatVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecUsrRankVO;
import cn.nbbandxdd.survey.resprec.service.vo.RespRecVO;

@RestController
@RequestMapping("/resprec")
public class RespRecService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RespRecLogic respRecLogic;
	
	@PostMapping("/insertDtl")
	public DtlRecVO insertDtl(@RequestBody @Validated(InsertGroup.class) DtlRecVO sv) {
		
		return modelMapper.map(
			respRecLogic.insertDtl(modelMapper.map(sv, DtlRecEntity.class)), DtlRecVO.class);
	}
	
	@PostMapping("/insertResp")
	public RespRecVO insertResp(@RequestBody @Validated(InsertGroup.class) RespRecVO sv) {
		
		return modelMapper.map(
			respRecLogic.insertResp(modelMapper.map(sv, RespRecEntity.class)), RespRecVO.class);
	}
	
	@PostMapping("/findRespList")
	public Page<RespRecVO> findRespList(Integer pageNum, Integer pageSize) {
		
		Page<RespRecEntity> sel = respRecLogic.findRespList(pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<RespRecVO>>() {}.getType());
	}
	
	@PostMapping("/findUsrRank")
	public Page<RespRecUsrRankVO> findUsrRank(
		@RequestBody @Validated(SelectGroup.class) RespRecUsrRankVO sv, Integer pageNum, Integer pageSize) {
		
		Page<RespRecUsrRankEntity> sel = respRecLogic.findUsrRank(
			modelMapper.map(sv, RespRecUsrRankEntity.class), pageNum, pageSize);
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<RespRecUsrRankVO>>() {}.getType());
	}
	
	@PostMapping("/findGrpStat")
	public List<RespRecGrpStatVO> findGrpStat(@RequestBody @Validated(SelectGroup.class) RespRecGrpStatVO sv) {
		
		List<RespRecGrpStatEntity> sel = respRecLogic.findGrpStat(
			modelMapper.map(sv, RespRecGrpStatEntity.class));
		
		if (null == sel) {
			
			return null;
		}
		return modelMapper.map(sel, new TypeToken<Page<RespRecGrpStatVO>>() {}.getType());
	}
	
	@PostMapping("/findExamStat")
	public RespRecExamStatVO findExamStat(@RequestBody @Validated(SelectGroup.class) RespRecExamStatVO sv) {
		
		return modelMapper.map(
			respRecLogic.findExamStat(modelMapper.map(sv, RespRecExamStatEntity.class)),
			RespRecExamStatVO.class);
	}
}
