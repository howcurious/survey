package cn.nbbandxdd.survey.pubserno.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.nbbandxdd.survey.pubserno.dao.entity.PubSerNoEntity;

@Mapper
public interface PubSerNoDAO {

	@Select(
		"SELECT SER_NO_TYP, SER_NO_NAM, BGN_SER_NO, CUR_SER_NO, END_SER_NO, STP_SPRD, FMT_OUT_LEN " +
		"FROM PUB_SER_NO " +
		"WHERE SER_NO_TYP = #{serNoTyp} " +
		"FOR UPDATE"
	)
	@Results(id = "PubSerNo", value = {
		@Result(property = "serNoTyp", column = "SER_NO_TYP", javaType = String.class),
		@Result(property = "bgnSerNo", column = "BGN_SER_NO", javaType = Integer.class),
		@Result(property = "curSerNo", column = "CUR_SER_NO", javaType = Integer.class),
		@Result(property = "endSerNo", column = "END_SER_NO", javaType = Integer.class),
		@Result(property = "stpSprd", column = "STP_SPRD", javaType = Integer.class),
		@Result(property = "fmtOutLen", column = "FMT_OUT_LEN", javaType = Integer.class)
	})
	PubSerNoEntity loadByKey(PubSerNoEntity entity);
	
	@Update(
		"UPDATE PUB_SER_NO " +
		"SET CUR_SER_NO = #{curSerNo} " +
		"WHERE SER_NO_TYP = #{serNoTyp}"
	)
	int update(PubSerNoEntity entity);
}
