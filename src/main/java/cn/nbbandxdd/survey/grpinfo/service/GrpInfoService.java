package cn.nbbandxdd.survey.grpinfo.service;

import cn.nbbandxdd.survey.grpinfo.repository.GrpInfoRepository;
import cn.nbbandxdd.survey.grpinfo.repository.entity.GrpInfoEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * <p>分组信息Service。
 *
 * <ul>
 * <li>查询分组列表，对外服务接口，使用{@link #findGrpList(Flux)}。</li>
 * <li>查询部门列表，对外服务接口，使用{@link #findDprtList()}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Service
public class GrpInfoService {

    /**
     * <p>分组信息Repository。
     */
    private final GrpInfoRepository grpInfoRepository;

    /**
     * <p>构造器。
     *
     * @param grpInfoRepository 分组信息Repository
     */
    public GrpInfoService(GrpInfoRepository grpInfoRepository) {

        this.grpInfoRepository = grpInfoRepository;
    }

    /**
     * <p>查询分组列表，对外服务接口。
     *
     * @param entity 分组信息Entity
     * @return 查询记录
     */
    public Flux<GrpInfoEntity> findGrpList(Flux<GrpInfoEntity> entity) {

        return entity
            .filter(one -> StringUtils.isNotBlank(one.getDprtNam()))
            .flatMap(one -> grpInfoRepository.findByDprtNam(one.getDprtNam()));
    }

    /**
     * <p>查询部门列表，对外服务接口。
     *
     * @return 查询记录
     */
    public Flux<GrpInfoEntity> findDprtList() {

        return grpInfoRepository.findDistinctDprtNam();
    }
}
