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

/**
 * <p>用户信息DAO。
 * 
 * <ul>
 * <li>新增记录，使用{@link #insert(UsrInfoEntity)}。</li>
 * <li>依据{@code openId}修改记录，使用{@link #update(UsrInfoEntity)}。</li>
 * <li>依据{@code openId}查询记录，使用{@link #loadByKey(UsrInfoEntity)}。</li>
 * <li>依据部门名{@code dprtNam}和职能组名{@code grpNam}统计人数，使用{@link #findUsrCntByGrp(String, String)}。</li>
 * <li>依据{@code openId}查询用户名，使用{@link #fillbackUsrNam(String)}。</li>
 * <li>依据{@code openId}查询职能组名，使用{@link #fillbackGrpNam(String)}。</li>
 * <li>依据{@code openId}查询部门名，使用{@link #fillbackDprtNam(String)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Mapper
public interface UsrInfoDAO {

	/**
	 * <p>新增记录。
	 * 
	 * @param entity 用户信息Entity
	 * @return 插入影响记录数
	 * @see UsrInfoEntity
	 */
	@Insert(
		"INSERT INTO " +
		"USR_INFO (OPEN_ID, DPRT_NAM, GRP_NAM, USR_NAM, REG_TMSTP) " +
		"VALUES (#{openId}, #{dprtNam}, #{grpNam}, #{usrNam}, #{regTmstp, jdbcType = TIMESTAMP});" +
		
		"INSERT INTO ROLE_INFO (OPEN_ID, ROLE_ID) VALUES (#{openId}, #{openId}), (#{openId}, 'everyone');"
	)
    int insert(UsrInfoEntity entity);
	
	/**
	 * <p>依据{@code openId}修改记录。
	 * 
	 * @param entity 用户信息Entity
	 * @return 更新匹配记录数
	 * @see UsrInfoEntity
	 */
	@Update(
		"UPDATE USR_INFO " +
		"SET DPRT_NAM = #{dprtNam}, GRP_NAM = #{grpNam}, USR_NAM = #{usrNam} " +
		"WHERE OPEN_ID = #{openId}"
	)
	int update(UsrInfoEntity entity);
	
	/**
	 * <p>依据{@code openId}查询记录。
	 * 
	 * @param entity 用户信息Entity
	 * @return UsrInfoEntity 用户信息Entity
	 * @see UsrInfoEntity
	 */
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
	
	/**
	 * <p>依据部门名{@code dprtNam}和职能组名{@code grpNam}统计人数。
	 * 
	 * @param dprtNam 部门名
	 * @param grpNam 职能组名
	 * @return 职能组人数
	 */
	@Select(
		"SELECT COUNT(1) " +
		"FROM USR_INFO " +
		"WHERE DPRT_NAM = #{dprtNam} AND GRP_NAM = #{grpNam}"
	)
	int findUsrCntByGrp(@Param("dprtNam") String dprtNam, @Param("grpNam") String grpNam);
	
	/**
	 * <p>依据{@code openId}查询用户名。
	 * 
	 * @param openId {@code openId}
	 * @return String 用户名
	 */
	@Select(
		"SELECT USR_NAM FROM USR_INFO WHERE OPEN_ID = #{openId}"
	)
	String fillbackUsrNam(@Param("openId") String openId);
	
	/**
	 * <p>依据{@code openId}查询职能组名。
	 * 
	 * @param openId {@code openId}
	 * @return String 职能组名
	 */
	@Select(
		"SELECT GRP_NAM FROM USR_INFO WHERE OPEN_ID = #{openId}"
	)
	String fillbackGrpNam(@Param("openId") String openId);
	
	/**
	 * <p>依据{@code openId}查询部门名。
	 * 
	 * @param openId {@code openId}
	 * @return String 部门名
	 */
	@Select(
		"SELECT DPRT_NAM FROM USR_INFO WHERE OPEN_ID = #{openId}"
	)
	String fillbackDprtNam(@Param("openId") String openId);
}
