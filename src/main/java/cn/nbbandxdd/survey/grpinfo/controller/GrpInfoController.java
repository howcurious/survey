package cn.nbbandxdd.survey.grpinfo.controller;

import cn.nbbandxdd.survey.common.ModelMapper;
import cn.nbbandxdd.survey.grpinfo.controller.vo.GrpInfoVO;
import cn.nbbandxdd.survey.grpinfo.repository.entity.GrpInfoEntity;
import cn.nbbandxdd.survey.grpinfo.service.GrpInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <p>分组信息Controller。
 *
 * <ul>
 * <li>查询分组列表，使用{@link #findGrpList(ServerRequest)}。</li>
 * <li>查询部门列表，使用{@link #findDprtList(ServerRequest)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Configuration
public class GrpInfoController {

    /**
     * <p>分组信息Service。
     */
    private final GrpInfoService grpInfoService;

    /**
     * <p>构造器。
     *
     * @param grpInfoService 分组信息Service
     */
    public GrpInfoController(GrpInfoService grpInfoService) {

        this.grpInfoService = grpInfoService;
    }

    /**
     * <p>路由函数。
     */
    @Bean
    public RouterFunction<?> grpInfoRouterFunction() {

        return route(POST("/grpinfo/findGrpList"), this::findGrpList)
            .andRoute(POST("/grpinfo/findDprtList"), this::findDprtList);
    }

    /**
     * <p>查询分组列表。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code dprtNam}：部门名（必输）。校验失败情形：空白字段。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code dprtNam}：部门名。</li>
     * <li>{@code grpNam}：分组名。</li>
     * </ul>
     */
    private Mono<ServerResponse> findGrpList(ServerRequest request) {

        Flux<GrpInfoEntity> entity = request.bodyToFlux(GrpInfoVO.class)
            .map(one -> ModelMapper.map(one, GrpInfoEntity.class));

        Flux<GrpInfoVO> body = grpInfoService.findGrpList(entity)
            .map(one -> ModelMapper.map(one, GrpInfoVO.class));

        return ServerResponse.ok().body(body, GrpInfoVO.class);
    }

    /**
     * <p>查询分组列表。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code dprtNam}：部门名。</li>
     * </ul>
     */
    private Mono<ServerResponse> findDprtList(ServerRequest request) {

        Flux<GrpInfoVO> body = grpInfoService.findDprtList()
            .map(one -> ModelMapper.map(one, GrpInfoVO.class));

        return ServerResponse.ok().body(body, GrpInfoVO.class);
    }
}
