package cn.nbbandxdd.survey.ques.service;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.exam.repository.ExamRepository;
import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import cn.nbbandxdd.survey.pubserno.generator.PubSerNoGenerator;
import cn.nbbandxdd.survey.ques.repository.AnswRepository;
import cn.nbbandxdd.survey.ques.repository.QuesRepository;
import cn.nbbandxdd.survey.ques.repository.entity.AnswEntity;
import cn.nbbandxdd.survey.ques.repository.entity.QuesEntity;
import cn.nbbandxdd.survey.resprec.repository.RespRecRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>题目Service。
 *
 * <ul>
 * <li>新增题目，对外服务接口，使用{@link #insert(Mono)}。</li>
 * <li>删除题目，对外服务接口，使用{@link #delete(Mono)}。</li>
 * <li>修改题目，对外服务接口，使用{@link #update(Mono)}。</li>
 * <li>查看题目详情（出题者视角），对外服务接口，使用{@link #findByPron(Mono)}。</li>
 * <li>查看题目详情（答题者视角），对外服务接口，使用{@link #findByResp(Mono)}。</li>
 * <li>查看题目详情（回看者视角），对外服务接口，使用{@link #findByExp(Mono, Integer, Integer)}。</li>
 * <li>查看题目列表，对外服务接口，使用{@link #findList(Integer, Integer)}。</li>
 * <li>查看公共题目列表，对外服务接口，使用{@link #findCommonList(Integer, Integer)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Service
public class QuesService {

    /**
     * <p>问卷Repository。
     */
    private final ExamRepository examRepository;

    /**
     * <p>题目Repository。
     */
    private final QuesRepository quesRepository;

    /**
     * <p>答案Repository。
     */
    private final AnswRepository answRepository;

    /**
     * <p>作答记录Repository。
     */
    private final RespRecRepository respRecRepository;

    /**
     * <p>构造器。
     *
     * @param examRepository 问卷Repository
     * @param quesRepository 题目Repository
     * @param answRepository 答案Repository
     * @param respRecRepository 作答记录Repository
     */
    public QuesService(ExamRepository examRepository, QuesRepository quesRepository, AnswRepository answRepository, RespRecRepository respRecRepository) {

        this.examRepository = examRepository;
        this.quesRepository = quesRepository;
        this.answRepository = answRepository;
        this.respRecRepository = respRecRepository;
    }

    /**
     * <p>新增题目，对外服务接口。
     *
     * @param entity 题目Entity，答案Entity集合
     * @return 题目Entity
     */
    @Transactional
    public Mono<QuesEntity> insert(Mono<Tuple2<QuesEntity, List<AnswEntity>>> entity) {

        return entity
            .flatMap(tup -> {

                if (!ICommonConstDefine.QUES_TYP_CD_SET.contains(tup.getT1().getTypCd())) {

                    return Mono.error(new SurveyValidationException("题目类型错误。"));
                }
                if (StringUtils.isBlank(tup.getT1().getDsc())) {

                    return Mono.error(new SurveyValidationException("请填写题目描述。"));
                }
                if ((StringUtils.equals(tup.getT1().getTypCd(), ICommonConstDefine.QUES_TYP_CD_SINGLE_CHOICE) || StringUtils.equals(tup.getT1().getTypCd(), ICommonConstDefine.QUES_TYP_CD_MULTIPLE_CHOICE)) &&
                    tup.getT2().isEmpty()) {

                    return Mono.error(new SurveyValidationException("选择题选项不能为空。"));
                }
                if (StringUtils.equals(tup.getT1().getTypCd(), ICommonConstDefine.QUES_TYP_CD_SHORT_ANSWER) && !tup.getT2().isEmpty()) {

                    return Mono.error(new SurveyValidationException("简答题选项只能为空。"));
                }

                return Mono.just(tup);
            })
            .flatMap(tup -> Mono.deferContextual(ctx -> {

                tup.getT1().setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(tup);
            }))
            .map(tup -> {

                LocalDateTime now = LocalDateTime.now();
                tup.getT1().setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                tup.getT1().setLastMantTmstp(now);

                tup.getT1().setNew(true);

                return tup;
            })
            .flatMap(tup -> PubSerNoGenerator
                .get(ICommonConstDefine.PUB_SER_NO_QUES_QUES_CD)
                .map(serNo -> {

                    tup.getT1().setQuesCd(tup.getT1().getLastMantDat() + serNo);
                    return tup;
                }))
            .flatMap(tup -> quesRepository.save(tup.getT1()).map(one -> tup))
            .map(tup -> {

                int idx = 0;
                for (AnswEntity en : tup.getT2()) {

                    en.setAnswCd(StringUtils.leftPad(String.valueOf(idx++), 2, '0'));
                }
                return tup;
            })
            .flatMap(tup -> Flux.fromIterable(tup.getT2())
                .flatMap(one -> {

                    if (StringUtils.isBlank(one.getDsc())) {

                        return Mono.error(new SurveyValidationException("请填写选项描述。"));
                    }
                    if (one.getScre() == null || one.getScre() < 0 || 100 < one.getScre()) {

                        return Mono.error(new SurveyValidationException("选项分值区间为[0,100]。"));
                    }

                    return Mono.just(one);
                })
                .flatMap(one -> Mono.deferContextual(ctx -> {

                    one.setQuesCd(tup.getT1().getQuesCd());
                    one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                    return Mono.just(one);
                }))
                .map(one -> {

                    LocalDateTime now = LocalDateTime.now();
                    one.setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    one.setLastMantTmstp(now);

                    return one;
                })
                .flatMap(answRepository::insert)
                .then(Mono.just(tup.getT1()))
            );
    }

    /**
     * <p>删除题目，对外服务接口。
     *
     * @param entity 题目Entity
     * @return 无
     */
    @Transactional
    public Mono<Void> delete(Mono<QuesEntity> entity) {

        return entity
            .flatMap(one -> {

                if (StringUtils.isBlank(one.getQuesCd())) {

                    return Mono.error(new SurveyValidationException("题目编号不能为空。"));
                }

                return Mono.just(one);
            })
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMap(one -> quesRepository.deleteByQuesCdAndLastMantUsr(one.getQuesCd(), one.getLastMantUsr()).map(cnt -> one))
            .flatMap(one -> answRepository.deleteByQuesCdAndLastMantUsr(one.getQuesCd(), one.getLastMantUsr()))
            .then();
    }

    /**
     * <p>修改题目，对外服务接口。
     *
     * @param entity 题目Entity，答案Entity集合
     * @return 题目Entity
     */
    @Transactional
    public Mono<Void> update(Mono<Tuple2<QuesEntity, List<AnswEntity>>> entity) {

        return entity
            .flatMap(tup -> {

                if (StringUtils.isBlank(tup.getT1().getQuesCd())) {

                    return Mono.error(new SurveyValidationException("题目编号不能为空。"));
                }
                if (!ICommonConstDefine.QUES_TYP_CD_SET.contains(tup.getT1().getTypCd())) {

                    return Mono.error(new SurveyValidationException("题目类型错误。"));
                }
                if (StringUtils.isBlank(tup.getT1().getDsc())) {

                    return Mono.error(new SurveyValidationException("请填写题目描述。"));
                }
                if ((StringUtils.equals(tup.getT1().getTypCd(), ICommonConstDefine.QUES_TYP_CD_SINGLE_CHOICE) || StringUtils.equals(tup.getT1().getTypCd(), ICommonConstDefine.QUES_TYP_CD_MULTIPLE_CHOICE)) &&
                    tup.getT2().isEmpty()) {

                    return Mono.error(new SurveyValidationException("选择题选项不能为空。"));
                }
                if (StringUtils.equals(tup.getT1().getTypCd(), ICommonConstDefine.QUES_TYP_CD_SHORT_ANSWER) && !tup.getT2().isEmpty()) {

                    return Mono.error(new SurveyValidationException("简答题选项只能为空。"));
                }

                return Mono.just(tup);
            })
            .flatMap(tup -> Mono.deferContextual(ctx -> {

                tup.getT1().setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(tup);
            }))
            .map(tup -> {

                LocalDateTime now = LocalDateTime.now();
                tup.getT1().setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                tup.getT1().setLastMantTmstp(now);

                return tup;
            })
            .flatMap(tup -> quesRepository.save(tup.getT1()).map(one -> tup))
            .flatMap(tup -> answRepository.deleteByQuesCdAndLastMantUsr(tup.getT1().getQuesCd(), tup.getT1().getLastMantUsr()).map(cnt -> tup))
            .map(tup -> {

                int idx = 0;
                for (AnswEntity en : tup.getT2()) {

                    en.setAnswCd(StringUtils.leftPad(String.valueOf(idx++), 2, '0'));
                }
                return tup;
            })
            .flatMap(tup -> Flux.fromIterable(tup.getT2())
                .flatMap(one -> {

                    if (StringUtils.isBlank(one.getDsc())) {

                        return Mono.error(new SurveyValidationException("请填写选项描述。"));
                    }
                    if (one.getScre() == null || one.getScre() < 0 || 100 < one.getScre()) {

                        return Mono.error(new SurveyValidationException("选项分值区间为[0,100]。"));
                    }

                    return Mono.just(one);
                })
                .flatMap(one -> Mono.deferContextual(ctx -> {

                    one.setQuesCd(tup.getT1().getQuesCd());
                    one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                    return Mono.just(one);
                }))
                .map(one -> {

                    LocalDateTime now = LocalDateTime.now();
                    one.setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    one.setLastMantTmstp(now);

                    return one;
                })
                .flatMap(answRepository::insert)
                .then()
            );
    }

    /**
     * <p>查看题目详情（出题者视角），对外服务接口。
     *
     * @param entity 题目Entity
     * @return 题目Entity，答案Entity列表
     */
    public Mono<Tuple2<QuesEntity, List<AnswEntity>>> findByPron(Mono<QuesEntity> entity) {

        return entity
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMap(one -> {

                Mono<QuesEntity> quesEntity = quesRepository.findByQuesCdAndLastMantUsr(one.getQuesCd(), one.getLastMantUsr());
                Mono<List<AnswEntity>> answList = answRepository.findByQuesCd(one.getQuesCd()).collectList();

                return Mono.zip(quesEntity, answList, Tuples::of);
            });
    }

    /**
     * <p>查看题目详情（答题者视角），对外服务接口。
     *
     * @param entity 题目Entity
     * @return 题目Entity，答案Entity列表
     */
    public Mono<Tuple2<QuesEntity, List<AnswEntity>>> findByResp(Mono<QuesEntity> entity) {

        return entity
            .flatMap(one -> {

                Mono<QuesEntity> quesEntity = quesRepository.findByQuesCd(one.getQuesCd());
                Mono<List<AnswEntity>> answList = answRepository.findByQuesCd(one.getQuesCd()).map(en -> {

                    en.setScre(0);
                    return en;
                }).collectList();

                return Mono.zip(quesEntity, answList, Tuples::of);
            });
    }

    /**
     * <p>查看题目详情（回看者视角），对外服务接口。
     *
     * @param entity 问卷Entity
     * @return 题目Entity，答案Entity列表，选择列表
     */
    public Flux<Tuple3<QuesEntity, List<AnswEntity>, List<String>>> findByExp(Mono<ExamEntity> entity, Integer pageNum, Integer pageSize) {

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
            .flatMap(one -> respRecRepository.findByOpenIdAndExamCd(one.getLastMantUsr(), one.getExamCd()).map(en -> one).switchIfEmpty(Mono.error(new SurveyValidationException("问卷尚未作答。"))))
            .flatMap(one -> examRepository.findById(one.getExamCd()).flatMap(en -> {

                    if (StringUtils.equals(en.getAnswImmInd(), ICommonConstDefine.COMMON_IND_NO) && LocalDateTime.now().isBefore(en.getEndTime())) {

                        return Mono.error(new SurveyValidationException("问卷答案尚不可见。"));
                    }
                    return Mono.just(one);
                }))
            .flatMapMany(one -> quesRepository.findByExamCd(one.getExamCd(), pageSize, (pageNum - 1) * pageSize)
                .flatMap(q -> {

                    Mono<List<AnswEntity>> answList = answRepository.findByQuesCd(q.getQuesCd()).collectList();
                    Mono<List<String>> selList = answRepository.findSelByOpenIdAndExamCdAndQuesCd(one.getLastMantUsr(), one.getExamCd(), q.getQuesCd()).collectList();

                    return Mono.zip(Mono.just(q), Mono.zip(answList, selList, Tuples::of), (e, z) -> Tuples.of(e, z.getT1(), z.getT2()));
                })
            );
    }

    /**
     * <p>查看题目列表，对外服务接口。
     *
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 题目Entity列表
     */
    public Flux<QuesEntity> findList(Integer pageNum, Integer pageSize) {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .flatMapMany(openId -> quesRepository.findByLastMantUsr(openId, pageSize, (pageNum - 1) * pageSize));
    }

    /**
     * <p>查看公共题目列表，对外服务接口。
     *
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 题目Entity列表
     */
    public Flux<QuesEntity> findCommonList(Integer pageNum, Integer pageSize) {

        return quesRepository.findByLastMantUsr(ICommonConstDefine.USER_EVERYONE, pageSize, (pageNum - 1) * pageSize);
    }
}
