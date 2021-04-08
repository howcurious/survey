package cn.nbbandxdd.survey.exam.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.exam.dao.entity.ExamEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamIntvEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamQuesRlnEntity;
import cn.nbbandxdd.survey.exam.dao.entity.ExamQuesTypRlnEntity;
import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;

@Mapper
public interface ExamDAO {

	@Insert(
		"<script>" +
		"INSERT INTO " +
		"EXAM (EXAM_CD, TYP_CD, RPET_IND, CNTDWN_IND, ANSW_IMM_IND, TTL, DSC, BGN_TIME, END_TIME, SEQ_NO, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) " +
		"VALUES (#{examCd}, #{typCd}, #{rpetInd}, #{cntdwnInd}, #{answImmInd}, #{ttl}, #{dsc}, #{bgnTime, jdbcType = TIMESTAMP}, #{endTime, jdbcType = TIMESTAMP}, #{seqNo}, #{lastMantUsr}, #{lastMantDat}, #{lastMantTmstp, jdbcType = TIMESTAMP});" +
		
		"<if test=\"typCd == '2'.toString\">" +
			"INSERT INTO EXAM_QUES_TYP_RLN (EXAM_CD, QUES_TYP_CD, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) VALUES " +
			"<foreach collection = 'quesTypRlnList' open = '' item = 'item' separator = ',' close = ''>" +
				"(#{examCd}, #{item.quesTypCd}, #{item.scre}, #{lastMantUsr}, #{lastMantDat}, #{lastMantTmstp})" +
			"</foreach>" +
			";" +
			
			"INSERT INTO EXAM_QUES_RLN " +
			"SELECT #{examCd} AS EXAM_CD, Q.QUES_CD AS QUES_CD, @rank := @rank + 1 AS SEQ_NO, " +
				"#{lastMantUsr} AS LAST_MANT_USR, #{lastMantDat} AS LAST_MANT_DAT, #{lastMantTmstp} AS LAST_MANT_TMSTP " +
			"FROM (" +
			"<foreach collection = 'quesTypRlnList' open = '' item = 'item' separator = ' UNION ALL ' close = ''>" +
				"(SELECT * FROM QUES WHERE LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = #{lastMantUsr}) AND TYP_CD = #{item.quesTypCd} ORDER BY RAND() LIMIT #{item.cnt})" +
			"</foreach>" + ") Q, " +
			"(SELECT @rank := -1) R" +
			";" +
		"</if>" +
		"</script>"
	)
    int insert(ExamEntity entity);

	@Delete(
		"DELETE FROM EXAM WHERE EXAM_CD = #{examCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		"DELETE FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		"DELETE FROM EXAM_QUES_TYP_RLN WHERE EXAM_CD = #{examCd} AND LAST_MANT_USR = #{lastMantUsr};" +
		"DELETE FROM RESP_REC REC " +
		"WHERE EXISTS (" +
			"SELECT 1 FROM EXAM " +
			"WHERE REC.EXAM_CD = EXAM.EXAM_CD AND EXAM.EXAM_CD = #{examCd} AND EXAM.LAST_MANT_USR = #{lastMantUsr}" +
		");" +
		"DELETE FROM DTL_REC REC " +
		"WHERE EXISTS (" +
			"SELECT 1 FROM EXAM " +
			"WHERE REC.EXAM_CD = EXAM.EXAM_CD AND EXAM.EXAM_CD = #{examCd} AND EXAM.LAST_MANT_USR = #{lastMantUsr}" +
		");"
	)
	int delete(ExamEntity entity);

	@Update(
		"<script>" +
		"UPDATE EXAM " +
		"<set>" +
			"<if test = 'typCd != null'>TYP_CD = #{typCd},</if>" +
			"<if test = 'rpetInd != null'>RPET_IND = #{rpetInd},</if>" +
			"<if test = 'cntdwnInd != null'>CNTDWN_IND = #{cntdwnInd},</if>" +
			"<if test = 'answImmInd != null'>ANSW_IMM_IND = #{answImmInd},</if>" +
			"<if test = 'ttl != null'>TTL = #{ttl},</if>" +
			"<if test = 'dsc != null'>DSC = #{dsc},</if>" +
			"<if test = 'bgnTime != null'>BGN_TIME = #{bgnTime, jdbcType = TIMESTAMP},</if>" +
			"<if test = 'endTime != null'>END_TIME = #{endTime, jdbcType = TIMESTAMP},</if>" +
			"<if test = 'lastMantDat != null'>LAST_MANT_DAT = #{lastMantDat},</if>" +
			"<if test = 'lastMantTmstp != null'>LAST_MANT_TMSTP = #{lastMantTmstp, jdbcType = TIMESTAMP},</if>" +
		"</set>" +
		"WHERE EXAM_CD = #{examCd} AND LAST_MANT_USR = #{lastMantUsr}" +
		"</script>"
	)
	int update(ExamEntity entity);
	
