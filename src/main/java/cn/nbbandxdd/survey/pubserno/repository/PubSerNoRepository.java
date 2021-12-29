package cn.nbbandxdd.survey.pubserno.repository;

import cn.nbbandxdd.survey.pubserno.repository.entity.PubSerNoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * <p>公共序列号Repository。
 *
 * <ul>
 * <li>依据序列号类型{@code serNoTyp}查询记录，并为该记录加Update锁，使用{@link #findByIdForUpdate(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface PubSerNoRepository extends ReactiveCrudRepository<PubSerNoEntity, String> {

    /**
     * <p>依据序列号类型{@code serNoTyp}查询记录，并为该记录加Update锁。
     *
     * @param serNoTyp 序列号类型
     * @return 公共序列号Entity
     */
    @Query("SELECT * FROM PUB_SER_NO WHERE SER_NO_TYP = :serNoTyp FOR UPDATE")
    Mono<PubSerNoEntity> findByIdForUpdate(String serNoTyp);
}
