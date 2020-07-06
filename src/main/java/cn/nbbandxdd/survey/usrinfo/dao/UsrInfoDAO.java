package cn.nbbandxdd.survey.usrinfo.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.nbbandxdd.survey.usrinfo.dao.entity.UsrInfoEntity;

@Mapper
public interface UsrInfoDAO {

	@Insert(
		"INSERT INTO " +
		"USR_INFO (OPEN_ID, DPRT_NAM, GRP_NAM, USR_NAM, REG_TMSTP) " +
		"VALUES (#{openId}, #{dprtNam}, #{grpNam}, #{usrNam}, #{regTmstp, jdbcType = TIMESTAMP})"
	)
    int insert(UsrInfoEntity entity);
	
	@Update(
		"UPDATE USR_INFO " +
		"SET DPRT_NAM = #{dprtNam}, GRP_NAM = #{grpNam}, USR_NAM = #{usrNam} " +
		"WHERE OPEN_ID = #{openId}"
	)
	int update(UsrInfoEntity entity);
	
	@Select(
		"SELECT OPEN_ID, DPRT_NAM, GRP_NAM, USR_NAM, REG_TMSTP " +
		"FROM USR_INFO " +
		"WHERE OPEN_ID = #{openId}"
	)
	@Results(id = "UsrInfo", value = {
		@Result(property = "openId", column = "OPEN_ID", javaType = String.class),
		@Result(property = "dprtNam", column = "DPRT_NAM", javaType = String.class),
		@Result(property = "grpNam", column = "GRP_NAM", javaType = String.class),
		@Result(property = "usrNam", column = "USR_NAM", javaType = String.class),
		@Result(property = "regTmstp", column = "REG_TMSTP", javaType = Timestamp.class)
	})
	UsrInfoEntity loadByKey(UsrInfoEntity entity);
	
	@Select(
		"SELECT COUNT(1) " +
		"FROM USR_INFO " +
		"WHERE DPRT_NAM = #{dprtNam} AND GRP_NAM = #{grpNam}"
	)
	int findUsrCntByGrp(@Param("dprtNam") String dprtNam, @Param("grpNam") String grpNam);
	
	@Select(
		"SELECT USR_NAM FROM USR_INFO WHERE OPEN_ID = #{openId}"
	)
	String fillbackUsrNam(@Param("openId") String openId);
	
	@Select(
		"SELECT GRP_NAM FROM USR_INFO WHERE OPEN_ID = #{openId}"
	)
	String fillbackGrpNam(@Param("openId") String openId);
	
	@Select(
		"SELECT DPRT_NAM FROM USR_INFO WHERE OPEN_ID = #{openId}"
	)
	String fillbackDprtNam(@Param("openId") String openId);
}
