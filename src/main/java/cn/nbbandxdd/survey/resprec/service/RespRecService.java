package cn.nbbandxdd.survey.resprec.service;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.exam.repository.ExamRepository;
import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import cn.nbbandxdd.survey.ques.repository.AnswRepository;
import cn.nbbandxdd.survey.ques.repository.entity.AnswEntity;
import cn.nbbandxdd.survey.resprec.repository.DtlRecRepository;
import cn.nbbandxdd.survey.resprec.repository.RespRecRepository;
import cn.nbbandxdd.survey.resprec.repository.entity.DtlRecEntity;
import cn.nbbandxdd.survey.resprec.repository.entity.ExamStatEntity;
import cn.nbbandxdd.survey.resprec.repository.entity.GrpStatEntity;
import cn.nbbandxdd.survey.resprec.repository.entity.RespRecEntity;
import cn.nbbandxdd.survey.usrinfo.repository.UsrInfoRepository;
import cn.nbbandxdd.survey.usrinfo.repository.entity.UsrInfoEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>作答记录Service。
 *
 *
 *
 * @author howcurious
 */
@Service
public class RespRecService {

    /**
     * <p>问卷Repository。
     */
    private final ExamRepository examRepository;

    /**
     * <p>答案Repository。
     */
    private final AnswRepository answRepository;

    /**
     * <p>作答记录Repository。
     */
    private final RespRecRepository respRecRepository;

    /**
     * <p>作答明细Repository。
     */
    private final DtlRecRepository dtlRecRepository;

    /**
     * <p>用户信息Repository。
     */
    private final UsrInfoRepository usrInfoRepository;

    /**
     * <p>构造器。
     *
     * @param examRepository 问卷Repository
     * @param answRepository 答案Repository
     * @param respRecRepository 作答记录Repository
     * @param dtlRecRepository 作答明细Repository
     * @param usrInfoRepository 用户信息Repository
     */
    public RespRecService(ExamRepository examRepository, AnswRepository answRepository, RespRecRepository respRecRepository, DtlRecRepository dtlRecRepository, UsrInfoRepository usrInfoRepository) {

        this.examRepository = examRepository;
        this.answRepository = answRepository;
        this.respRecRepository = respRecRepository;
        this.dtlRecRepository = dtlRecRepository;
        this.usrInfoRepository = usrInfoRepository;
    }

