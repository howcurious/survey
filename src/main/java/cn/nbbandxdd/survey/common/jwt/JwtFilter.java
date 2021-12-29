package cn.nbbandxdd.survey.common.jwt;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * <p>Jwt过滤器。校验请求中是否包含形如“authorization: Bearer myToken”的报文头，并校验“myToken”。
 *
 * @author howcurious
 */
@Component
public class JwtFilter implements WebFilter {

    /**
     * <p>登录请求路径。
     */
    private final PathPattern pathPattern = new PathPatternParser().parse("/login");

    /**
     * <p>校验Jwt。
     * 如果报文头authorization缺失或格式错误，则返回状态码{@code HttpStatus.BAD_REQUEST}；
     * 如果Jwt校验失败，则返回状态码{@code HttpStatus.BAD_REQUEST}。
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (pathPattern.matches(exchange.getRequest().getPath().pathWithinApplication())) {

            return chain.filter(exchange);
        }

        return Optional.ofNullable(exchange.getRequest().getHeaders().get("authorization"))
            .flatMap(lis -> lis.stream().findFirst())
            .filter(authHeader -> StringUtils.startsWith(authHeader, "Bearer "))
            .map(authHeader -> RegExUtils.replaceFirst(authHeader, "^Bearer ", StringUtils.EMPTY))
            .map(JwtUtils::fillbackOpenidFromToken)
            .map(openId -> chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(ICommonConstDefine.CONTEXT_KEY_OPEN_ID, openId)))
            .orElseGet(() -> {

                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().writeWith(Mono.empty());
            });
    }
}
