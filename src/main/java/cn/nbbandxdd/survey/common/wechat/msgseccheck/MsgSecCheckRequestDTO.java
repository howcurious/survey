package cn.nbbandxdd.survey.common.wechat.msgseccheck;

import lombok.Data;

/**
 * <p>检查一段文本是否含有违法违规内容请求DTO。
 *
 * @author howcurious
 */
@Data
public class MsgSecCheckRequestDTO {

    /**
     * <p>接口版本号，2.0版本为固定值2。
     */
    private Integer version;

    /**
     * <p>微信小程序中用户openId。
     */
    private String openid;

    /**
     * <p>场景枚举值（1 资料；2 评论；3 论坛；4 社交日志）。
     */
    private Integer scene;

    /**
     * <p>需检测的文本内容。
     */
    private String content;
}
