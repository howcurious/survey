package cn.nbbandxdd.survey.common;

import java.util.HashSet;
import java.util.Set;

public interface ICommonConstDefine {

	public static final String COMMON_IND_YES = "1";
	public static final String COMMON_IND_NO = "0";
	public static final Set<String> COMMON_IND_SET = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(COMMON_IND_YES);
			add(COMMON_IND_NO);
		}
	};
	
	public static final String PUB_SER_NO_EXAM_EXAM_CD = "EXAM_EXAM_CD";
	public static final String PUB_SER_NO_QUES_QUES_CD = "QUES_QUES_CD";

	public static final String UNI_JOB_RESP_REC_SEND = "RESP_REC_SEND";

	public static final String EXAM_TYP_CD_DEFAULT = "0";
	public static final String EXAM_TYP_CD_DEFINITE = "1";
	public static final String EXAM_TYP_CD_RANDOM = "2";
	public static final Set<String> EXAM_TYP_CD_SET = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(EXAM_TYP_CD_DEFAULT);
			add(EXAM_TYP_CD_DEFINITE);
			add(EXAM_TYP_CD_RANDOM);
		}
	};
	
	public static final String EXAM_STATUS_DELETED = "notexists";
	public static final String EXAM_STATUS_NOTSTART = "notstart";
	public static final String EXAM_STATUS_FINISHED = "finished";
	public static final String EXAM_STATUS_COMPLETED_AND_REPEATABLE = "completed_and_repeatable";
	public static final String EXAM_STATUS_COMPLETED_AND_UNREPEATABLE = "completed_and_unrepeatable";
	public static final String EXAM_STATUS_TO_BE_COMPLETED = "to_be_completed";
	
	public static final String QUES_TYP_CD_SINGLE_CHOICE = "1";
	public static final String QUES_TYP_CD_MULTIPLE_CHOICE = "2";
	public static final Set<String> QUES_TYP_CD_SET = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(QUES_TYP_CD_SINGLE_CHOICE);
			add(QUES_TYP_CD_MULTIPLE_CHOICE);
		}
	};
	
	public static final String ANSW_TYP_CD_DEFAULT = "0";

	public static final String USER_EVERYONE = "everyone";
	
	public static final String SYS_ERROR_JWT_NOT_VALID_COD = "0001";
	public static final String SYS_ERROR_JWT_NOT_VALID_MSG = "无效的TOKEN。";
	public static final String SYS_ERROR_WECHAT_MSG_SEC_CHECK_COD = "1001";
	public static final String SYS_ERROR_WECHAT_MSG_SEC_CHECK_MSG = "内容含有违法违规内容。";
	public static final String SYS_ERROR_PARAM_NOT_VALID_COD = "2001";
	public static final String SYS_ERROR_PARAM_NOT_VALID_MSG = "参数校验失败。";
	public static final String SYS_ERROR_DEFAULT_COD = "9999";
	public static final String SYS_ERROR_DEFAULT_MSG = "系统错误。";
}
