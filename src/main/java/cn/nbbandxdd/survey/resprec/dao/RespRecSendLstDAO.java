package cn.nbbandxdd.survey.resprec.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.nbbandxdd.survey.resprec.dao.entity.RespRecSendLstEntity;

@Mapper
public interface RespRecSendLstDAO {

	@Update(
		"UPDATE RESP_REC_SEND_LST " +
		"SET LAST_SEND_TMSTP = #{lastSendTmstp, jdbcType = TIMESTAMP}, ERR_MSG = #{errMsg} " +
		"WHERE OPEN_ID = #{openId} AND DPRT_NAM = #{dprtNam}"
	)
	int update(RespRecSendLstEntity entity);
	
	@Select(
		"SELECT OPEN_ID, DPRT_NAM, MAIL_ADDR, MAIL_SUBJ, LAST_SEND_TMSTP, ERR_MSG " +
		"FROM RESP_REC_SEND_LST"
	)
	@Results(id = "RespRecSendLst", value = {
		@Result(property = "openId", column = "OPEN_ID", javaType = String.class),
		@Result(property = "dprtNam", column = "DPRT_NAM", javaType = String.class),
		@Result(property = "mailAddr", column = "MAIL_ADDR", javaType = String.class),
		@Result(property = "mailSubj", column = "MAIL_SUBJ", javaType = String.class),
		@Result(property = "lastSendTmstp", column = "LAST_SEND_TMSTP", javaType = Timestamp.class),
		@Result(property = "errMsg", column = "ERR_MSG", javaType = String.class)
	})
	List<RespRecSendLstEntity> findList();
}
