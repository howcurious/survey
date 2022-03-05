package cn.nbbandxdd.survey.ncov.service;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.ncov.repository.NCoVRepository;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVDetailEntity;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVEntity;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVStatEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class NCoVService {

    private final NCoVRepository nCoVRepository;

    public NCoVService(NCoVRepository nCoVRepository) {

        this.nCoVRepository = nCoVRepository;
    }

    public Mono<Void> save(Mono<NCoVEntity> entity) {

        return entity
            .flatMap(one -> Mono.deferContextual(ctx -> {

                one.setOpenId(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID));
                return Mono.just(one);
            }))
            .flatMap(one -> Mono.zip(Mono.just(one), nCoVRepository.findById(one.getOpenId()).defaultIfEmpty(new NCoVEntity()), (a, b) -> {

                a.setLastMantTmstp(LocalDateTime.now());
                a.setNew(StringUtils.isBlank(b.getOpenId()));
                return a;
            }))
            .flatMap(nCoVRepository::save)
            .then();
    }

    public Mono<NCoVEntity> findById() {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .flatMap(nCoVRepository::findById);
    }

    public Flux<NCoVStatEntity> findDprtStat() {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .filterWhen(openId -> nCoVRepository.findCntByOpenIdAndHamCd(openId).map(cnt -> 1 == cnt))
            .flatMapMany(one -> nCoVRepository.findDprtStat());
    }

    public Flux<NCoVStatEntity> findGrpStat(Mono<NCoVStatEntity> entity) {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .filterWhen(openId -> nCoVRepository.findCntByOpenIdAndHamCd(openId).map(cnt -> 1 == cnt))
            .flatMapMany(one -> entity.flatMapMany(en -> nCoVRepository.findGrpStat(en.getDprtNam())));
    }

    public Flux<NCoVDetailEntity> findDetail(Mono<NCoVDetailEntity> entity) {

        return Mono
            .deferContextual(ctx -> Mono.just(ctx.get(ICommonConstDefine.CONTEXT_KEY_OPEN_ID).toString()))
            .filterWhen(openId -> nCoVRepository.findCntByOpenIdAndHamCd(openId).map(cnt -> 1 == cnt))
            .flatMapMany(one -> entity.flatMapMany(en -> nCoVRepository.findDetail(en.getDprtNam(), en.getGrpNam())));
    }
}
