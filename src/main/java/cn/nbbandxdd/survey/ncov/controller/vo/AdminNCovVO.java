package cn.nbbandxdd.survey.ncov.controller.vo;

import lombok.Data;

/**
 * B端请求的VO
 */
@Data
public class AdminNCovVO {
    private String openId;
    private String userName;
    private String departName;
    private String groupName;
    private String phoneNo;
    private String dist;
    private String addr;
}
