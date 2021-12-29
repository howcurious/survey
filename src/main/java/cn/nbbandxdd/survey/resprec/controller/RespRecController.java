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
 *
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
     */
    public Mono<ServerResponse> findGrpStat(ServerRequest request) {

        Mono<GrpStatEntity> entity = request.bodyToMono(GrpStatVO.class)
            .map(one -> ModelMapper.map(one, GrpStatEntity.class));

        return ServerResponse.ok().body(respRecService.findGrpStat(entity), GrpStatVO.class);
    }

    /**
     * <p>查询作答统计（按问卷）。
     */
    public Mono<ServerResponse> findExamStat(ServerRequest request) {

        Mono<ExamStatEntity> entity = request.bodyToMono(ExamStatVO.class)
            .map(one -> ModelMapper.map(one, ExamStatEntity.class));

        return ServerResponse.ok().body(respRecService.findExamStat(entity), ExamStatVO.class);
    }
}
