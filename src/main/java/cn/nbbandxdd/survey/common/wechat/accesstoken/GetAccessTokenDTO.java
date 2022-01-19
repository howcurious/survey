package cn.nbbandxdd.survey.common.wechat.accesstoken;

import lombok.Data;

/**
 * <p>接口调用凭证DTO。
 *
 * @author howcurious
 */
@Data
public class GetAccessTokenDTO {

    /**
     * <p>获取到的凭证。
     */
    private String access_token;

    /**
     * <p>凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    private String expires_in;

    /**
     * <p>错误码。
     */
    private String errcode;

    /**
     * <p>错误信息。
     */
    private String errmsg;
}
