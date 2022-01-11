package cn.nbbandxdd.survey.usrinfo.service;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.grpinfo.repository.GrpInfoRepository;
import cn.nbbandxdd.survey.usrinfo.repository.RoleInfoRepository;
import cn.nbbandxdd.survey.usrinfo.repository.UsrInfoRepository;
import cn.nbbandxdd.survey.usrinfo.repository.entity.UsrInfoEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * <p>用户信息Service。
 *
 * <ul>
 * <li>新增实名登记，对外服务接口，使用{@link #insert(Mono)}。</li>
 * <li>修改实名登记，对外服务接口，使用{@link #update(Mono)}。</li>
 * <li>查询实名登记，对外服务接口，使用{@link #loadByKey()}。</li>
 * <li>判断用户是否已实名登记，<strong>内部使用接口</strong>，使用{@link #existsByKey(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Service
public class UsrInfoService {

    /**
     * <p>用户信息Repository。
     */
    private final UsrInfoRepository usrInfoRepository;

    /**
     * <p>角色信息Repository。
     */
    private final RoleInfoRepository roleInfoRepository;

    /**
     * <p>分组信息Repository。
     */
    private final GrpInfoRepository grpInfoRepository;

    /**
     * <p>构造器。
     *
     * @param usrInfoRepository 用户信息Repository
     * @param roleInfoRepository 角色信息Repository
     * @param grpInfoRepository 分组信息Repository
     */
    public UsrInfoService(
        UsrInfoRepository usrInfoRepository, RoleInfoRepository roleInfoRepository, GrpInfoRepository grpInfoRepository) {

        this.usrInfoRepository = usrInfoRepository;
        this.roleInfoRepository = roleInfoRepository;
        this.grpInfoRepository = grpInfoRepository;
    }

    /**
     * <p>新增实名登记，对外服务接口。补齐{@code openId}和注册时间戳{@code regTmstp}。
     *
     * @param entity 用户信息Entity
     * @return 无
     */
    @Transactional
    public Mono<Void> insert(Mono<UsrInfoEntity> entity) {

        return entity
            .flatMap(one -> {

                if (StringUtils.isBlank(one.getDprtNam())) {

                    return Mono.error(new SurveyValidationException("请选择部门。"));
                }
                if (StringUtils.isBlank(one.getGrpNam())) {

                    return Mono.error(new SurveyValidationException("请选择分组。"));
                }

                return Mono.just(one);
            })
            .flatMap(one -> grpInfoRepository.findByDprtNamAndGrpNam(one.getDprtNam(), one.getGrpNam())
                .map(en -> one).switchIfEmpty(Mono.error(new SurveyValidationException("部门或分组错误。"))))
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setOpenId(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .map(one -> {

                one.setRegTmstp(LocalDateTime.now());
                one.setNew(true);
                return one;
            })
            .flatMap(usrInfoRepository::save)
            .flatMap(one -> roleInfoRepository.save(one.getOpenId(), ICommonConstDefine.USER_EVERYONE))
            .switchIfEmpty(Mono.error(
                new SurveyValidationException("新增实名登记校验失败。")))
            .then();
    }

    /**
     * <p>修改实名登记，对外服务接口。补齐{@code openId}和注册时间戳{@code regTmstp}。
     *
     * @param entity 用户信息Entity
     * @return 无
     */
    @Transactional
    public Mono<Void> update(Mono<UsrInfoEntity> entity) {

        return entity
            .flatMap(one -> {

                if (StringUtils.isBlank(one.getDprtNam())) {

                    return Mono.error(new SurveyValidationException("请选择部门。"));
                }
                if (StringUtils.isBlank(one.getGrpNam())) {

                    return Mono.error(new SurveyValidationException("请选择分组。"));
                }

                return Mono.just(one);
            })
            .flatMap(one -> grpInfoRepository.findByDprtNamAndGrpNam(one.getDprtNam(), one.getGrpNam())
                .map(en -> one).switchIfEmpty(Mono.error(new SurveyValidationException("部门或分组错误。"))))
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setOpenId(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .map(one -> {

                one.setRegTmstp(LocalDateTime.now());
                one.setNew(false);
                return one;
            })
            .flatMap(usrInfoRepository::save)
            .switchIfEmpty(Mono.error(
                new SurveyValidationException("修改实名登记校验失败。")))
            .then();
    }

    /**
     * <p>查询实名登记，对外服务接口。补齐{@code openId}。
     *
     * @return 用户信息Entity
     */
    public Mono<UsrInfoEntity> loadByKey() {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .flatMap(usrInfoRepository::findById);
    }

    /**
     * <p>判断用户是否已实名登记，<strong>内部使用接口</strong>。因不获取当前请求对应的openId，不能用于对外服务。
     *
     * @param openId openId
     * @return 用户是否已实名登记
     */
    public Mono<Boolean> existsByKey(String openId) {

        return usrInfoRepository.existsById(openId);
    }
}
