package cn.nbbandxdd.survey.pubserno.generator;

import cn.nbbandxdd.survey.pubserno.service.PubSerNoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <p>公共序列号生成器。用于辅助生成问卷、题目序列号。
 *
 * <ul>
 * <li>依据序列号类型{@code serNoTyp}获取公共序列号，使用{@link #get(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class PubSerNoGenerator implements InitializingBean {

    /**
     * <p>PubSerNoGenerator实例。
     */
    private static PubSerNoGenerator instance;

    /**
     * <p>公共序列号Service。
     */
    private final PubSerNoService pubSerNoService;

    /**
     * <p>构造器。
     *
     * @param pubSerNoService 公共序列号Service
     */
    public PubSerNoGenerator(PubSerNoService pubSerNoService) {

        this.pubSerNoService = pubSerNoService;
    }

    /**
     * <p>获取公共序列号生成器单例。
     *
     * @return 公共序列号生成器单例
     */
    public static PubSerNoGenerator instance() {

        return instance;
    }

    /**
     * afterPropertiesSet
     */
    @Override
    public void afterPropertiesSet() {

        instance = this;
    }

    /**
     * <p>依据序列号类型{@code serNoTyp}获取公共序列号。
     *
     * @param serNoTyp 序列号类型
     * @return 序列号
     */
    public static Mono<String> get(String serNoTyp) {

        return instance().pubSerNoService.getPubSerNo(serNoTyp);
    }
}
