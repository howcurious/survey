package cn.nbbandxdd.survey.usrinfo.controller;

import cn.nbbandxdd.survey.usrinfo.repository.entity.UsrInfoEntity;
import cn.nbbandxdd.survey.usrinfo.service.UsrInfoService;
import cn.nbbandxdd.survey.usrinfo.controller.vo.UsrInfoVO;
import cn.nbbandxdd.survey.common.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <p>用户信息Controller。
 *
 * <ul>
 * <li>新增实名登记，使用{@link #insert(ServerRequest)}。</li>
 * <li>修改实名登记，使用{@link #update(ServerRequest)}。</li>
 * <li>查询实名登记，使用{@link #load(ServerRequest)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Configuration
public class UsrInfoController {

    /**
     * <p>用户信息Service。
     */
    private final UsrInfoService usrInfoService;

    /**
     * <p>构造器。
     *
     * @param usrInfoService 用户信息Service
     */
    public UsrInfoController(UsrInfoService usrInfoService) {

        this.usrInfoService = usrInfoService;
    }

    /**
     * <p>路由函数。
     */
    @Bean
    public RouterFunction<?> usrInfoRouterFunction() {

        return route(POST("/usrinfo/insert"), this::insert)
            .andRoute(POST("/usrinfo/update"), this::update)
            .andRoute(POST("/usrinfo/load"), this::load);
    }

    /**
     * <p>新增实名登记。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code dprtNam}：部门名（必输）。校验失败情形：空白字段；与分组名不匹配。</li>
     * <li>{@code grpNam}：分组名（必输）。校验失败情形：空白字段；与部门名不匹配。</li>
     * <li>{@code usrNam}：用户名（必输）。校验失败情形：空白字段。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    private Mono<ServerResponse> insert(ServerRequest request) {

        Mono<UsrInfoEntity> entity = request.bodyToMono(UsrInfoVO.class)
            .map(one -> ModelMapper.map(one, UsrInfoEntity.class));

        Mono<Void> body = usrInfoService.insert(entity);

        return ServerResponse.ok().body(body, Void.class);
    }

    /**
     * <p>修改实名登记。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code dprtNam}：部门名（必输）。校验失败情形：空白字段；与分组名不匹配。</li>
     * <li>{@code grpNam}：分组名（必输）。校验失败情形：空白字段；与部门名不匹配。</li>
     * <li>{@code usrNam}：用户名（必输）。校验失败情形：空白字段。</li>
     * </ul>
     *
     * <p>输出字段：无。
     */
    private Mono<ServerResponse> update(ServerRequest request) {

        Mono<UsrInfoEntity> entity = request.bodyToMono(UsrInfoVO.class)
            .map(one -> ModelMapper.map(one, UsrInfoEntity.class));

        Mono<Void> body = usrInfoService.update(entity);

        return ServerResponse.ok().body(body, Void.class);
    }

    /**
     * <p>查询实名登记。依据Http请求报文头authorization，返回用户实名登记信息。
     *
     * <p>输入字段：无。
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code dprtNam}：部门名。</li>
     * <li>{@code grpNam}：分组名。</li>
     * <li>{@code usrNam}：用户名。</li>
     * </ul>
     */
    private Mono<ServerResponse> load(ServerRequest request) {

        Mono<UsrInfoVO> body = usrInfoService.loadByKey()
            .map(one -> ModelMapper.map(one, UsrInfoVO.class));

        return ServerResponse.ok().body(body, UsrInfoVO.class);
    }
}
