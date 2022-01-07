package cn.nbbandxdd.survey.exam.controller;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.ModelMapper;
import cn.nbbandxdd.survey.common.exception.SurveyMsgSecCheckException;
import cn.nbbandxdd.survey.common.wechat.msgseccheck.MsgSecCheck;
import cn.nbbandxdd.survey.exam.controller.vo.*;
import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import cn.nbbandxdd.survey.exam.repository.entity.ExamQuesTypRlnEntity;
import cn.nbbandxdd.survey.exam.service.ExamService;
import cn.nbbandxdd.survey.ques.controller.vo.QuesVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <p>问卷Controller。
 *
 * <ul>
 * <li>新增问卷，使用{@link #insert(ServerRequest)}。</li>
 * <li>删除问卷，使用{@link #delete(ServerRequest)}。</li>
 * <li>修改问卷，使用{@link #update(ServerRequest)}。</li>
 * <li>查看问卷状态，使用{@link #findStatus(ServerRequest)}。</li>
 * <li>查看问卷详情，使用{@link #findDetail(ServerRequest)}。</li>
 * <li>查看问卷待作答题目，使用{@link #findToAnsw(ServerRequest)}。</li>
 * <li>查看问卷题目列表，使用{@link #findQuesList(ServerRequest)}。</li>
 * <li>查看问卷列表，使用{@link #findList(ServerRequest)}。</li>
 * <li>查看公共问卷列表，使用{@link #findCommonList(ServerRequest)}。</li>
 * <li>问卷中新增题目，使用{@link #insertQues(ServerRequest)}。</li>
 * <li>问卷中删除题目，使用{@link #deleteQues(ServerRequest)}。</li>
 * <li>查看问卷中未添加过的题目，使用{@link #findAvaQues(ServerRequest)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Slf4j
@Configuration
public class ExamController {

    /**
     * <p>问卷Service。
     */
    private final ExamService examService;

    /**
     * <p>检查一段文本是否含有违法违规内容。
     */
    private final MsgSecCheck msgSecCheck;

    /**
     * <p>构造器。
     *
     * @param examService 问卷Service
     * @param msgSecCheck 检查一段文本是否含有违法违规内容
     */
    public ExamController(ExamService examService, MsgSecCheck msgSecCheck) {

        this.examService = examService;
        this.msgSecCheck = msgSecCheck;
    }

    /**
     * <p>路由函数。
     */
    @Bean
    public RouterFunction<?> examRouterFunction() {

        return route(POST("/exam/insert"), this::insert)
            .andRoute(POST("/exam/delete"), this::delete)
            .andRoute(POST("/exam/update"), this::update)
            .andRoute(POST("/exam/findStatus"), this::findStatus)
            .andRoute(POST("/exam/findDetail"), this::findDetail)
            .andRoute(POST("/exam/findToAnsw"), this::findToAnsw)
            .andRoute(POST("/exam/findQuesList"), this::findQuesList)
            .andRoute(POST("/exam/findList"), this::findList)
            .andRoute(POST("/exam/findCommonList"), this::findCommonList)
            .andRoute(POST("/exam/insertQues"), this::insertQues)
            .andRoute(POST("/exam/deleteQues"), this::deleteQues)
            .andRoute(POST("/exam/findAvaQues"), this::findAvaQues);
    }

    /**
     * <p>新增问卷。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code typCd}：问卷类型（必输）。取值范围：0：手工选择题目；1：手工选择题目；2：随机生成题目。</li>
     * <li>{@code rpetInd}：重复作答标识（必输）。取值范围：0：否；1：是。</li>
     * <li>{@code cntdwnInd}：倒计时标识（选输）。取值范围：0：否；1（默认）：是。</li>
     * <li>{@code answImmInd}：立即展示答案标识（选输）。取值范围：0：否；1（默认）：是。</li>
     * <li>{@code ttl}：标题（必输）。校验失败情形：空白字段。</li>
     * <li>{@code dsc}：描述（选输）。</li>
     * <li>{@code bgnTime}：起始时间（选输）。默认值：当前时间。</li>
     * <li>{@code endTime}：截止时间（选输）。默认值：当前时间+1天。</li>
     * <li>{@code quesTypRlnList}：问卷与题目类型间关系列表（选输）：</li>
     * <li> - {@code quesTypCd}：题目类型（必输）。取值范围：1：单选；2：多选。</li>
     * <li> - {@code scre}：题目类型分数（必输）。取值范围：[0, 100]。</li>
     * <li> - {@code cnt}：题目类型数量（必输）。取值范围：[0, 100]。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code examCd}：问卷编号。</li>
     * </ul>
     */
    public Mono<ServerResponse> insert(ServerRequest request) {

        List<ExamQuesTypRlnEntity> dummy = new ArrayList<>();
        Mono<Tuple2<ExamEntity, List<ExamQuesTypRlnEntity>>> entity = request.bodyToMono(ExamInsertVO.class)
            .map(one -> Tuples.of(
                ModelMapper.map(one, ExamEntity.class),
                StringUtils.equals(one.getTypCd(), ICommonConstDefine.EXAM_TYP_CD_RANDOM) && one.getQuesTypRlnList() != null ?
                    ModelMapper.map(one.getQuesTypRlnList(), new TypeToken<ArrayList<ExamQuesTypRlnEntity>>() {}.getType()) :
                    dummy))
            .filterWhen(tup -> msgSecCheck.get(StringUtils.joinWith("。", tup.getT1().getTtl(), tup.getT1().getDsc())).map(dto -> {

                boolean valid = dto.getErrcode() == null || StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);
                if (!valid) {

                    log.error("问卷内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
                }
                return valid;
            }))
            .switchIfEmpty(Mono.error(new SurveyMsgSecCheckException()));

        Mono<ExamVO> body = examService.insert(entity)
            .map(one -> ModelMapper.map(one, ExamVO.class));

        return ServerResponse.ok().body(body, ExamVO.class);
    }

    /**
     * <p>删除问卷。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    public Mono<ServerResponse> delete(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        return ServerResponse.ok().body(examService.delete(entity), Void.class);
    }

    /**
     * <p>修改问卷。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code rpetInd}：重复作答标识（必输）。取值范围：0：否；1：是。</li>
     * <li>{@code cntdwnInd}：倒计时标识（选输）。取值范围：0：否；1：是。</li>
     * <li>{@code answImmInd}：立即展示答案标识（选输）。取值范围：0：否；1：是。</li>
     * <li>{@code ttl}：标题（必输）。校验失败情形：空白字段。</li>
     * <li>{@code dsc}：描述（选输）。</li>
     * <li>{@code bgnTime}：起始时间（选输）。</li>
     * <li>{@code endTime}：截止时间（选输）。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    public Mono<ServerResponse> update(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class))
            .filterWhen(one -> msgSecCheck.get(StringUtils.joinWith("。", one.getTtl(), one.getDsc())).map(dto -> {

                boolean valid = dto.getErrcode() == null || StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);
                if (!valid) {

                    log.error("问卷内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
                }
                return valid;
            }))
            .switchIfEmpty(Mono.error(new SurveyMsgSecCheckException()));

        return ServerResponse.ok().body(examService.update(entity), Void.class);
    }

    /**
     * <p>查看问卷状态。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code status}：问卷状态：</li>
     * <li> - {@code deleted}：已删除。</li>
     * <li> - {@code notstart}：未开始。</li>
     * <li> - {@code finished}：已结束。</li>
     * <li> - {@code completed_and_repeatable}：已完成，可重复作答。</li>
     * <li> - {@code completed_and_unrepeatable}：已完成，不可重复作答。</li>
     * <li> - {@code to_be_completed}：未作答。</li>
     * </ul>
     */
    public Mono<ServerResponse> findStatus(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Mono<ExamStatusVO> body = examService.findStatus(entity)
            .map(tup -> {

                ExamStatusVO vo = new ExamStatusVO();
                vo.setExamCd(tup.getT1());
                vo.setStatus(tup.getT2());

                return vo;
            });

        return ServerResponse.ok().body(body, ExamStatusVO.class);
    }

    /**
     * <p>查看问卷详情。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code examCd}：问卷编号。</li>
     * <li>{@code typCd}：问卷类型。</li>
     * <li>{@code rpetInd}：重复作答标识。</li>
     * <li>{@code cntdwnInd}：倒计时标识。</li>
     * <li>{@code answImmInd}：立即展示答案标识。</li>
     * <li>{@code ttl}：标题。</li>
     * <li>{@code dsc}：描述。</li>
     * <li>{@code bgnTime}：起始时间。</li>
     * <li>{@code endTime}：截止时间。</li>
     * <li>{@code quesCnt}：问卷题目数量。</li>
     * <li>{@code quesTypRlnList}：问卷与题目类型间关系列表：</li>
     * <li> - {@code quesTypCd}：题目类型。</li>
     * <li> - {@code scre}：题目类型分数。</li>
     * <li> - {@code cnt}：题目类型数量。</li>
     * </ul>
     */
    public Mono<ServerResponse> findDetail(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Mono<ExamDetailVO> body = examService.findDetail(entity)
            .map(tup -> {

                ExamDetailVO vo = ModelMapper.map(tup.getT1(), ExamDetailVO.class);
                vo.setQuesCnt(tup.getT2());
                vo.setQuesTypRlnList(ModelMapper.map(tup.getT3(), new TypeToken<ArrayList<ExamQuesTypRlnVO>>() {}.getType()));

                return vo;
            });

        return ServerResponse.ok().body(body, ExamDetailVO.class);
    }

    /**
     * <p>查看问卷待作答题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code examCd}：问卷编号。</li>
     * <li>{@code typCd}：问卷类型。</li>
     * <li>{@code rpetInd}：重复作答标识。</li>
     * <li>{@code cntdwnInd}：倒计时标识。</li>
     * <li>{@code answImmInd}：立即展示答案标识。</li>
     * <li>{@code ttl}：标题。</li>
     * <li>{@code dsc}：描述。</li>
     * <li>{@code bgnTime}：起始时间。</li>
     * <li>{@code endTime}：截止时间。</li>
     * <li>{@code quesList}：题目列表：</li>
     * <li> - {@code quesCd}：题目编号。</li>
     * <li> - {@code typCd}：题目类型。</li>
     * <li> - {@code dsc}：描述。</li>
     * </ul>
     */
    public Mono<ServerResponse> findToAnsw(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Mono<ExamWithQuesVO> body = examService.findToAnsw(entity)
            .map(tup -> {

                ExamWithQuesVO vo = ModelMapper.map(tup.getT1(), ExamWithQuesVO.class);
                vo.setQuesList(ModelMapper.map(tup.getT2(), new TypeToken<ArrayList<QuesVO>>() {}.getType()));

                return vo;
            });

        return ServerResponse.ok().body(body, ExamWithQuesVO.class);
    }

    /**
     * <p>查看问卷题目列表。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li> - {@code quesCd}：题目编号。</li>
     * <li> - {@code typCd}：题目类型。</li>
     * <li> - {@code dsc}：描述。</li>
     * </ul>
     */
    public Mono<ServerResponse> findQuesList(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<QuesVO> body = examService.findQuesList(entity, pageNum, pageSize)
            .map(one -> ModelMapper.map(one, QuesVO.class));

        return ServerResponse.ok().body(body, QuesVO.class);
    }

    /**
     * <p>查看问卷列表。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li> - {@code examCd}：问卷编号。</li>
     * <li> - {@code typCd}：问卷类型。</li>
     * <li> - {@code rpetInd}：重复作答标识。</li>
     * <li> - {@code cntdwnInd}：倒计时标识。</li>
     * <li> - {@code answImmInd}：立即展示答案标识。</li>
     * <li> - {@code ttl}：标题。</li>
     * <li> - {@code dsc}：描述。</li>
     * <li> - {@code bgnTime}：起始时间。</li>
     * <li> - {@code endTime}：截止时间。</li>
     * </ul>
     */
    public Mono<ServerResponse> findList(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<ExamVO> body = examService.findList(pageNum, pageSize)
            .map(one -> ModelMapper.map(one, ExamVO.class));

        return ServerResponse.ok().body(body, ExamVO.class);
    }

    /**
     * <p>查看公共问卷列表。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li> - {@code examCd}：问卷编号。</li>
     * <li> - {@code typCd}：问卷类型。</li>
     * <li> - {@code rpetInd}：重复作答标识。</li>
     * <li> - {@code cntdwnInd}：倒计时标识。</li>
     * <li> - {@code answImmInd}：立即展示答案标识。</li>
     * <li> - {@code ttl}：标题。</li>
     * <li> - {@code dsc}：描述。</li>
     * <li> - {@code bgnTime}：起始时间。</li>
     * <li> - {@code endTime}：截止时间。</li>
     * </ul>
     */
    public Mono<ServerResponse> findCommonList(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<ExamVO> body = examService.findCommonList(pageNum, pageSize)
            .map(one -> ModelMapper.map(one, ExamVO.class));

        return ServerResponse.ok().body(body, ExamVO.class);
    }

    /**
     * <p>问卷中新增题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * <li>{@code quesCds}：题目编号列表（必输）。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    public Mono<ServerResponse> insertQues(ServerRequest request) {

        Mono<Tuple2<String, List<String>>> entity = request.bodyToMono(ExamQuesRlnVO.class)
            .map(one -> Tuples.of(one.getExamCd(), one.getQuesCds()));

        return ServerResponse.ok().body(examService.insertQues(entity), Void.class);
    }

    /**
     * <p>问卷中删除题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * <li>{@code quesCds}：题目编号列表（必输）。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    public Mono<ServerResponse> deleteQues(ServerRequest request) {

        Mono<Tuple2<String, List<String>>> entity = request.bodyToMono(ExamQuesRlnVO.class)
            .map(one -> Tuples.of(one.getExamCd(), one.getQuesCds()));

        return ServerResponse.ok().body(examService.deleteQues(entity), Void.class);
    }

    /**
     * <p>查看问卷中未添加过的题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li> - {@code quesCd}：题目编号。</li>
     * <li> - {@code typCd}：题目类型。</li>
     * <li> - {@code dsc}：描述。</li>
     * </ul>
     */
    public Mono<ServerResponse> findAvaQues(ServerRequest request) {

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<QuesVO> body = examService.findAvaQues(entity, pageNum, pageSize)
            .map(one -> ModelMapper.map(one, QuesVO.class));

        return ServerResponse.ok().body(body, QuesVO.class);
    }
}
