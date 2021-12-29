package cn.nbbandxdd.survey.usrinfo.repository.entity;

import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <p>用户信息Entity。
 * 
 * @author howcurious
 */
@Data
@Table("USR_INFO")
public class UsrInfoEntity implements Persistable<String> {

    /**
     * <p>微信小程序中用户openId。
     */
    @Id
    @Column("OPEN_ID")
    private String openId;

    /**
     * <p>部门名。
     */
    @Column("DPRT_NAM")
    private String dprtNam;

    /**
     * <p>分组名。
     */
    @Column("GRP_NAM")
    private String grpNam;

    /**
     * <p>用户名。
     */
    @Column("USR_NAM")
    private String usrNam;

    /**
     * <p>注册时间戳。
     */
    @LastModifiedDate
    @Column("REG_TMSTP")
    private LocalDateTime regTmstp;

    /**
     * <p>新增或修改标识。如果为新增，则设置为{@literal true}，否则设置为{@literal false}。
     */
    @Transient
    private boolean isNew;

    /**
     * getId
     *
     * @return 微信小程序中用户openId {@code openId}
     */
    @Override
    public String getId() {

        return openId;
    }

    /**
     * isNew
     *
     * @return 新增或修改标识{@code isNew}
     */
    @Override
    public boolean isNew() {

        return isNew;
    }
}
