package cn.nbbandxdd.survey.admin_user.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ADMIN_USR")
public class AdminUserInfoEntity {
    @Column("USR_NAME")
    private String userName;
    @Column("USR_PWD")
    private String userPwd;
}
