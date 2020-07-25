package cn.nbbandxdd.survey.unijob.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import cn.nbbandxdd.survey.unijob.dao.entity.UniJobEntity;

/**
 * <p>唯一任务DAO。
 * 
 * <ul>
 * <li>依据任务类型{@code jobTyp}和上次执行日期{@code srcDat}更新最近执行日期，使用{@link #update(UniJobEntity)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Mapper
public interface UniJobDAO {

	/**
	 * <p>依据任务类型{@code jobTyp}和上次执行日期{@code srcDat}更新最近执行日期。
	 * 
	 * @param entity 唯一任务Entity
	 * @return 更新匹配记录数
	 * @see UniJobEntity
	 */
	@Update(
		"UPDATE UNI_JOB " +
		"SET DAT = #{dstDat} " +
		"WHERE JOB_TYP = #{jobTyp} AND DAT = #{srcDat}"
	)
	int update(UniJobEntity entity);
}
