package cn.nbbandxdd.survey.exam.service;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.exam.repository.ExamQuesRlnRepository;
import cn.nbbandxdd.survey.exam.repository.ExamQuesTypRlnRepository;
import cn.nbbandxdd.survey.exam.repository.ExamRepository;
import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import cn.nbbandxdd.survey.exam.repository.entity.ExamQuesRlnEntity;
import cn.nbbandxdd.survey.exam.repository.entity.ExamQuesTypRlnEntity;
import cn.nbbandxdd.survey.pubserno.generator.PubSerNoGenerator;
import cn.nbbandxdd.survey.ques.repository.QuesRepository;
import cn.nbbandxdd.survey.ques.repository.entity.QuesEntity;
import cn.nbbandxdd.survey.resprec.repository.DtlRecRepository;
import cn.nbbandxdd.survey.resprec.repository.RespRecRepository;
import cn.nbbandxdd.survey.resprec.repository.entity.RespRecEntity;
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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>问卷Service。
 *
 * <ul>
 * <li>新增问卷，对外服务接口，使用{@link #insert(Mono)}。</li>
 * <li>删除问卷，对外服务接口，使用{@link #delete(Mono)}。</li>
 * <li>修改问卷，对外服务接口，使用{@link #update(Mono)}。</li>
 * <li>查看问卷状态，对外服务接口，使用{@link #findStatus(Mono)}。</li>
 * <li>查看问卷详情，对外服务接口，使用{@link #findDetail(Mono)}。</li>
 * <li>查看问卷待作答题目，对外服务接口，使用{@link #findToAnsw(Mono)}。</li>
 * <li>查看问卷题目列表，对外服务接口，使用{@link #findQuesList(Mono, Integer, Integer)}。</li>
 * <li>查看问卷列表，对外服务接口，使用{@link #findList(Integer, Integer)}。</li>
 * <li>查看公共问卷列表，对外服务接口，使用{@link #findCommonList(Integer, Integer)}。</li>
 * <li>问卷中新增题目，对外服务接口，使用{@link #insertQues(Mono)}。</li>
 * <li>问卷中删除题目，对外服务接口，使用{@link #deleteQues(Mono)}。</li>
 * <li>查看问卷中未添加过的题目，对外服务接口，使用{@link #findAvaQues(Mono, Integer, Integer)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Service
public class ExamService {

    /**
     * <p>问卷Repository。
     */
    private final ExamRepository examRepository;

    /**
     * <p>题目Repository。
     */
    private final QuesRepository quesRepository;

    /**
     * <p>问卷与题目间关系Repository。
     */
    private final ExamQuesRlnRepository examQuesRlnRepository;

    /**
     * <p>问卷与题目类型间关系Repository。
     */
    private final ExamQuesTypRlnRepository examQuesTypRlnRepository;

    /**
     * <p>作答记录Repository。
     */
    private final RespRecRepository respRecRepository;

    /**
     * <p>作答明细Repository。
     */
    private final DtlRecRepository dtlRecRepository;

    /**
     * <p>构造器。
     *
     * @param examRepository 问卷Repository
     * @param quesRepository 题目Repository
     * @param examQuesRlnRepository 问卷与题目间关系Repository
     * @param examQuesTypRlnRepository 问卷与题目类型间关系Repository
     * @param respRecRepository 作答记录Repository
     * @param dtlRecRepository 作答明细Repository
     */
    public ExamService(ExamRepository examRepository, QuesRepository quesRepository, ExamQuesRlnRepository examQuesRlnRepository, ExamQuesTypRlnRepository examQuesTypRlnRepository, RespRecRepository respRecRepository, DtlRecRepository dtlRecRepository) {

        this.examRepository = examRepository;
        this.quesRepository = quesRepository;
        this.examQuesRlnRepository = examQuesRlnRepository;
        this.examQuesTypRlnRepository = examQuesTypRlnRepository;
        this.respRecRepository = respRecRepository;
        this.dtlRecRepository = dtlRecRepository;
    }

    /**
     * <p>新增问卷，对外服务接口。
     *
     * @param entity 问卷Entity，问卷与题目类型间关系Entity集合
     * @return 问卷Entity
     */
    @Transactional
    public Mono<ExamEntity> insert(Mono<Tuple2<ExamEntity, List<ExamQuesTypRlnEntity>>> entity) {

        return entity
            .filter(tup -> (StringUtils.isBlank(tup.getT1().getTypCd()) || ICommonConstDefine.EXAM_TYP_CD_SET.contains(tup.getT1().getTypCd())) &&
                ICommonConstDefine.COMMON_IND_SET.contains(tup.getT1().getRpetInd()) &&
                (StringUtils.isBlank(tup.getT1().getCntdwnInd()) || ICommonConstDefine.COMMON_IND_SET.contains(tup.getT1().getCntdwnInd())) &&
                (StringUtils.isBlank(tup.getT1().getAnswImmInd()) || ICommonConstDefine.COMMON_IND_SET.contains(tup.getT1().getAnswImmInd())) &&
                StringUtils.isNotBlank(tup.getT1().getTtl()))
            .flatMap(tup -> Mono.deferContextual(ctx -> {

                tup.getT1().setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(tup);
            }))
            .map(tup -> {

                if (StringUtils.isBlank(tup.getT1().getTypCd())) {

                    tup.getT1().setTypCd(ICommonConstDefine.EXAM_TYP_CD_DEFAULT);
                }
                if (StringUtils.isBlank(tup.getT1().getCntdwnInd())) {

                    tup.getT1().setCntdwnInd(ICommonConstDefine.COMMON_IND_YES);
                }
                if (StringUtils.isBlank(tup.getT1().getAnswImmInd())) {

                    tup.getT1().setAnswImmInd(ICommonConstDefine.COMMON_IND_YES);
                }

                LocalDateTime now = LocalDateTime.now();
                if (null == tup.getT1().getBgnTime()) {

                    tup.getT1().setBgnTime(now);
                }
                if (null == tup.getT1().getEndTime()) {

                    tup.getT1().setEndTime(now.plusDays(1));
                }
                tup.getT1().setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                tup.getT1().setLastMantTmstp(now);

                tup.getT1().setSeqNo(StringUtils.equals(ICommonConstDefine.EXAM_TYP_CD_RANDOM, tup.getT1().getTypCd()) ? -1 : 0);

                tup.getT1().setNew(true);

                return tup;
            })
            .flatMap(tup -> PubSerNoGenerator
                .get(ICommonConstDefine.PUB_SER_NO_EXAM_EXAM_CD)
                .map(serNo -> {

                    tup.getT1().setExamCd(tup.getT1().getLastMantDat() + serNo);
                    return tup;
                }))
            .flatMap(tup -> examRepository.save(tup.getT1()).map(one -> tup))
            .switchIfEmpty(Mono.error(new SurveyValidationException("新增问卷校验失败。")))
            .flatMap(tup -> Flux.fromIterable(tup.getT2())
                .filter(one -> ICommonConstDefine.QUES_TYP_CD_SET.contains(one.getQuesTypCd()))
                .flatMap(one -> Mono.deferContextual(ctx -> {

                    one.setExamCd(tup.getT1().getExamCd());
                    one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                    return Mono.just(one);
                }))
                .map(one -> {

                    one.setScre(Math.min(null == one.getScre() ? 0 : one.getScre(), 100));
                    one.setCnt(Math.min(null == one.getCnt() ? 0 : one.getCnt(), 100));

                    LocalDateTime now = LocalDateTime.now();
                    one.setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    one.setLastMantTmstp(now);

                    return one;
                })
                .flatMap(one -> examQuesTypRlnRepository.insert(one).map(cnt -> one))
                .flatMap(one -> Flux
                    .zip(
                        quesRepository.findRandomByLastMantUsrAndTypCd(one.getLastMantUsr(), one.getQuesTypCd(), one.getCnt()),
                        Flux.range(0, one.getCnt()),
                        (q, s) -> {

                            ExamQuesRlnEntity enRln = new ExamQuesRlnEntity();
                            enRln.setExamCd(tup.getT1().getExamCd());
                            enRln.setQuesCd(q.getQuesCd());
                            enRln.setSeqNo(s + (StringUtils.equals(ICommonConstDefine.QUES_TYP_CD_SINGLE_CHOICE, one.getQuesTypCd()) ? 0 : 100));
                            enRln.setLastMantUsr(tup.getT1().getLastMantUsr());
                            enRln.setLastMantDat(tup.getT1().getLastMantDat());
                            enRln.setLastMantTmstp(tup.getT1().getLastMantTmstp());

                            return enRln;
                        })
                    .flatMap(examQuesRlnRepository::insert)
                )
                .then(Mono.just(tup.getT1()))
            );
    }

    /**
     * <p>删除问卷，对外服务接口。
     *
     * @param entity 问卷Entity
     * @return 无
     */
    @Transactional
    public Mono<Void> delete(Mono<ExamEntity> entity) {

        return entity
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMap(one -> examRepository.deleteByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr()).map(cnt -> one))
            .flatMap(one -> examQuesRlnRepository.deleteByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr()).map(cnt -> one))
            .flatMap(one -> examQuesTypRlnRepository.deleteByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr()).map(cnt -> one))
            .flatMap(one -> respRecRepository.deleteByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr()).map(cnt -> one))
            .flatMap(one -> dtlRecRepository.deleteByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr()))
            .then();
    }

    /**
     * <p>修改问卷，对外服务接口。
     *
     * @param entity 问卷Entity
     * @return 无
     */
    @Transactional
    public Mono<Void> update(Mono<ExamEntity> entity) {

        return entity
            .filter(one -> StringUtils.isNotBlank(one.getExamCd()) &&
                ICommonConstDefine.COMMON_IND_SET.contains(one.getRpetInd()) &&
                (StringUtils.isBlank(one.getCntdwnInd()) || ICommonConstDefine.COMMON_IND_SET.contains(one.getCntdwnInd())) &&
                (StringUtils.isBlank(one.getAnswImmInd()) || ICommonConstDefine.COMMON_IND_SET.contains(one.getAnswImmInd())) &&
                StringUtils.isNotBlank(one.getTtl()))
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMap(one -> {

                Mono<ExamEntity> ori = examRepository.findByExamCdAndLastMantUsrForUpdate(one.getExamCd(), one.getLastMantUsr());
                return Mono.zip(ori, Mono.just(one), (o, c) -> {

                    if (StringUtils.isNotBlank(c.getRpetInd())) {

                        o.setRpetInd(c.getRpetInd());
                    }
                    if (StringUtils.isNotBlank(c.getCntdwnInd())) {

                        o.setCntdwnInd(c.getCntdwnInd());
                    }
                    if (StringUtils.isNotBlank(c.getAnswImmInd())) {

                        o.setCntdwnInd(c.getAnswImmInd());
                    }
                    if (StringUtils.isNotBlank(c.getTtl())) {

                        o.setTtl(c.getTtl());
                    }
                    o.setDsc(c.getDsc());
                    if (c.getBgnTime() != null) {

                        o.setBgnTime(c.getBgnTime());
                    }
                    if (c.getEndTime() != null) {

                        o.setEndTime(c.getEndTime());
                    }

                    LocalDateTime now = LocalDateTime.now();
                    o.setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    o.setLastMantTmstp(now);

                    return o;
                });
            })
            .flatMap(examRepository::save)
            .then();
    }

    /**
     * <p>查看问卷状态，对外服务接口。
     *
     * @param entity 问卷Entity
     * @return 问卷编号，问卷状态
     */
    public Mono<Tuple2<String, String>> findStatus(Mono<ExamEntity> entity) {

        return entity
            .flatMap(one -> {

                Mono<ExamEntity> examEntity = examRepository.findById(one.getExamCd())
                    .defaultIfEmpty(new ExamEntity());

                Mono<RespRecEntity> respRecEntity = Mono
                    .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
                    .flatMap(openId -> respRecRepository.findByOpenIdAndExamCd(openId, one.getExamCd()))
                    .defaultIfEmpty(new RespRecEntity());

                return Mono.zip(examEntity, respRecEntity, (e, r) -> {

                    LocalDateTime now = LocalDateTime.now();

                    if (StringUtils.isBlank(e.getExamCd())) {

                        return Tuples.of(one.getExamCd(), ICommonConstDefine.EXAM_STATUS_DELETED);
                    } else if (now.isBefore(e.getBgnTime())) {

                        return Tuples.of(one.getExamCd(), ICommonConstDefine.EXAM_STATUS_NOTSTART);
                    } else if (now.isAfter(e.getEndTime())) {

                        return Tuples.of(one.getExamCd(), ICommonConstDefine.EXAM_STATUS_FINISHED);
                    } else if (StringUtils.equals(r.getExamCd(), e.getExamCd())) {

                        if (StringUtils.equals(e.getRpetInd(), ICommonConstDefine.COMMON_IND_YES)) {

                            return Tuples.of(one.getExamCd(), ICommonConstDefine.EXAM_STATUS_COMPLETED_AND_REPEATABLE);
                        } else {

                            return Tuples.of(one.getExamCd(), ICommonConstDefine.EXAM_STATUS_COMPLETED_AND_UNREPEATABLE);
                        }
                    }

                    return Tuples.of(one.getExamCd(), ICommonConstDefine.EXAM_STATUS_TO_BE_COMPLETED);
                });
            });
    }

    /**
     * <p>查看问卷详情，对外服务接口。
     *
     * @param entity 问卷Entity
     * @return 问卷Entity，问卷题目数量，问卷与题目类型间关系Entity列表
     */
    public Mono<Tuple3<ExamEntity, Integer, List<ExamQuesTypRlnEntity>>> findDetail(Mono<ExamEntity> entity) {

        return entity
            .flatMap(one -> {

                Mono<ExamEntity> examEntity = examRepository.findById(one.getExamCd());
                Mono<Integer> quesCnt = examQuesRlnRepository.findQuesCntByExamCd(one.getExamCd());
                Mono<List<ExamQuesTypRlnEntity>> quesTypRlnList = examQuesTypRlnRepository.findByExamCd(one.getExamCd()).collectList();

                return Mono.zip(
                    examEntity, Mono.zip(quesCnt, quesTypRlnList, Tuples::of),
                    (e, z) -> Tuples.of(e, z.getT1(), z.getT2())
                );
            });
    }

    /**
     * <p>查看问卷待作答题目，对外服务接口。
     *
     * @param entity 问卷Entity
     * @return 问卷Entity，题目Entity列表
     */
    public Mono<Tuple2<ExamEntity, List<QuesEntity>>> findToAnsw(Mono<ExamEntity> entity) {

        return entity
            .flatMap(one -> {

                Mono<ExamEntity> examEntity = examRepository.findById(one.getExamCd());
                Mono<List<QuesEntity>> quesList = Mono.deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
                    .flatMap(openId -> quesRepository.findToAnswByExamCdAndOpenId(one.getExamCd(), openId).switchIfEmpty(quesRepository.findByExamCd(one.getExamCd(), Integer.MAX_VALUE, 0)).collectList());

                return Mono.zip(examEntity, quesList, Tuples::of);
            });
    }

    /**
     * <p>查看问卷题目列表，对外服务接口。
     *
     * @param entity 问卷Entity
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 题目Entity列表
     */
    public Flux<QuesEntity> findQuesList(Mono<ExamEntity> entity, Integer pageNum, Integer pageSize) {

        return entity
            .flatMapMany(one -> quesRepository.findByExamCd(one.getExamCd(), pageSize, (pageNum - 1) * pageSize));
    }

    /**
     * <p>查看问卷列表，对外服务接口。
     *
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 问卷Entity列表
     */
    public Flux<ExamEntity> findList(Integer pageNum, Integer pageSize) {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .flatMapMany(openId -> examRepository.findByLastMantUsr(openId, pageSize, (pageNum - 1) * pageSize));
    }

    /**
     * <p>查看公共问卷列表，对外服务接口。
     *
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 问卷Entity列表
     */
    public Flux<ExamEntity> findCommonList(Integer pageNum, Integer pageSize) {

        return examRepository.findByLastMantUsr(ICommonConstDefine.USER_EVERYONE, pageSize, (pageNum - 1) * pageSize);
    }

    /**
     * <p>问卷中新增题目，对外服务接口。
     *
     * @param entity 问卷编号，题目编号列表
     * @return 无
     */
    @Transactional
    public Mono<Void> insertQues(Mono<Tuple2<String, List<String>>> entity) {

        return entity
            .map(tup -> {

                ExamEntity exam = new ExamEntity();
                exam.setExamCd(tup.getT1());
                exam.setSeqNo(tup.getT2().size());

                int seqNo = 0;
                List<ExamQuesRlnEntity> lisExamQuesRln = new ArrayList<>();
                for (String quesCd : tup.getT2()) {

                    ExamQuesRlnEntity en = new ExamQuesRlnEntity();

                    en.setExamCd(tup.getT1());
                    en.setQuesCd(quesCd);
                    en.setSeqNo(seqNo++);

                    LocalDateTime now = LocalDateTime.now();
                    en.setLastMantDat(now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    en.setLastMantTmstp(now);

                    lisExamQuesRln.add(en);
                }

                return Tuples.of(exam, lisExamQuesRln);
            })
            .flatMap(tup -> Mono.deferContextual(ctx -> {

                String openId = ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID);
                tup.getT1().setLastMantUsr(openId);
                for (ExamQuesRlnEntity en : tup.getT2()) {

                    en.setLastMantUsr(openId);
                }

                return Mono.just(tup);
            }))
            .flatMap(tup -> examRepository.updateSeqNoByExamCdAndLastMantUsr(tup.getT1().getExamCd(), tup.getT1().getLastMantUsr(), tup.getT1().getSeqNo()).map(cnt -> tup))
            .flatMapMany(tup -> Flux.fromIterable(tup.getT2()))
            .flatMap(examQuesRlnRepository::insert)
            .then();
    }

    /**
     * <p>问卷中删除题目，对外服务接口。
     *
     * @param entity 问卷编号，题目编号列表
     * @return 无
     */
    @Transactional
    public Mono<Void> deleteQues(Mono<Tuple2<String, List<String>>> entity) {

        return entity
            .map(tup -> {

                List<ExamQuesRlnEntity> lisExamQuesRln = new ArrayList<>();
                for (String quesCd : tup.getT2()) {

                    ExamQuesRlnEntity en = new ExamQuesRlnEntity();
                    en.setExamCd(tup.getT1());
                    en.setQuesCd(quesCd);

                    lisExamQuesRln.add(en);
                }

                return lisExamQuesRln;
            })
            .flatMapMany(lis -> Flux.deferContextual(ctx -> {

                String openId = ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID);
                for (ExamQuesRlnEntity en : lis) {

                    en.setLastMantUsr(openId);
                }

                return Flux.fromIterable(lis);
            }))
            .flatMap(one -> examQuesRlnRepository.deleteByExamCdAndQuesCdAndLastMantUsr(one.getExamCd(), one.getQuesCd(), one.getLastMantUsr()))
            .then();
    }

    /**
     * <p>查看问卷中未添加过的题目，对外服务接口。
     *
     * @param entity 问卷Entity
     * @param pageNum 页编号
     * @param pageSize 页大小
     * @return 题目Entity列表
     */
    public Flux<QuesEntity> findAvaQues(Mono<ExamEntity> entity, Integer pageNum, Integer pageSize) {

        return entity
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setLastMantUsr(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMapMany(one -> quesRepository.findAvaByExamCdAndLastMantUsr(one.getExamCd(), one.getLastMantUsr(), pageSize, (pageNum - 1) * pageSize));
    }
}
