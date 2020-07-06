package cn.nbbandxdd.survey.unijob.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import cn.nbbandxdd.survey.unijob.dao.entity.UniJobEntity;

@Mapper
public interface UniJobDAO {

	@Update(
		"UPDATE UNI_JOB " +
		"SET DAT = #{dstDat} " +
		"WHERE JOB_TYP = #{jobTyp} AND DAT = #{srcDat}"
	)
	int update(UniJobEntity entity);
}
