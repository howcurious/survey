package cn.nbbandxdd.survey.resprec.controller;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.ModelMapper;
import cn.nbbandxdd.survey.exam.controller.vo.ExamVO;
import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import cn.nbbandxdd.survey.resprec.controller.vo.*;
import cn.nbbandxdd.survey.resprec.repository.entity.DtlRecEntity;
import cn.nbbandxdd.survey.resprec.repository.entity.ExamStatEntity;
import cn.nbbandxdd.survey.resprec.repository.entity.GrpStatEntity;
import cn.nbbandxdd.survey.resprec.service.RespRecService;
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
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <p>作答记录Controller。
 *
 * <ul>
 * <li>新增作答明细，使用{@link #insertDtl(ServerRequest)}。</li>
 * <li>查询作答记录，使用{@link #findRespList(ServerRequest)}。</li>
 * <li>查询作答排名，使用{@link #findUsrRank(ServerRequest)}。</li>
 * <li>查询作答统计（按分组），使用{@link #findGrpStat(ServerRequest)}。</li>
 * <li>查询作答统计（按问卷），使用{@link #findExamStat(ServerRequest)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Slf4j
@Configuration
public class RespRecController {

    /**
     * <p>作答记录Service。
     */
    private final RespRecService respRecService;

    /**
     * <p>构造器。
     *
     * @param respRecService 作答记录Service
     */
    public RespRecController(RespRecService respRecService) {

        this.respRecService = respRecService;
    }

    /**
     * <p>路由函数。
     */
    @Bean
    public RouterFunction<?> respRecRouterFunction() {

        return route(POST("/resprec/insertDtl"), this::insertDtl)
            .andRoute(POST("/resprec/findRespList"), this::findRespList)
            .andRoute(POST("/resprec/findUsrRank"), this::findUsrRank)
            .andRoute(POST("/resprec/findGrpStat"), this::findGrpStat)
            .andRoute(POST("/resprec/findExamStat"), this::findExamStat);
    }

    /**
     * <p>新增作答明细。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * <li>{@code quesCd}：题目编号（必输）。</li>
     * <li>{@code answList}：答案列表（必输）：</li>
     * <li> - {@code answCd}：答案编号（必输）。</li>
     * <li>{@code lastInd}：最后一题标识（选输）。取值范围：0（默认）：否；1：是。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code examCd}：问卷编号。</li>
     * <li>{@code quesCd}：题目编号。</li>
     * <li>{@code answList}：答案列表：</li>
     * <li> - {@code answCd}：正确答案编号。</li>
     * <li>{@code respScre}：问卷作答分数。</li>
     * <li>{@code respSpnd}：问卷作答用时。</li>
     * </ul>
     */
    public Mono<ServerResponse> insertDtl(ServerRequest request) {

        List<String> dummy = new ArrayList<>();
        Mono<Tuple3<DtlRecEntity, String, List<String>>> entity = request.bodyToMono(DtlRecVO.class)
            .map(one -> Tuples.of(
                ModelMapper.map(one, DtlRecEntity.class),
                StringUtils.isBlank(one.getLastInd()) ? ICommonConstDefine.COMMON_IND_NO : one.getLastInd(),
                one.getAnswList() != null ?
                    ModelMapper.map(one.getAnswList(), new TypeToken<ArrayList<String>>() {}.getType()) :
                    dummy));

        Mono<DtlRecVO> body = respRecService.insertDtl(entity)
            .map(tup -> {

                DtlRecVO vo = ModelMapper.map(tup.getT1(), DtlRecVO.class);
                vo.setAnswList(ModelMapper.map(tup.getT2(), new TypeToken<ArrayList<String>>() {}.getType()));
                vo.setRespScre(tup.getT3());
                vo.setRespSpnd(tup.getT4());

                return vo;
            });

        return ServerResponse.ok().body(body, DtlRecVO.class);
    }

    /**
     * <p>查询作答记录。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code examCd}：问卷编号。</li>
     * <li>{@code ttl}：标题。</li>
     * <li>{@code scre}：分数。</li>
     * <li>{@code spnd}：用时。</li>
     * <li>{@code dat}：作答日期。</li>
     * </ul>
     */
    public Mono<ServerResponse> findRespList(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Flux<RespRecVO> body = respRecService.findRespList(pageNum, pageSize)
            .map(tup -> {

                RespRecVO vo = ModelMapper.map(tup.getT1(), RespRecVO.class);
                vo.setTtl(tup.getT2());

                return vo;
            });

        return ServerResponse.ok().body(body, RespRecVO.class);
    }

    /**
     * <p>查询作答排名。
     *
     * <p>输入字段：无。
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code examCd}：问卷编号。</li>
     * <li>{@code dprtNam}：部门名。</li>
     * <li>{@code grpNam}：分组名。</li>
     * <li>{@code usrNam}：用户名。</li>
     * <li>{@code scre}：分数。</li>
     * <li>{@code spnd}：用时。</li>
     * <li>{@code dat}：作答日期。</li>
     * </ul>
     */
    public Mono<ServerResponse> findUsrRank(ServerRequest request) {

        Integer pageNum = Integer.parseInt(request.queryParam("pageNum").orElse("1"));
        Integer pageSize = Integer.parseInt(request.queryParam("pageSize").orElse("10"));

        Mono<ExamEntity> entity = request.bodyToMono(ExamVO.class)
            .map(one -> ModelMapper.map(one, ExamEntity.class));

        Flux<UsrRankVO> body = respRecService.findUsrRank(entity, pageNum, pageSize)
            .map(tup -> {

                UsrRankVO vo = ModelMapper.map(tup.getT1(), UsrRankVO.class);
                vo.setDprtNam(tup.getT2());
                vo.setGrpNam(tup.getT3());
                vo.setUsrNam(tup.getT4());

                return vo;
            });

        return ServerResponse.ok().body(body, UsrRankVO.class);
    }

    /**
     * <p>查询作答统计（按分组）。
     *
     * <p>输入字段：无。
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * <li>{@code dprtNam}：部门名（选输）。默认值为用户所在部门名</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code dprtNam}：部门名。</li>
     * <li>{@code grpNam}：分组名。</li>
     * <li>{@code cnt}：参与人数。</li>
     * <li>{@code totCnt}：总人数。</li>
     * <li>{@code ptpnRate}：参与率。</li>
     * <li>{@code avgScre}：平均分数。</li>
     * <li>{@code avgSpnd}：平均用时。</li>
     * </ul>
     */
    public Mono<ServerResponse> findGrpStat(ServerRequest request) {

        Mono<GrpStatEntity> entity = request.bodyToMono(GrpStatVO.class)
            .map(one -> ModelMapper.map(one, GrpStatEntity.class));

        return ServerResponse.ok().body(respRecService.findGrpStat(entity), GrpStatVO.class);
    }

    /**
     * <p>查询作答统计（按问卷）。
     *
     * <p>输入字段：无。
     * <ul>
     * <li>{@code examCd}：问卷编号（必输）。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code ttl}：标题。</li>
     * <li>{@code avgScre}：平均分数。</li>
     * <li>{@code avgSpnd}：平均用时。</li>
     * <li>{@code cnt}：参与人数。</li>
     * <li>{@code cntU40}：参与人数，分数区间[0, 40]。</li>
     * <li>{@code rateU40}：分数占比，区间[0, 40]。</li>
     * <li>{@code cntU70}：参与人数，分数区间[41, 70]。</li>
     * <li>{@code rateU70}：分数占比，区间[40, 70]。</li>
     * <li>{@code cntU100}：参与人数，分数区间[71, 100]。</li>
     * <li>{@code rateU100}：分数占比，区间[71, 100]。</li>
     * <li>{@code cnt100}：参与人数，分数100。</li>
     * <li>{@code rate100}：分数占比，分数100。</li>
     * </ul>
     */
    public Mono<ServerResponse> findExamStat(ServerRequest request) {

        Mono<ExamStatEntity> entity = request.bodyToMono(ExamStatVO.class)
            .map(one -> ModelMapper.map(one, ExamStatEntity.class));

        return ServerResponse.ok().body(respRecService.findExamStat(entity), ExamStatVO.class);
    }
}
