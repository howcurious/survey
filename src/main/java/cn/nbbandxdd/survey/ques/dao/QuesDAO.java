package cn.nbbandxdd.survey.ques.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Many;

import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.ques.dao.entity.AnswEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByPronEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByExpEntity;

@Mapper
public interface QuesDAO {

	@Insert(
		"<script>" +
		"INSERT INTO " +
		"QUES (QUES_CD, TYP_CD, DSC, COMM, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) " +
		"VALUES (#{quesCd}, #{typCd}, #{dsc}, #{comm}, #{lastMantUsr}, #{lastMantDat}, #{lastMantTmstp, jdbcType = TIMESTAMP});" +
		
		"INSERT INTO " +
		"ANSW (QUES_CD, ANSW_CD, TYP_CD, DSC, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) " +
		"VALUES " +
		"<foreach collection = 'answList' open = '' item = 'item' separator = ',' close = ''>" +
			"(#{quesCd}, #{item.answCd}, #{typCd}, #{item.dsc}, #{item.scre}, #{lastMantUsr}, #{lastMantDat}, #{lastMantTmstp, jdbcType = TIMESTAMP})" +
		"</foreach>" +
		";" +
		"</script>"
	)
	int insert(QuesByPronEntity entity);
	
	@Delete(
		"DELETE FROM QUES WHERE QUES_CD = #{quesCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		"DELETE FROM ANSW WHERE QUES_CD = #{quesCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		"DELETE FROM EXAM_QUES_RLN WHERE QUES_CD = #{quesCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		"DELETE FROM DTL_REC WHERE EXISTS (SELECT 1 FROM QUES WHERE DTL_REC.QUES_CD = QUES.QUES_CD AND QUES.QUES_CD = #{quesCd} AND QUES.LAST_MANT_USR = #{lastMantUsr});"
	)
    int delete(QuesByPronEntity entity);
	
	@Update(
		"<script>" +
		"UPDATE QUES " +
		"SET TYP_CD = #{typCd}, DSC = #{dsc}, COMM = #{comm} " +
		"WHERE QUES_CD = #{quesCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		
		"DELETE FROM ANSW WHERE QUES_CD = #{quesCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		
		"INSERT INTO " +
		"ANSW (QUES_CD, ANSW_CD, TYP_CD, DSC, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) " +
		"VALUES " +
		"<foreach collection = 'answList' open = '' item = 'item' separator = ',' close = ''>" +
			"(#{quesCd}, #{item.answCd}, #{typCd}, #{item.dsc}, #{item.scre}, #{lastMantUsr}, #{lastMantDat}, #{lastMantTmstp, jdbcType = TIMESTAMP})" +
		"</foreach>" +
		";" +
		"</script>"
	)
	int update(QuesByPronEntity entity);
	
	@Select(
		"SELECT QUES_CD, ANSW_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM ANSW " +
		"WHERE QUES_CD = #{quesCd}"
	)
	@Results(id = "AnswWithoutScre", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "answCd", column = "ANSW_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class)
	})
	List<AnswEntity> findAnswWithoutScreList(@Param("quesCd") String quesCd);
	
	@Select(
		"SELECT QUES_CD, ANSW_CD, TYP_CD, DSC, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM ANSW " +
		"WHERE QUES_CD = #{quesCd}"
	)
	@Results(id = "AnswWithScre", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "answCd", column = "ANSW_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "scre", column = "SCRE", javaType = Integer.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class)
	})
	List<AnswEntity> findAnswWithScreList(@Param("quesCd") String quesCd);
	
	@Select(
		"SELECT QUES_CD, TYP_CD, DSC, COMM, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM QUES " +
		"WHERE QUES_CD = #{quesCd} AND LAST_MANT_USR IN ('everyone', #{lastMantUsr})"
	)
	@Results(id = "QuesByPron", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "comm", column = "COMM", javaType = String.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class),
		@Result(property = "answList", column = "QUES_CD",
			many = @Many(select = "cn.nbbandxdd.survey.ques.dao.QuesDAO.findAnswWithScreList"))
	})
	QuesByPronEntity findByPron(QuesByPronEntity entity);
	
	@Select(
		"SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM QUES " +
		"WHERE QUES_CD = #{quesCd}"
	)
	@Results(id = "QuesByResp", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class),
		@Result(property = "answList", column = "QUES_CD",
			many = @Many(select = "cn.nbbandxdd.survey.ques.dao.QuesDAO.findAnswWithoutScreList"))
	})
	QuesByRespEntity findByResp(QuesByRespEntity entity);
	
	@Select(
		"SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, QUES.COMM AS COMM, " +
			"QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP, " +
			"RLN.EXAM_CD AS EXAM_CD " +
		"FROM QUES " +
		"RIGHT JOIN (SELECT EXAM_CD, QUES_CD, SEQ_NO FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}) RLN " +
			"ON RLN.QUES_CD = QUES.QUES_CD " +
		"ORDER BY RLN.SEQ_NO"
	)
	@Results(id = "QuesByExp", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "comm", column = "COMM", javaType = String.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class),
		@Result(property = "answList", column = "QUES_CD",
			many = @Many(select = "cn.nbbandxdd.survey.ques.dao.QuesDAO.findAnswWithScreList")),
		@Result(property = "selList", column = "{examCd = EXAM_CD, quesCd = QUES_CD}",
			many = @Many(select = "cn.nbbandxdd.survey.resprec.dao.RespRecDAO.findSelAnsw"))
	})
	Page<QuesByExpEntity> findByExp(QuesByExpEntity entity);
	
	@Select(
		"SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM QUES " +
		"WHERE LAST_MANT_USR = #{lastMantUsr} " +
		"ORDER BY QUES_CD DESC"
	)
	@Results(id = "QuesList", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class)
	})
	Page<QuesByRespEntity> findList(QuesByRespEntity entity);

	@Select(
		"SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, " +
			"QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, " +
			"QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP " +
		"FROM (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}) RLN " +
		"LEFT JOIN QUES ON RLN.QUES_CD = QUES.QUES_CD " +
		"WHERE SEQ_NO > (" +
			"SELECT CASE WHEN MAX(RLN.SEQ_NO) IS NULL THEN -1 ELSE MAX(RLN.SEQ_NO) END " +
			"FROM (SELECT * FROM DTL_REC WHERE OPEN_ID = '${@cn.nbbandxdd.survey.common.jwt.JwtHolder@get()}' AND EXAM_CD = #{examCd}) REC " +
			"LEFT JOIN (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}) RLN " +
			"ON REC.EXAM_CD = RLN.EXAM_CD AND REC.QUES_CD = RLN.QUES_CD" +
		") ORDER BY RLN.SEQ_NO"
	)
	@ResultMap("QuesList")
	List<QuesByRespEntity> findToAnswQuesList(@Param("examCd") String examCd);

	@Select(
		"SELECT ANSW_CD FROM ANSW WHERE QUES_CD = #{quesCd} AND SCRE > 0"
	)
	List<String> findRightAnsw(@Param("quesCd") String quesCd);
	
	@Select(
		"SELECT TYP_CD FROM QUES WHERE QUES_CD = #{quesCd}"
	)
	String fillbackTypCd(@Param("quesCd") String quesCd);
}
