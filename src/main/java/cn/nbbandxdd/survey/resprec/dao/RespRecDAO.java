package cn.nbbandxdd.survey.resprec.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.github.pagehelper.Page;

import cn.nbbandxdd.survey.resprec.dao.entity.DtlRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecIntvEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecUsrRankEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecUsrStatEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecExamStatEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecGrpStatEntity;

@Mapper
public interface RespRecDAO {

	@Insert(
		"DELETE FROM DTL_REC WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd} AND QUES_CD = #{quesCd};" +
		"INSERT INTO " +
		"DTL_REC (OPEN_ID, EXAM_CD, QUES_CD, ANSW_CD, SCRE, LAST_MANT_TMSTP) " +
		"VALUES (#{openId}, #{examCd}, #{quesCd}, #{answCd}, #{scre}, #{lastMantTmstp});"
	)
    int insertDtl(DtlRecEntity entity);
	
	@Insert(
		"DELETE FROM RESP_REC WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd};" +
		"INSERT INTO " +
		"RESP_REC (OPEN_ID, EXAM_CD, SCRE, SPND, DAT) " +
		"VALUES (#{openId}, #{examCd}, #{scre}, #{spnd}, #{dat});"
	)
    int insertResp(RespRecEntity entity);
	
	@Select(
		"SELECT OPEN_ID, EXAM_CD, QUES_CD, ANSW_CD, SCRE, LAST_MANT_TMSTP " +
		"FROM DTL_REC " +
		"WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd} AND QUES_CD = #{quesCd}"
	)
	@Results(id = "DtlRec", value = {
		@Result(property = "openId", column = "OPEN_ID", javaType = String.class),
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "quesCd", column = "QUES_CD", javaType = String.class),
		@Result(property = "answCd", column = "ANSW_CD", javaType = String.class),
		@Result(property = "scre", column = "SCRE", javaType = Integer.class),
		@Result(property = "lastMantTmstp", column = "LAST_MANT_TMSTP", javaType = Timestamp.class)
	})
	DtlRecEntity loadDtlByKey(DtlRecEntity entity);
	
	@Select(
		"SELECT OPEN_ID, EXAM_CD, SCRE, SPND, DAT " +
		"FROM RESP_REC " +
		"WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd}"
	)
	@Results(id = "RespRec", value = {
		@Result(property = "openId", column = "OPEN_ID", javaType = String.class),
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "scre", column = "SCRE", javaType = Integer.class),
		@Result(property = "spnd", column = "SPND", javaType = Integer.class),
		@Result(property = "dat", column = "DAT", javaType = String.class),
		@Result(property = "ttl", column = "EXAM_CD", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.exam.dao.ExamDAO.fillbackTtl", fetchType = FetchType.LAZY))
	})
	RespRecEntity loadRespByKey(RespRecEntity entity);
	
	@Select(
		"SELECT ROUND(" +
			"(SELECT COUNT(1) FROM DTL_REC WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd} AND SCRE > 0)" +
			"/" +
			"(SELECT COUNT(1) FROM EXAM_QUES_RLN WHERE EXAM_CD = #{examCd}) * 100" +
		")"
	)
	int findScreForDefinite(RespRecEntity entity);
	
	@Select(
		"SELECT CASE WHEN SUM(RLN.SCRE) IS NULL THEN 0 ELSE SUM(RLN.SCRE) END " +
		"FROM (SELECT EXAM_CD, QUES_CD FROM DTL_REC WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd} AND SCRE > 0) REC " +
		"LEFT JOIN QUES ON QUES.QUES_CD = REC.QUES_CD " +
		"LEFT JOIN EXAM_QUES_TYP_RLN RLN ON RLN.EXAM_CD = REC.EXAM_CD AND RLN.QUES_TYP_CD = QUES.TYP_CD"
	)
	int findScreForRandom(RespRecEntity entity);
	
	@Select(
		"SELECT TIMESTAMPDIFF(SECOND, MIN(LAST_MANT_TMSTP), MAX(LAST_MANT_TMSTP)) FROM DTL_REC WHERE OPEN_ID = #{openId} AND EXAM_CD = #{examCd}"
	)
	int findRespSpnd(RespRecEntity entity);
	
	@Select(
		"SELECT ANSW_CD FROM ANSW " +
		"WHERE QUES_CD = #{quesCd} AND (" +
			"SELECT ANSW_CD FROM DTL_REC " +
			"WHERE OPEN_ID = '${@cn.nbbandxdd.survey.common.jwt.JwtHolder@get()}' AND EXAM_CD = #{examCd} and QUES_CD = #{quesCd}" +
		") LIKE CONCAT('%', ANSW_CD, '%')"
	)
	List<String> findSelAnsw(@Param("examCd") String examCd, @Param("quesCd") String quesCd);
	
	@Select(
		"(" +
			"SELECT OPEN_ID, EXAM_CD, SCRE, SPND, DAT " +
			"FROM RESP_REC REC " +
			"WHERE OPEN_ID = #{openId} AND EXISTS (" +
				"SELECT 1 FROM EXAM " +
				"WHERE EXAM.EXAM_CD = REC.EXAM_CD AND " +
					"EXAM.ANSW_IMM_IND = '1'" +
			") " +
	
			"UNION " +
	
			"SELECT OPEN_ID, EXAM_CD, SCRE, SPND, DAT " +
			"FROM RESP_REC REC " +
			"WHERE OPEN_ID = #{openId} AND EXISTS (" +
				"SELECT 1 FROM EXAM " +
				"WHERE EXAM.EXAM_CD = REC.EXAM_CD AND " +
					"EXAM.ANSW_IMM_IND = '0' AND EXAM.END_TIME < NOW()" +
			") " +
		")" +
		"ORDER BY DAT DESC, EXAM_CD DESC"
	)
	@ResultMap("RespRec")
	Page<RespRecEntity> findRespList(RespRecEntity entity);

	@Select(
		"SELECT OPEN_ID, EXAM_CD, SCRE, SPND, DAT " +
		"FROM RESP_REC REC " +
		"WHERE EXISTS (" +
			"SELECT 1 FROM EXAM " +
			"WHERE REC.EXAM_CD = EXAM.EXAM_CD " +
				"AND EXAM.EXAM_CD = #{examCd} " +
				"AND EXAM.LAST_MANT_USR = '${@cn.nbbandxdd.survey.common.jwt.JwtHolder@get()}'" +
		") " +
		"ORDER BY SCRE DESC, SPND ASC"
	)
	@Results(id = "UsrRank", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "scre", column = "SCRE", javaType = Integer.class),
		@Result(property = "spnd", column = "SPND", javaType = Integer.class),
		@Result(property = "dat", column = "DAT", javaType = String.class),
		@Result(property = "dprtNam", column = "OPEN_ID", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.fillbackDprtNam")),
		@Result(property = "grpNam", column = "OPEN_ID", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.fillbackGrpNam")),
		@Result(property = "usrNam", column = "OPEN_ID", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.fillbackUsrNam"))
	})
	Page<RespRecUsrRankEntity> findUsrRank(RespRecUsrRankEntity entity);
	
	@Select(
		"SELECT GRP.DPRT_NAM AS DPRT_NAM, GRP.GRP_NAM AS GRP_NAM, " +
			"(CASE WHEN RES.CNT IS NULL THEN 0 ELSE RES.CNT END) AS CNT, " +
			"(CASE WHEN RES.AVG_SCRE IS NULL THEN 0 ELSE RES.AVG_SCRE END) AS AVG_SCRE, " +
			"(CASE WHEN RES.AVG_SPND IS NULL THEN 0 ELSE RES.AVG_SPND END) AS AVG_SPND " +
		"FROM (SELECT * FROM GRP_INFO WHERE DPRT_NAM = #{dprtNam}) GRP " +
		"LEFT JOIN (" +
			"SELECT USR.DPRT_NAM AS DPRT_NAM, USR.GRP_NAM AS GRP_NAM, " +
				"COUNT(1) AS CNT, AVG(REC.SCRE) AS AVG_SCRE, AVG(REC.SPND) AS AVG_SPND " +
			"FROM (SELECT * FROM RESP_REC WHERE EXAM_CD = #{examCd}) REC " +
			"INNER JOIN (SELECT * FROM USR_INFO WHERE DPRT_NAM = #{dprtNam}) USR ON REC.OPEN_ID = USR.OPEN_ID " +
			"GROUP BY USR.DPRT_NAM, USR.GRP_NAM" +
		") RES ON GRP.DPRT_NAM = RES.DPRT_NAM AND GRP.GRP_NAM = RES.GRP_NAM " +
		"ORDER BY GRP.SEQ_NO"
	)
	@Results(id = "GrpStat", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "dprtNam", column = "DPRT_NAM", javaType = String.class),
		@Result(property = "grpNam", column = "GRP_NAM", javaType = String.class),
		@Result(property = "cnt", column = "CNT", javaType = Integer.class),
		@Result(property = "avgScre", column = "AVG_SCRE", javaType = Double.class),
		@Result(property = "avgSpnd", column = "AVG_SPND", javaType = Double.class),
		@Result(property = "totCnt", column = "{dprtNam = DPRT_NAM, grpNam = GRP_NAM}",
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.findUsrCntByGrp"))
	})
	List<RespRecGrpStatEntity> findGrpStat(RespRecGrpStatEntity entity);
	
	@Select(
		"SELECT GRP.DPRT_NAM AS DPRT_NAM, GRP.GRP_NAM AS GRP_NAM, " +
			"(CASE WHEN RES.CNT IS NULL THEN 0 ELSE RES.CNT END) AS CNT, " +
			"(CASE WHEN RES.AVG_SCRE IS NULL THEN 0 ELSE RES.AVG_SCRE END) AS AVG_SCRE, " +
			"(CASE WHEN RES.AVG_SPND IS NULL THEN 0 ELSE RES.AVG_SPND END) AS AVG_SPND " +
		"FROM (SELECT * FROM GRP_INFO WHERE DPRT_NAM = #{dprtNam}) GRP " +
		"LEFT JOIN (" +
			"SELECT USR.DPRT_NAM AS DPRT_NAM, USR.GRP_NAM AS GRP_NAM, " +
				"COUNT(1) AS CNT, AVG(REC.SCRE) AS AVG_SCRE, AVG(REC.SPND) AS AVG_SPND " +
			"FROM (" +
				"SELECT * FROM RESP_REC " +
				"WHERE EXISTS (" +
					"SELECT 1 FROM EXAM " +
					"WHERE RESP_REC.EXAM_CD = EXAM.EXAM_CD " +
					"AND LAST_MANT_USR = #{lastMantUsr} AND BGN_TIME BETWEEN #{bgnTimeBgn, jdbcType = TIMESTAMP} AND #{bgnTimeEnd, jdbcType = TIMESTAMP}" +
					") " +
				") REC " +
			"LEFT JOIN (SELECT * FROM USR_INFO WHERE DPRT_NAM = #{dprtNam}) USR ON REC.OPEN_ID = USR.OPEN_ID " +
			"GROUP BY USR.DPRT_NAM, USR.GRP_NAM" +
		") RES ON GRP.DPRT_NAM = RES.DPRT_NAM AND GRP.GRP_NAM = RES.GRP_NAM"
	)
	@ResultMap("GrpStat")
	List<RespRecGrpStatEntity> findIntvGrpStat(RespRecIntvEntity entity);
	
	@Select(
		"SELECT EXAM_CD, COUNT(1) AS CNT, AVG(SCRE) AS AVG_SCRE, AVG(SPND) AS AVG_SPND, " +
			"SUM(CASE WHEN SCRE BETWEEN 0 AND 40 THEN 1 ELSE 0 END) AS CNT_U40, " +
			"SUM(CASE WHEN SCRE BETWEEN 0 AND 40 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_U40, " +
			"SUM(CASE WHEN SCRE BETWEEN 41 AND 70 THEN 1 ELSE 0 END) AS CNT_U70, " +
			"SUM(CASE WHEN SCRE BETWEEN 41 AND 70 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_U70, " +
			"SUM(CASE WHEN SCRE BETWEEN 71 AND 100 THEN 1 ELSE 0 END) AS CNT_U100, " +
			"SUM(CASE WHEN SCRE BETWEEN 71 AND 100 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_U100, " +
			"SUM(CASE WHEN SCRE = 100 THEN 1 ELSE 0 END) AS CNT_100, " +
			"SUM(CASE WHEN SCRE = 100 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_100 " +
		"FROM RESP_REC " +
		"WHERE EXAM_CD = #{examCd}"
	)
	@Results(id = "ExamStat", value = {
		@Result(property = "examCd", column = "EXAM_CD", javaType = String.class),
		@Result(property = "avgScre", column = "AVG_SCRE", javaType = Double.class),
		@Result(property = "avgSpnd", column = "AVG_SPND", javaType = Double.class),
		@Result(property = "cnt", column = "CNT", javaType = Integer.class),
		@Result(property = "cntU40", column = "CNT_U40", javaType = Integer.class),
		@Result(property = "rateU40", column = "RATE_U40", javaType = Double.class),
		@Result(property = "cntU70", column = "CNT_U70", javaType = Integer.class),
		@Result(property = "rateU70", column = "RATE_U70", javaType = Double.class),
		@Result(property = "cntU100", column = "CNT_U100", javaType = Integer.class),
		@Result(property = "rateU100", column = "RATE_U100", javaType = Double.class),
		@Result(property = "cnt100", column = "CNT_100", javaType = Integer.class),
		@Result(property = "rate100", column = "RATE_100", javaType = Double.class),
		@Result(property = "ttl", column = "EXAM_CD",
			one = @One(select = "cn.nbbandxdd.survey.exam.dao.ExamDAO.fillbackTtl", fetchType = FetchType.LAZY)),
		@Result(property = "bgnTime", column = "EXAM_CD",
			one = @One(select = "cn.nbbandxdd.survey.exam.dao.ExamDAO.fillbackBgnTime", fetchType = FetchType.LAZY))
	})
	RespRecExamStatEntity findExamStat(RespRecExamStatEntity entity);
	
	@Select(
		"SELECT EXAM_CD, COUNT(1) AS CNT, AVG(SCRE) AS AVG_SCRE, AVG(SPND) AS AVG_SPND, " +
			"SUM(CASE WHEN SCRE BETWEEN 0 AND 40 THEN 1 ELSE 0 END) AS CNT_U40, " +
			"SUM(CASE WHEN SCRE BETWEEN 0 AND 40 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_U40, " +
			"SUM(CASE WHEN SCRE BETWEEN 41 AND 70 THEN 1 ELSE 0 END) AS CNT_U70, " +
			"SUM(CASE WHEN SCRE BETWEEN 41 AND 70 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_U70, " +
			"SUM(CASE WHEN SCRE BETWEEN 71 AND 100 THEN 1 ELSE 0 END) AS CNT_U100, " +
			"SUM(CASE WHEN SCRE BETWEEN 71 AND 100 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_U100, " +
			"SUM(CASE WHEN SCRE = 100 THEN 1 ELSE 0 END) AS CNT_100, " +
			"SUM(CASE WHEN SCRE = 100 THEN 1 ELSE 0 END) / COUNT(1) AS RATE_100 " +
		"FROM RESP_REC " +
		"WHERE EXISTS (" +
			"SELECT 1 FROM EXAM WHERE RESP_REC.EXAM_CD = EXAM.EXAM_CD " +
			"AND LAST_MANT_USR = #{lastMantUsr} AND BGN_TIME BETWEEN #{bgnTimeBgn, jdbcType = TIMESTAMP} AND #{bgnTimeEnd, jdbcType = TIMESTAMP}" +
		")" +
		"AND EXISTS (" +
			"SELECT 1 FROM USR_INFO WHERE RESP_REC.OPEN_ID = USR_INFO.OPEN_ID " +
			"AND DPRT_NAM = #{dprtNam}" +
		")" +
		"GROUP BY EXAM_CD"
	)
	@ResultMap("ExamStat")
	List<RespRecExamStatEntity> findIntvExamStat(RespRecIntvEntity entity);
	
	@Select(
		"SELECT OPEN_ID, COUNT(1) AS CNT, AVG(SCRE) AS AVG_SCRE " +
		"FROM (" +
			"SELECT * FROM RESP_REC " +
			"WHERE EXISTS (" +
				"SELECT 1 FROM EXAM " +
				"WHERE RESP_REC.EXAM_CD = EXAM.EXAM_CD " +
				"AND LAST_MANT_USR = #{lastMantUsr} AND BGN_TIME BETWEEN #{bgnTimeBgn, jdbcType = TIMESTAMP} AND #{bgnTimeEnd, jdbcType = TIMESTAMP}" +
			") " +
			"AND EXISTS (" +
				"SELECT 1 FROM USR_INFO WHERE RESP_REC.OPEN_ID = USR_INFO.OPEN_ID " +
				"AND DPRT_NAM = #{dprtNam}" +
			")" +
		") REC " +
		"GROUP BY OPEN_ID " +
		"ORDER BY CNT DESC, AVG_SCRE DESC " +
		"LIMIT 10"
	)
	@Results(id = "UsrStat", value = {
		@Result(property = "cnt", column = "CNT", javaType = Integer.class),
		@Result(property = "avgScre", column = "AVG_SCRE", javaType = Double.class),
		@Result(property = "dprtNam", column = "OPEN_ID", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.fillbackDprtNam")),
		@Result(property = "grpNam", column = "OPEN_ID", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.fillbackGrpNam")),
		@Result(property = "usrNam", column = "OPEN_ID", javaType = String.class,
			one = @One(select = "cn.nbbandxdd.survey.usrinfo.dao.UsrInfoDAO.fillbackUsrNam"))
	})
	List<RespRecUsrStatEntity> findIntvUsrStatByCnt(RespRecIntvEntity entity);
	
	@Select(
		"SELECT OPEN_ID, COUNT(1) AS CNT, AVG(SCRE) AS AVG_SCRE " +
		"FROM (" +
			"SELECT * FROM RESP_REC " +
			"WHERE EXISTS (" +
				"SELECT 1 FROM EXAM " +
				"WHERE RESP_REC.EXAM_CD = EXAM.EXAM_CD " +
				"AND LAST_MANT_USR = #{lastMantUsr} AND BGN_TIME BETWEEN #{bgnTimeBgn, jdbcType = TIMESTAMP} AND #{bgnTimeEnd, jdbcType = TIMESTAMP}" +
			")" +
			"AND EXISTS (" +
				"SELECT 1 FROM USR_INFO WHERE RESP_REC.OPEN_ID = USR_INFO.OPEN_ID " +
				"AND DPRT_NAM = #{dprtNam}" +
			")" +
		") REC " +
		"GROUP BY OPEN_ID " +
		"HAVING CNT >= #{othIntCond} " +
		"ORDER BY AVG_SCRE DESC " +
		"LIMIT 5"
	)
	@ResultMap("UsrStat")
	List<RespRecUsrStatEntity> findIntvUsrStatByAvgScre(RespRecIntvEntity entity);
}
