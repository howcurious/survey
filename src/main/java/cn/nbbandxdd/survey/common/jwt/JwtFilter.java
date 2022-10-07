package cn.nbbandxdd.survey.common.jwt;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.SurveyTokenException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;
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
    private final PathPattern loginPathPattern = new PathPatternParser().parse("/login");

    /**
     * <p>健康检查请求路径。
     */
    private final PathPattern healthPathPattern = new PathPatternParser().parse("/actuator/health");

    /**
     * <p>校验Jwt。
     * 如果报文头authorization缺失或错误、Jwt校验失败，则抛出异常{@code SurveyTokenException}，
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (loginPathPattern.matches(exchange.getRequest().getPath().pathWithinApplication()) ||
            healthPathPattern.matches(exchange.getRequest().getPath())) {

            return chain.filter(exchange);
        }
        List<String> platformList = exchange.getRequest().getHeaders().get("x-platform");
        boolean isToBPlatform = !CollectionUtils.isEmpty(platformList) && "admin".equals(platformList.get(0));
        return Optional.ofNullable(exchange.getRequest().getHeaders().get("authorization"))
            .flatMap(lis -> lis.stream().findFirst())
            .filter(authHeader -> StringUtils.startsWith(authHeader, "Bearer "))
            .map(authHeader -> RegExUtils.replaceFirst(authHeader, "^Bearer ", StringUtils.EMPTY))
            .flatMap(token -> {
                try {
                    if (isToBPlatform) {
                        return Optional.of(JwtUtils.getUserNameFromToken(token));
                    } else {
                        return Optional.of(JwtUtils.fillbackOpenidFromToken(token));
                    }
                } catch (JWTVerificationException exception) {
                    return Optional.empty();
                }
            })
            .map(openId -> chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(ICommonConstDefine.CONTEXT_KEY_OPEN_ID, openId)))
            .orElse(Mono.error(new SurveyTokenException()));
    }
}