    /**
     * <p>新增作答明细，对外服务接口。
     *
     * @param entity 作答明细Entity，最后一题标识，答案编号列表
     * @return 作答明细Entity，正确答案编号列表，问卷作答分数，问卷作答用时
     */
    @Transactional
    public Mono<Tuple4<DtlRecEntity, List<String>, Integer, Integer>> insertDtl(Mono<Tuple3<DtlRecEntity, String, List<String>>> entity) {

        return entity
            .flatMap(tup -> {

                if (StringUtils.isBlank(tup.getT1().getExamCd())) {

                    return Mono.error(new SurveyValidationException("问卷编号不能为空。"));
                }
                if (StringUtils.isBlank(tup.getT1().getQuesCd())) {

                    return Mono.error(new SurveyValidationException("题目编号不能为空。"));
                }
                if (!ICommonConstDefine.COMMON_IND_SET.contains(tup.getT2())) {

                    return Mono.error(new SurveyValidationException("最后一题标识格式错误。"));
                }
                if (tup.getT3().isEmpty()) {

                    return Mono.error(new SurveyValidationException("选项编号不能为空。"));
                }

                return Mono.just(tup);
            })
            .flatMap(tup -> examRepository.findById(tup.getT1().getExamCd()).flatMap(en -> {

                    if (LocalDateTime.now().isAfter(en.getEndTime())) {

                        return Mono.error(new SurveyValidationException("问卷已经结束。"));
                    }
                    if (LocalDateTime.now().isBefore(en.getBgnTime())) {

                        return Mono.error(new SurveyValidationException("问卷尚未开始。"));
                    }

                    return Mono.just(tup);
                }))
            .flatMap(tup -> Mono.deferContextual(ctx -> {

                tup.getT1().setOpenId(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(tup);
            }))
            .flatMap(tup -> {

                Mono<List<AnswEntity>> answList = answRepository.findByQuesCd(tup.getT1().getQuesCd()).filter(en -> en.getScre() != null && 0 < en.getScre()).collectList();
                return Mono.zip(Mono.just(tup), answList, (t, a) -> Tuples.of(t.getT1(), t.getT2(), t.getT3(), a));
            }).map(tup -> {

                tup.getT1().setAnswCd(String.join(",", tup.getT3()));
                tup.getT1().setLastMantTmstp(LocalDateTime.now());

                List<String> s = tup.getT3();
                List<String> a = tup.getT4().stream().map(AnswEntity::getAnswCd).collect(Collectors.toList());
                Collections.sort(s);
                Collections.sort(a);

                int scre = tup.getT4().stream().mapToInt(AnswEntity::getScre).max().orElse(0);
                if (s.size() != a.size()) {

                    scre = 0;
                } else {

                    for (int i = 0; i < s.size(); ++i) {

                        if (!s.get(i).equals(a.get(i))) {

                            scre = 0;
                            break;
                        }
                    }
                }
                tup.getT1().setScre(scre);

                return tup;
            })
            .flatMap(tup -> dtlRecRepository.deleteByOpenIdAndExamCdAndQuesCd(tup.getT1().getOpenId(), tup.getT1().getExamCd(), tup.getT1().getQuesCd()).map(cnt -> tup))
            .flatMap(tup -> dtlRecRepository.insert(tup.getT1()).map(cnt -> tup))
            .flatMap(tup -> {

                if (StringUtils.equals(tup.getT2(), ICommonConstDefine.COMMON_IND_NO)) {

                    return Mono.just(Tuples.of(tup.getT1(), tup.getT3(), 0, 0));
                }

                Mono<RespRecEntity> respRecEntity = Mono.just(new RespRecEntity())
                    .map(en -> {

                        en.setOpenId(tup.getT1().getOpenId());
                        en.setExamCd(tup.getT1().getExamCd());
                        en.setDat(tup.getT1().getLastMantTmstp().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

                        return en;
                    });

                Mono<Integer> respScre = examRepository.findById(tup.getT1().getExamCd()).flatMap(en -> {

                    if (StringUtils.equals(en.getTypCd(), ICommonConstDefine.EXAM_TYP_CD_DEFAULT) ||
                        StringUtils.equals(en.getTypCd(), ICommonConstDefine.EXAM_TYP_CD_DEFINITE)) {

                        return dtlRecRepository.findScreByOpenIdAndExamCdForDefinite(tup.getT1().getOpenId(), tup.getT1().getExamCd());
                    } else {

                        return dtlRecRepository.findScreByOpenIdAndExamCdForRandom(tup.getT1().getOpenId(), tup.getT1().getExamCd());
                    }
                });

                Mono<Integer> respSpnd = dtlRecRepository.findSpndByOpenIdAndExamCd(tup.getT1().getOpenId(), tup.getT1().getExamCd());

                return Mono.zip(Mono.zip(respRecEntity, respScre, (e, scre) -> {

                            e.setScre(scre);
                            return e;
                        }), respSpnd, (e, spnd) -> {

                            e.setSpnd(spnd);
                            return e;
                        })
                    .flatMap(en -> respRecRepository.deleteByOpenIdAndExamCd(en.getOpenId(), en.getExamCd()).map(cnt -> en))
                    .flatMap(en -> respRecRepository.insert(en).map(cnt -> en))
                    .map(en -> Tuples.of(tup.getT1(), tup.getT3(), en.getScre(), en.getSpnd()));
            });
    }

    /**
     * <p>查询作答记录，对外服务接口。
     *
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 作答记录Entity，问卷标题
     */
    public Flux<Tuple2<RespRecEntity, String>> findRespList(Integer pageNum, Integer pageSize) {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .flatMapMany(openId -> respRecRepository.findListByOpenId(openId, pageSize, (pageNum - 1) * pageSize))
            .flatMap(one -> {

                Mono<String> ttl = examRepository.findById(one.getExamCd()).map(ExamEntity::getTtl);
                return Mono.zip(Mono.just(one), ttl, Tuples::of);
            });
    }

    /**
     * <p>查询作答排名，对外服务接口。
     *
     * @param entity 问卷Entity
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 作答记录Entity，部门名，分组名，用户名
     */
    public Flux<Tuple4<RespRecEntity, String, String, String>> findUsrRank(Mono<ExamEntity> entity, Integer pageNum, Integer pageSize) {

        return entity
            .flatMap(one -> {

                if (StringUtils.isBlank(one.getExamCd())) {

                    return Mono.error(new SurveyValidationException("问卷编号不能为空。"));
                }

                return Mono.just(one);
            })
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMapMany(one -> respRecRepository.findRankByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr(), pageSize, (pageNum - 1) * pageSize))
            .flatMap(one -> {

                Mono<UsrInfoEntity> usrInfo = usrInfoRepository.findById(one.getOpenId());
                return Mono.zip(Mono.just(one), usrInfo, (e, u) -> Tuples.of(e, u.getDprtNam(), u.getGrpNam(), u.getUsrNam()));
            });
    }

    /**
     * <p>查询作答统计（按分组），对外服务接口。
     *
     * @param entity 按分组作答统计Entity
     * @return 按分组作答统计Entity列表
     */
    public Flux<GrpStatEntity> findGrpStat(Mono<GrpStatEntity> entity) {

        return entity
            .flatMap(one -> {

                if (StringUtils.isBlank(one.getExamCd())) {

                    return Mono.error(new SurveyValidationException("问卷编号不能为空。"));
                }

                return Mono.just(one);
            })
            .flatMap(one -> {

                if (StringUtils.isNotBlank(one.getDprtNam())) {

                    return Mono.just(one);
                }

                Mono<String> dprtNam = Mono
                    .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
                    .flatMap(usrInfoRepository::findById)
                    .map(UsrInfoEntity::getDprtNam);

                return Mono.zip(Mono.just(one), dprtNam, (e, d) -> {

                    e.setDprtNam(d);
                    return e;
                });
            })
            .flatMapMany(one -> respRecRepository.findGrpStatByExamCdAndDprtNam(one.getExamCd(), one.getDprtNam()));
    }

    /**
     * <p>查询作答统计（按问卷），对外服务接口。
     *
     * @param entity 按问卷作答统计Entity
     * @return 按问卷作答统计Entity
     */
    public Mono<ExamStatEntity> findExamStat(Mono<ExamStatEntity> entity) {

        return entity
            .flatMap(one -> {

                if (StringUtils.isBlank(one.getExamCd())) {

                    return Mono.error(new SurveyValidationException("问卷编号不能为空。"));
                }

                return Mono.just(one);
            })
            .flatMap(one -> respRecRepository.findExamStatByExamCd(one.getExamCd()));
    }
}