	@Select(
		"SELECT EXAM_CD, TYP_CD, RPET_IND, CNTDWN_IND, ANSW_IMM_IND, TTL, DSC, BGN_TIME, END_TIME, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM EXAM " +
		"WHERE EXAM_CD = #{examCd}"
	)
	@Results(id = "ExamWithQuesTyp", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "rpetInd", column = "RPET_IND", javaType = String.class),
		@Result(property = "cntdwnInd", column = "CNTDWN_IND", javaType = String.class),
		@Result(property = "answImmInd", column = "ANSW_IMM_IND", javaType = String.class),
		@Result(property = "ttl", column = "TTL", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "bgnTime", column = "BGN_TIME", javaType = Timestamp.class),
		@Result(property = "endTime", column = "END_TIME", javaType = Timestamp.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class),
		@Result(property = "quesCnt", column = "EXAM_CD",
			one = @One(select = "cn.nbbandxdd.survey.exam.dao.ExamDAO.findQuesCnt")),
		@Result(property = "quesTypRlnList", column = "EXAM_CD",
			many = @Many(select = "cn.nbbandxdd.survey.exam.dao.ExamDAO.findExamQuesTypRlnList"))
	})
	ExamEntity findDetail(ExamEntity entity);
	
	@Select(
		"SELECT CASE WHEN COUNT(1) IS NULL THEN 0 ELSE COUNT(1) END " +
		"FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}"
	)
	int findQuesCnt(@Param("examCd") String examCd);
	
	@Select(
		"SELECT QRLN.EXAM_CD AS EXAM_CD, TRLN.QUES_TYP_CD AS QUES_TYP_CD, TRLN.SCRE AS SCRE, COUNT(1) AS CNT " +
		"FROM (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}) QRLN " +
		"LEFT JOIN QUES ON QUES.QUES_CD = QRLN.QUES_CD " +
		"INNER JOIN (SELECT * FROM EXAM_QUES_TYP_RLN WHERE EXAM_CD = #{examCd}) TRLN " +
		"ON QRLN.EXAM_CD = TRLN.EXAM_CD AND QUES.TYP_CD = TRLN.QUES_TYP_CD " +
		"GROUP BY QUES.TYP_CD"
	)
	@Results(id = "ExamQuesTypRln", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "quesTypCd", column = "QUES_TYP_CD", javaType = String.class),
		@Result(property = "scre", column = "SCRE", javaType = Integer.class),
		@Result(property = "cnt", column = "CNT", javaType = Integer.class)
	})
	List<ExamQuesTypRlnEntity> findExamQuesTypRlnList(@Param("examCd") String examCd);
	
	@Select(
		"SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, " +
			"QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP " +
		"FROM QUES " +
		"RIGHT JOIN (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}) RLN " +
			"ON QUES.QUES_CD = RLN.QUES_CD " +
		"ORDER BY RLN.SEQ_NO"
	)
	@Results(id = "QuesList", value = {
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class)
	})
	Page<QuesByRespEntity> findQuesList(ExamEntity entity);
	
	@Select(
		"SELECT EXAM_CD, TYP_CD, RPET_IND, CNTDWN_IND, ANSW_IMM_IND, TTL, DSC, BGN_TIME, END_TIME, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM EXAM " +
		"WHERE EXAM_CD = #{examCd}"
	)
	@Results(id = "ExamWithQues", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "rpetInd", column = "RPET_IND", javaType = String.class),
		@Result(property = "cntdwnInd", column = "CNTDWN_IND", javaType = String.class),
		@Result(property = "answImmInd", column = "ANSW_IMM_IND", javaType = String.class),
		@Result(property = "ttl", column = "TTL", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "bgnTime", column = "BGN_TIME", javaType = Timestamp.class),
		@Result(property = "endTime", column = "END_TIME", javaType = Timestamp.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class),
		@Result(property = "quesList", column = "EXAM_CD",
			many = @Many(select = "cn.nbbandxdd.survey.ques.dao.QuesDAO.findToAnswQuesList"))
	})
	ExamEntity findToAnsw(ExamEntity entity);

	@Select(
		"SELECT EXAM_CD, TYP_CD, RPET_IND, CNTDWN_IND, ANSW_IMM_IND, TTL, DSC, BGN_TIME, END_TIME, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM EXAM " +
		"WHERE LAST_MANT_USR = #{lastMantUsr} " +
		"ORDER BY EXAM_CD DESC"
	)
	@Results(id = "ExamList", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "typCd", column = "TYP_CD", javaType = String.class),
		@Result(property = "rpetInd", column = "RPET_IND", javaType = String.class),
		@Result(property = "cntdwnInd", column = "CNTDWN_IND", javaType = String.class),
		@Result(property = "answImmInd", column = "ANSW_IMM_IND", javaType = String.class),
		@Result(property = "ttl", column = "TTL", javaType = String.class),
		@Result(property = "dsc", column = "DSC", javaType = String.class),
		@Result(property = "bgnTime", column = "BGN_TIME", javaType = Timestamp.class),
		@Result(property = "endTime", column = "END_TIME", javaType = Timestamp.class),
		@Result(property = "lastMantUsr", column = "LAST_MANT_USR", javaType = String.class),
		@Result(property = "lastMantDat", column = "LAST_MANT_DAT", javaType = String.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class)
	})
	Page<ExamEntity> findList(ExamEntity entity);
	
	@Select(
		"SELECT EXAM_CD, TYP_CD, RPET_IND, CNTDWN_IND, ANSW_IMM_IND, TTL, DSC, BGN_TIME, END_TIME, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM EXAM " +
		"WHERE EXAM_CD = #{examCd}"
	)
	@ResultMap("ExamList")
	ExamEntity loadByKey(ExamEntity entity);
	
	@Select(
		"SELECT SEQ_NO FROM EXAM WHERE EXAM_CD = #{examCd} AND LAST_MANT_USR = #{lastMantUsr} FOR UPDATE"
	)
	Integer loadSeqByKey(ExamEntity entity);
	
	@Insert(
		"<script>" +
		"UPDATE EXAM SET SEQ_NO = #{seqNo} + ${@org.apache.commons.collections4.CollectionUtils@size(quesCds)} WHERE EXAM_CD = #{examCd};" +
		"INSERT INTO " +
		"EXAM_QUES_RLN (EXAM_CD, QUES_CD, SEQ_NO, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) " +
		"VALUES " +
		"<foreach collection = 'quesCds' open = '' item = 'quesCd' index = 'idx' separator = ',' close = ''>" +
			"(#{examCd}, #{quesCd}, #{idx} + #{seqNo}, #{lastMantUsr}, #{lastMantDat}, #{lastMantTmstp, jdbcType = TIMESTAMP})" +
		"</foreach>" +
		";" +
		"</script>"
	)
	int insertQues(ExamQuesRlnEntity entity);

	@Delete(
		"<script>" +
		"DELETE FROM EXAM_QUES_RLN " +
		"WHERE EXAM_CD = #{examCd} AND LAST_MANT_USR = #{lastMantUsr} AND QUES_CD IN " +
		"<foreach collection = 'quesCds' open = '(' item = 'quesCd' separator = ',' close = ')'>" +
			"#{quesCd}" +
		"</foreach>" +
		"</script>"
	)
	int deleteQues(ExamQuesRlnEntity entity);
	
	@Select(
		"SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
		"FROM QUES " +
		"WHERE LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = #{lastMantUsr}) " +
		"AND NOT EXISTS (" +
			"SELECT 1 FROM EXAM_QUES_RLN RLN " +
			"WHERE RLN.EXAM_CD = #{examCd} AND QUES.QUES_CD = RLN.QUES_CD" +
		") " +
		"ORDER BY QUES_CD DESC"
	)
	@ResultMap("QuesList")
	Page<QuesByRespEntity> findAvaQues(ExamQuesRlnEntity entity);
	
	@Select(
		"SELECT TTL FROM EXAM WHERE EXAM_CD = #{examCd}"
	)
	String fillbackTtl(@Param("examCd") String examCd);
	
	@Select(
		"SELECT BGN_TIME FROM EXAM WHERE EXAM_CD = #{examCd}"
	)
	Date fillbackBgnTime(@Param("examCd") String examCd);
	
	@Select(
		"SELECT COUNT(1) " +
		"FROM EXAM " +
		"WHERE LAST_MANT_USR = #{lastMantUsr} AND BGN_TIME BETWEEN #{bgnTimeBgn, jdbcType = TIMESTAMP} AND #{bgnTimeEnd, jdbcType = TIMESTAMP}"
	)
	int findExamCntByIntv(ExamIntvEntity entity);
}
