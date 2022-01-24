package cn.nbbandxdd.survey.ques.controller;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.ModelMapper;
import cn.nbbandxdd.survey.common.exception.SurveyMsgSecCheckException;
import cn.nbbandxdd.survey.common.wechat.msgseccheck.MsgSecCheck;
import cn.nbbandxdd.survey.exam.controller.vo.ExamVO;
import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import cn.nbbandxdd.survey.ques.controller.vo.*;
import cn.nbbandxdd.survey.ques.repository.entity.AnswEntity;
import cn.nbbandxdd.survey.ques.repository.entity.QuesEntity;
import cn.nbbandxdd.survey.ques.service.QuesService;
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
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <p>题目Controller。
 *
 * <ul>
 * <li>新增题目，使用{@link #insert(ServerRequest)}。</li>
 * <li>删除题目，使用{@link #delete(ServerRequest)}。</li>
 * <li>修改题目，使用{@link #update(ServerRequest)}。</li>
 * <li>查看题目详情（出题者视角），使用{@link #findByPron(ServerRequest)}。</li>
 * <li>查看题目详情（答题者视角），使用{@link #findByResp(ServerRequest)}。</li>
 * <li>查看题目详情（回看者视角），使用{@link #findByExp(ServerRequest)}。</li>
 * <li>查看题目列表，使用{@link #findList(ServerRequest)}。</li>
 * <li>查看公共题目列表，使用{@link #findCommonList(ServerRequest)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Slf4j
@Configuration
public class QuesController {

    /**
     * <p>题目Service。
     */
    private final QuesService quesService;

    /**
     * <p>检查一段文本是否含有违法违规内容。
     */
    private final MsgSecCheck msgSecCheck;

    /**
     * <p>构造器。
     *
     * @param quesService 题目Service
     * @param msgSecCheck 检查一段文本是否含有违法违规内容
     */
    public QuesController(QuesService quesService, MsgSecCheck msgSecCheck) {

        this.quesService = quesService;
        this.msgSecCheck = msgSecCheck;
    }

    /**
     * <p>路由函数。
     */
    @Bean
    public RouterFunction<?> quesRouterFunction() {

        return route(POST("/ques/insert"), this::insert)
            .andRoute(POST("/ques/delete"), this::delete)
            .andRoute(POST("/ques/update"), this::update)
            .andRoute(POST("/ques/findByPron"), this::findByPron)
            .andRoute(POST("/ques/findByResp"), this::findByResp)
            .andRoute(POST("/ques/findByExp"), this::findByExp)
            .andRoute(POST("/ques/findList"), this::findList)
            .andRoute(POST("/ques/findCommonList"), this::findList);
    }

    /**
     * <p>新增题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code typCd}：题目类型（必输）。取值范围：1：单选；2：多选。</li>
     * <li>{@code dsc}：描述（必输）。校验失败情形：空白字段。</li>
     * <li>{@code comm}：题解（选输）。</li>
     * <li>{@code answList}：答案列表（必输）：</li>
     * <li> - {@code dsc}：描述（必输）。校验失败情形：空白字段。</li>
     * <li> - {@code scre}：分数（必输）。取值范围：[0, 100]。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code quesCd}：题目编号。</li>
     * </ul>
     */
    public Mono<ServerResponse> insert(ServerRequest request) {

        List<AnswEntity> dummy = new ArrayList<>();
        Mono<Tuple2<QuesEntity, List<AnswEntity>>> entity = request.bodyToMono(QuesByPronVO.class)
            .map(one -> Tuples.of(
                ModelMapper.map(one, QuesEntity.class),
                one.getAnswList() != null ?
                    ModelMapper.map(one.getAnswList(), new TypeToken<ArrayList<AnswEntity>>() {}.getType()) :
                    dummy))
            .filterWhen(tup -> msgSecCheck.get(
                StringUtils.joinWith("。", tup.getT1().getDsc(), tup.getT1().getComm(),
                    StringUtils.joinWith("。", tup.getT2().stream().map(AnswEntity::getDsc).collect(Collectors.toList()))))
                .map(dto -> {

                    boolean valid = dto.getErrcode() == null || StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);
                    if (!valid) {

                        log.error("问卷内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
                    }
                    return valid;
                }))
            .switchIfEmpty(Mono.error(new SurveyMsgSecCheckException()));

        Mono<QuesVO> body = quesService.insert(entity)
            .map(one -> ModelMapper.map(one, QuesVO.class));

        return ServerResponse.ok().body(body, QuesVO.class);
    }

    /**
     * <p>删除题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code quesCd}：题目编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    public Mono<ServerResponse> delete(ServerRequest request) {

        Mono<QuesEntity> entity = request.bodyToMono(QuesByPronVO.class)
            .map(one -> ModelMapper.map(one, QuesEntity.class));

        return ServerResponse.ok().body(quesService.delete(entity), Void.class);
    }

    /**
     * <p>修改题目。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code quesCd}：题目编号（必输）。</li>
     * <li>{@code typCd}：题目类型（必输）。取值范围：1：单选；2：多选。</li>
     * <li>{@code dsc}：描述（必输）。校验失败情形：空白字段。</li>
     * <li>{@code comm}：题解（选输）。</li>
     * <li>{@code answList}：答案列表（必输）：</li>
     * <li> - {@code dsc}：描述（必输）。校验失败情形：空白字段。</li>
     * <li> - {@code scre}：分数（必输）。取值范围：[0, 100]。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    public Mono<ServerResponse> update(ServerRequest request) {

        List<AnswEntity> dummy = new ArrayList<>();
        Mono<Tuple2<QuesEntity, List<AnswEntity>>> entity = request.bodyToMono(QuesByPronVO.class)
            .map(one -> Tuples.of(
                    ModelMapper.map(one, QuesEntity.class),
                    one.getAnswList() != null ?
                        ModelMapper.map(one.getAnswList(), new TypeToken<ArrayList<AnswEntity>>() {}.getType()) :
                        dummy))
            .filterWhen(tup -> msgSecCheck.get(
                StringUtils.joinWith("。", tup.getT1().getDsc(), tup.getT1().getComm(),
                    StringUtils.joinWith("。", tup.getT2().stream().map(AnswEntity::getDsc).collect(Collectors.toList()))))
                .map(dto -> {

                    boolean valid = dto.getErrcode() == null || StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);
                    if (!valid) {

                        log.error("问卷内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
                    }
                    return valid;
                }))
            .switchIfEmpty(Mono.error(new SurveyMsgSecCheckException()));

        return ServerResponse.ok().body(quesService.update(entity), Void.class);
    }

    /**
     * <p>查看题目详情（出题者视角）。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code quesCd}：题目编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code quesCd}：题目编号。</li>
     * <li>{@code typCd}：题目类型。</li>
     * <li>{@code dsc}：描述。</li>
     * <li>{@code comm}：题解。</li>
     * <li>{@code answList}：答案列表：</li>
     * <li> - {@code answCd}：答案编号。</li>
     * <li> - {@code dsc}：描述。</li>
     * <li> - {@code scre}：分数。</li>
     * </ul>
     */
    public Mono<ServerResponse> findByPron(ServerRequest request) {

        Mono<QuesEntity> entity = request.bodyToMono(QuesVO.class)
            .map(one -> ModelMapper.map(one, QuesEntity.class));

        Mono<QuesByPronVO> body = quesService.findByPron(entity)
            .map(tup -> {

                QuesByPronVO vo = ModelMapper.map(tup.getT1(), QuesByPronVO.class);
                vo.setAnswList(ModelMapper.map(tup.getT2(), new TypeToken<ArrayList<AnswVO>>() {}.getType()));

                return vo;
            });

        return ServerResponse.ok().body(body, QuesByPronVO.class);
    }

    /**
     * <p>查看题目详情（答题者视角）。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code quesCd}：题目编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code quesCd}：题目编号。</li>
     * <li>{@code typCd}：题目类型。</li>
     * <li>{@code dsc}：描述。</li>
     * <li>{@code comm}：题解。</li>
     * <li>{@code answList}：答案列表：</li>
     * <li> - {@code answCd}：答案编号。</li>
     * <li> - {@code dsc}：描述。</li>
     * </ul>
     */
    public Mono<ServerResponse> findByResp(ServerRequest request) {

        Mono<QuesEntity> entity = request.bodyToMono(QuesVO.class)
            .map(one -> ModelMapper.map(one, QuesEntity.class));

        Mono<QuesByRespVO> body = quesService.findByResp(entity)
            .map(tup -> {

                QuesByRespVO vo = ModelMapper.map(tup.getT1(), QuesByRespVO.class);
                vo.setAnswList(ModelMapper.map(tup.getT2(), new TypeToken<ArrayList<AnswVO>>() {}.getType()));

                return vo;
            });

        return ServerResponse.ok().body(body, QuesByRespVO.class);
    }

    /**
     * <p>查看题目详情（回看者视角）。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code quesCd}：题目编号。</li>
     * <li>{@code typCd}：题目类型。</li>
     * <li>{@code dsc}：描述。</li>
     * <li>{@code comm}：题解。</li>
     * <li>{@code answList}：答案列表：</li>
     * <li> - {@code answCd}：答案编号。</li>
     * <li> - {@code dsc}：描述。</li>
     * <li> - {@code scre}：分数。</li>
     * <li>{@code selList}：选择列表。</li>
     * </ul>
     */
    public Mono<ServerResponse> findByExp(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Flux<QuesByExpVO> body = quesService.findByExp(entity, pageNum, pageSize)
            .map(tup -> {

                QuesByExpVO vo = ModelMapper.map(tup.getT1(), QuesByExpVO.class);
                vo.setAnswList(ModelMapper.map(tup.getT2(), new TypeToken<ArrayList<AnswVO>>() {}.getType()));
                vo.setSelList(ModelMapper.map(tup.getT3(), new TypeToken<ArrayList<String>>() {}.getType()));

                return vo;
            });

        return ServerResponse.ok().body(body, QuesByExpVO.class);
    }

    /**
     * <p>查看题目列表。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code quesCd}：题目编号。</li>
     * <li>{@code typCd}：题目类型。</li>
     * <li>{@code dsc}：描述。</li>
     * </ul>
     */
    public Mono<ServerResponse> findList(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<QuesVO> body = quesService.findList(pageNum, pageSize)
            .map(one -> ModelMapper.map(one, QuesVO.class));

        return ServerResponse.ok().body(body, QuesVO.class);
    }

    /**
     * <p>查看公共题目列表。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code quesCd}：题目编号。</li>
     * <li>{@code typCd}：题目类型。</li>
     * <li>{@code dsc}：描述。</li>
     * </ul>
     */
    public Mono<ServerResponse> findCommonList(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<QuesVO> body = quesService.findCommonList(pageNum, pageSize)
                .map(one -> ModelMapper.map(one, QuesVO.class));

        return ServerResponse.ok().body(body, QuesVO.class);
    }
}
