package cn.nbbandxdd.survey.usrinfo.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <p>角色信息Entity。
 *
 * @author howcurious
 */
@Data
@Table("ROLE_INFO")
public class RoleInfoEntity {

    /**
     * <p>微信小程序中用户openId，与角色Id {@code roleId}组成组合主键。
     */
    @Column("OPEN_ID")
    private String openId;

    /**
     * <p>角色Id，与openId {@code openId}组成组合主键。
     */
    @Column("ROLE_ID")
    private String roleId;
}
