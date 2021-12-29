package cn.nbbandxdd.survey.common.wechat.code2session;

import lombok.Data;

/**
 * <p>登录凭证校验DTO。
 *
 * @author howcurious
 */
@Data
public class Code2SessionDTO {

    /**
     * <p>用户唯一标识。
     */
    private String openid;

    /**
     * <p>会话密钥。
     */
    private String session_key;

    /**
     * <p>错误码。
     */
    private String errcode;

    /**
     * <p>错误信息。
     */
    private String errmsg;
}
