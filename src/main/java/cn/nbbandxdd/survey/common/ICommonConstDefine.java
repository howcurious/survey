package cn.nbbandxdd.survey.common;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>公共常量类。
 * 
 * @author howcurious
 */
public interface ICommonConstDefine {

	/**
	 * <p>公共标识：是。
	 */
	public static final String COMMON_IND_YES = "1";
	/**
	 * <p>公共标志：否。
	 */
	public static final String COMMON_IND_NO = "0";
	/**
	 * <p>公共标志集合。
	 */
	public static final Set<String> COMMON_IND_SET = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(COMMON_IND_YES);
			add(COMMON_IND_NO);
		}
	};
	
	/**
	 * <p>公共序列号类型：问卷。
	 */
	public static final String PUB_SER_NO_EXAM_EXAM_CD = "EXAM_EXAM_CD";
	/**
	 * <p>公共序列号类型：题目。
	 */
	public static final String PUB_SER_NO_QUES_QUES_CD = "QUES_QUES_CD";

	/**
	 * <p>唯一任务类型：作答记录报表发送任务。
	 */
	public static final String UNI_JOB_RESP_REC_SEND = "RESP_REC_SEND";

	/**
	 * <p>问卷类型：默认（同“手工选择题目”）。
	 */
	public static final String EXAM_TYP_CD_DEFAULT = "0";
	/**
	 * <p>问卷类型：手工选择题目。
	 */
	public static final String EXAM_TYP_CD_DEFINITE = "1";
	/**
	 * <p>问卷类型：随机生成题目。
	 */
	public static final String EXAM_TYP_CD_RANDOM = "2";
	/**
	 * <p>问卷类型集合。
	 */
	public static final Set<String> EXAM_TYP_CD_SET = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(EXAM_TYP_CD_DEFAULT);
			add(EXAM_TYP_CD_DEFINITE);
			add(EXAM_TYP_CD_RANDOM);
		}
	};

	/**
	 * <p>问卷状态：问卷不存在。
	 */
	public static final String EXAM_STATUS_DELETED = "notexists";
	/**
	 * <p>问卷状态：问卷未开始。
	 */
	public static final String EXAM_STATUS_NOTSTART = "notstart";
	/**
	 * <p>问卷状态：问卷已结束。
	 */
	public static final String EXAM_STATUS_FINISHED = "finished";
	/**
	 * <p>问卷状态：问卷已作答，可重复作答。
	 */
	public static final String EXAM_STATUS_COMPLETED_AND_REPEATABLE = "completed_and_repeatable";
	/**
	 * <p>问卷状态：问卷已作答，不可重复作答。
	 */
	public static final String EXAM_STATUS_COMPLETED_AND_UNREPEATABLE = "completed_and_unrepeatable";
	/**
	 * <p>问卷状态：问卷未作答。
	 */
	public static final String EXAM_STATUS_TO_BE_COMPLETED = "to_be_completed";
	
	/**
	 * <p>题目类型：单选。
	 */
	public static final String QUES_TYP_CD_SINGLE_CHOICE = "1";
	/**
	 * <p>题目类型：多选。
	 */
	public static final String QUES_TYP_CD_MULTIPLE_CHOICE = "2";
	/**
	 * <p>题目类型集合。
	 */
	public static final Set<String> QUES_TYP_CD_SET = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(QUES_TYP_CD_SINGLE_CHOICE);
			add(QUES_TYP_CD_MULTIPLE_CHOICE);
		}
	};
	
	/**
	 * <p>选项类型：默认。
	 */
	public static final String ANSW_TYP_CD_DEFAULT = "0";

	/**
	 * <p>所有人，可用于问卷、题目、选项等数据表的LAST_MANT_USR字段。
	 * 所有用户都可以访问被“标记”为everyone的记录。
	 */
	public static final String USER_EVERYONE = "everyone";
	
	/**
	 * <p>错误码：无效的TOKEN。用于Jwt校验失败相关情形。
	 */
	public static final String SYS_ERROR_JWT_NOT_VALID_COD = "0001";
	/**
	 * <p>错误信息：无效的TOKEN。用于Jwt校验失败相关情形。
	 */
	public static final String SYS_ERROR_JWT_NOT_VALID_MSG = "无效的TOKEN。";
	/**
	 * <p>错误码：内容含有违法违规内容。用于微信小程序内容安全校验失败相关情形。
	 */
	public static final String SYS_ERROR_WECHAT_MSG_SEC_CHECK_COD = "1001";
	/**
	 * <p>错误信息：内容含有违法违规内容。用于微信小程序内容安全校验失败相关情形。
	 */
	public static final String SYS_ERROR_WECHAT_MSG_SEC_CHECK_MSG = "内容含有违法违规内容。";
	/**
	 * <p>错误码：参数校验失败。用于请求报文参数校验失败相关情形。
	 */
	public static final String SYS_ERROR_PARAM_NOT_VALID_COD = "2001";
	/**
	 * <p>错误信息：参数校验失败。用于请求报文参数校验失败相关情形。
	 */
	public static final String SYS_ERROR_PARAM_NOT_VALID_MSG = "参数校验失败。";
	/**
	 * <p>错误码：系统错误。用于表示原因不明的错误，需要通过日志进行排查。
	 */
	public static final String SYS_ERROR_DEFAULT_COD = "9999";
	/**
	 * <p>错误信息：系统错误。用于表示原因不明的错误，需要通过日志进行排查。
	 */
	public static final String SYS_ERROR_DEFAULT_MSG = "系统错误。";
}
