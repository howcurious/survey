package cn.nbbandxdd.survey.ncov.controller;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.ModelMapper;
import cn.nbbandxdd.survey.common.exception.SurveyMsgSecCheckException;
import cn.nbbandxdd.survey.common.wechat.msgseccheck.MsgSecCheck;
import cn.nbbandxdd.survey.ncov.controller.vo.NCoVStatVO;
import cn.nbbandxdd.survey.ncov.controller.vo.NCoVVO;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVEntity;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVStatEntity;
import cn.nbbandxdd.survey.ncov.service.NCoVService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class NCoVController {

    private final NCoVService nCoVService;

    private final MsgSecCheck msgSecCheck;

    public NCoVController(NCoVService nCoVService, MsgSecCheck msgSecCheck) {

        this.nCoVService = nCoVService;
        this.msgSecCheck = msgSecCheck;
    }

    @Bean
    public RouterFunction<?> nCoVRouterFunction() {

        return route(POST("/NCoV/save"), this::save)
            .andRoute(POST("/NCoV/findById"), this::findById)
            .andRoute(POST("/NCoV/findDprtStat"), this::findDprtStat)
            .andRoute(POST("/NCoV/findGrpStat"), this::findGrpStat);
    }

    public Mono<ServerResponse> save(ServerRequest request) {

        Mono<NCoVEntity> entity = request.bodyToMono(NCoVVO.class)
            .map(one -> ModelMapper.map(one, NCoVEntity.class))
            .filterWhen(one -> msgSecCheck.get(StringUtils.joinWith("。", one.getQ01(), one.getQ02(), one.getQ03(), one.getQ04(), one.getQ05(), one.getQ06(), one.getQ07(), one.getQ08(), one.getQ09(), one.getQ10(), one.getQ11(), one.getQ12(), one.getQ13(), one.getQ14())).map(dto -> {

                boolean valid = dto.getErrcode() == null || StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);
                if (!valid) {

                    log.error("问卷内容检测失败，{}：{}。", dto.getErrcode(), dto.getErrMsg());
                }
                return valid;
            }))
            .switchIfEmpty(Mono.error(new SurveyMsgSecCheckException()));

        return ServerResponse.ok().body(nCoVService.save(entity), Void.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {

        Mono<NCoVVO> body = nCoVService.findById()
            .map(one -> ModelMapper.map(one, NCoVVO.class));

        return ServerResponse.ok().body(body, NCoVVO.class);
    }

    public Mono<ServerResponse> findDprtStat(ServerRequest request) {

        Flux<NCoVStatVO> body = nCoVService.findDprtStat()
            .map(one -> ModelMapper.map(one, NCoVStatVO.class));

        return ServerResponse.ok().body(body,NCoVStatVO.class);
    }

    public Mono<ServerResponse> findGrpStat(ServerRequest request) {

        Mono<NCoVStatEntity> entity = request.bodyToMono(NCoVStatVO.class)
            .map(one -> ModelMapper.map(one, NCoVStatEntity.class));

        Flux<NCoVStatVO> body = nCoVService.findGrpStat(entity)
            .map(one -> ModelMapper.map(one, NCoVStatVO.class));

        return ServerResponse.ok().body(body,NCoVStatVO.class);
    }
}
