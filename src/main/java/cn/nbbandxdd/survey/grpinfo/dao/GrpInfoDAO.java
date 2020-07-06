package cn.nbbandxdd.survey.grpinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import cn.nbbandxdd.survey.grpinfo.dao.entity.GrpInfoEntity;

@Mapper
public interface GrpInfoDAO {

	@Select(
		"SELECT DPRT_NAM, GRP_NAM, SEQ_NO " +
		"FROM GRP_INFO " +
		"WHERE DPRT_NAM = #{dprtNam} AND GRP_NAM = #{grpNam}"
	)
	@Results(id = "GrpInfo", value = {
		@Result(property = "dprtNam", column = "DPRT_NAM", javaType = String.class),
		@Result(property = "grpNam", column = "GRP_NAM", javaType = String.class),
		@Result(property = "seqNo", column = "SEQ_NO", javaType = Integer.class)
	})
	GrpInfoEntity loadByKey(GrpInfoEntity entity);
	
	@Select(
		"SELECT GRP_NAM " +
		"FROM GRP_INFO " +
		"WHERE DPRT_NAM = #{dprtNam} " +
		"ORDER BY SEQ_NO"
	)
	@ResultMap("GrpInfo")
	List<GrpInfoEntity> findGrpList(GrpInfoEntity entity);
	
	@Select(
		"SELECT DISTINCT DPRT_NAM " +
		"FROM GRP_INFO " +
		"ORDER BY SEQ_NO"
	)
	@ResultMap("GrpInfo")
	List<GrpInfoEntity> findDprtList();

	@Select(
		"SELECT DPRT_NAM, GRP_NAM, SEQ_NO " +
		"FROM GRP_INFO " +
		"WHERE GRP_NAM = #{grpNam}"
	)
	@ResultMap("GrpInfo")
	List<GrpInfoEntity> findByGrp(GrpInfoEntity entity);
}
