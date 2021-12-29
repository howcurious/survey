package cn.nbbandxdd.survey.pubserno.service;

import cn.nbbandxdd.survey.pubserno.repository.PubSerNoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * <p>公共序列号Service。
 *
 * <ul>
 * <li>获取公共序列号，<strong>内部使用接口</strong>，使用{@link #getPubSerNo(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Service
public class PubSerNoService {

    /**
     * <p>公共序列号Repository。
     */
    private final PubSerNoRepository pubSerNoRepository;

    /**
     * <p>构造器。
     *
     * @param pubSerNoRepository 公共序列号Repository
     */
    public PubSerNoService(PubSerNoRepository pubSerNoRepository) {

        this.pubSerNoRepository = pubSerNoRepository;
    }

    /**
     * <p>获取公共序列号，<strong>内部使用接口</strong>，序列号可以循环使用。
     *
     * @param serNoTyp 序列号类型
     * 问卷序列号：{@link cn.nbbandxdd.survey.common.ICommonConstDefine#PUB_SER_NO_EXAM_EXAM_CD}，
     * 题目序列号：{@link cn.nbbandxdd.survey.common.ICommonConstDefine#PUB_SER_NO_QUES_QUES_CD}。
     * @return 当前序列号
     */
    @Transactional
    public Mono<String> getPubSerNo(String serNoTyp) {

        return pubSerNoRepository.findByIdForUpdate(serNoTyp)
            .flatMap(entity -> {

                int bgn = entity.getBgnSerNo();
                int cur = entity.getCurSerNo();
                int next = cur + entity.getStpSprd();
                if (next > entity.getEndSerNo()) {

                    entity.setCurSerNo(bgn);
                } else {

                    entity.setCurSerNo(next);
                }

                return pubSerNoRepository.save(entity)
                    .map(one -> StringUtils.leftPad(String.valueOf(entity.getCurSerNo()), entity.getFmtOutLen(), '0'));
            });
    }
}
