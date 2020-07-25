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

/**
 * <p>用户信息Service。
 * 
 * <ul>
 * <li>新增实名登记，使用 {@link #insert(UsrInfoVO)}。</li>
 * <li>修改实名登记，使用 {@link #update(UsrInfoVO)}。</li>
 * <li>查看实名登记，使用 {@link #load()}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@RestController
@RequestMapping("/usrinfo")
public class UsrInfoService {
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UsrInfoLogic usrInfoLogic;
	
	/**
	 * <p>新增实名登记。
	 * 
	 * <p>输入字段：
	 * <ul>
	 * <li>{@code dprtNam}：部门名（选输）。如果为空白，则依据职能组名确定。
	 * 校验失败情形：为空白时，职能组名无法唯一确定部门名；不为空白时，与职能组名不匹配。</li>
	 * <li>{@code grpNam}：职能组名（必输）。校验失败情形：空白字段；与部门名不匹配。</li>
	 * <li>{@code usrNam}：用户名（必输）。校验失败情形：空白字段。</li>
	 * </ul>
	 * 
	 * <p>输出字段：无。
	 * 
	 * @param sv 用户信息VO
	 * @see UsrInfoVO
	 */
	@PostMapping("/insert")
	public void insert(@RequestBody @Validated(InsertGroup.class) UsrInfoVO sv) {
		
		usrInfoLogic.insert(modelMapper.map(sv, UsrInfoEntity.class));
	}
	
	/**
	 * <p>修改实名登记。
	 * 
	 * <p>输入字段：
	 * <ul>
	 * <li>{@code dprtNam}：部门名（选输）。如果为空白，则依据职能组名确定。
	 * 校验失败情形：为空白时，职能组名无法唯一确定部门名；不为空白时，与职能组名不匹配。</li>
	 * <li>{@code grpNam}：职能组名（必输）。校验失败情形：空白字段；与部门名不匹配。</li>
	 * <li>{@code usrNam}：用户名（必输）。校验失败情形：空白字段。</li>
	 * </ul>
	 * 
	 * <p>输出字段：无。
	 * 
	 * @param sv 用户信息VO
	 * @see UsrInfoVO
	 */
	@PostMapping("/update")
	public void update(@RequestBody @Validated(UpdateGroup.class) UsrInfoVO sv) {
		
		usrInfoLogic.update(modelMapper.map(sv, UsrInfoEntity.class));
	}
	
	/**
	 * <p>查看实名登记。依据Http请求报文头authorization，返回用户实名登记信息。
	 * 
	 * <p>输入字段：无。
	 * 
	 * <p>输出字段：
	 * <ul>
	 * <li>{@code dprtNam}：部门名。</li>
	 * <li>{@code grpNam}：职能组名。</li>
	 * <li>{@code usrNam}：用户名。</li>
	 * </ul>
	 * 
	 * @return 用户信息VO
	 * @see UsrInfoVO
	 */
	@PostMapping("/load")
	public UsrInfoVO load() {
		
		UsrInfoEntity se = usrInfoLogic.loadByKey();
		
		if (null == se) {
			
			return null;
		}
		return modelMapper.map(se, UsrInfoVO.class);
	}
}
