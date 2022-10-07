package cn.nbbandxdd.survey.ncov.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("NCoV")
public class AdminNCoVEntity implements Persistable<String> {
    @Id
    @Column("OPEN_ID")
    private String openId;

    @Column("USR_NAM")
    private String userName;

    @Column("DPRT_NAM")
    private String departName;

    @Column("GRP_NAM")
    private String groupName;

    @Column("PHO_NUM")
    private String phoneNo;

    @Column("DIST")
    private String dist;

    @Column("ADDR")
    private String addr;

    @Column("LAST_MANT_TMSTP")
    private LocalDateTime lastMantTmstp;

    @Transient
    private boolean isNew;


    @Override
    public String getId() {
        return openId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
