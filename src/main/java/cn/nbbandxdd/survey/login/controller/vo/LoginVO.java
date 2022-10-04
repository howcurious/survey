package cn.nbbandxdd.survey.login.controller.vo;

import lombok.Data;

/**
 * <p>登录VO。
 *
 * @author howcurious
 */
@Data
public class LoginVO {

    /**
     * <p>临时登录凭证。
     */
    private String code;

    /**
     * <p>token。
     */
    private String token;

    /**
     * <p>已注册标识。
     */
    private Boolean isRegistered;

    /**
     * <p>B端用户名
     */
    private String adminUserName;

    /**
     * <p>B端用户密码
     */
    private String adminPassword;
}
