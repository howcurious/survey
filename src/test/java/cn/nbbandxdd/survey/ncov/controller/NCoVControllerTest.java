package cn.nbbandxdd.survey.ncov.controller;

import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
import cn.nbbandxdd.survey.ncov.controller.vo.AdminNCovVO;
import cn.nbbandxdd.survey.ncov.controller.vo.NCoVDetailVO;
import cn.nbbandxdd.survey.ncov.controller.vo.NCoVStatVO;
import cn.nbbandxdd.survey.ncov.controller.vo.NCoVVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@AutoConfigureWebTestClient(timeout = "60000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class NCoVControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test() {

        // /login
        LoginVO vol = new LoginVO();
        vol.setCode("testcode");

        LoginVO loginVO = webTestClient
            .post()
            .uri("/login")
            .body(Mono.just(vol), LoginVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LoginVO.class).returnResult().getResponseBody();

        // /NCoV/save
        NCoVVO vos = new NCoVVO();
        vos.setQ01("q01");
        vos.setQ02("q02");
        vos.setQ03("q03");
        vos.setQ04("q04");
        vos.setQ05("q05");
        vos.setQ06("q06");
        vos.setQ07("q07");
        vos.setQ08("q08");
        vos.setQ09("q09");
        vos.setQ10("q10");
        vos.setQ11("q11");
        vos.setQ12("q12");
        vos.setQ13("q13");
        vos.setQ14("q14");
        vos.setQ15("q15");

        webTestClient
            .post()
            .uri("/NCoV/save")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vos), NCoVVO.class)
            .exchange()
            .expectStatus().isOk();

        // /NCoV/findById
        webTestClient
            .post()
            .uri("/NCoV/findById")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.q01").isEqualTo("q01");

        // /NCoV/save
        vos.setQ01("qU1");

        webTestClient
            .post()
            .uri("/NCoV/save")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vos), NCoVVO.class)
            .exchange()
            .expectStatus().isOk();

        // /NCoV/findById
        webTestClient
            .post()
            .uri("/NCoV/findById")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.q01").isEqualTo("qU1");

        // /NCov/findDprtStat
        webTestClient
            .post()
            .uri("/NCoV/findDprtStat")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk();

        // /NCoV/findGrpStat
        NCoVStatVO vofgs = new NCoVStatVO();
        vofgs.setDprtNam("testdprt");

        webTestClient
            .post()
            .uri("/NCoV/findGrpStat")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofgs), NCoVStatVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].grpNam").isNotEmpty()
                .jsonPath("$[0].onCntI").isNotEmpty()
                .jsonPath("$[0].offCntI").isNotEmpty()
                .jsonPath("$[0].leaveCntI").isNotEmpty()
                .jsonPath("$[0].awayCntI").isNotEmpty()
                .jsonPath("$[0].tempCntI").isNotEmpty()
                .jsonPath("$[0].grayCntI").isNotEmpty()
                .jsonPath("$[0].onCntE").isNotEmpty()
                .jsonPath("$[0].offCntE").isNotEmpty()
                .jsonPath("$[0].leaveCntE").isNotEmpty()
                .jsonPath("$[0].awayCntE").isNotEmpty()
                .jsonPath("$[0].tempCntE").isNotEmpty()
                .jsonPath("$[0].grayCntE").isNotEmpty();

        // /NCoV/findDetail
        NCoVDetailVO vofd = new NCoVDetailVO();
        vofd.setDprtNam("testdprt");
        vofd.setGrpNam("testgrp");

        webTestClient
            .post()
            .uri("/NCoV/findDetail")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofd), NCoVDetailVO.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void testGetUserInfoById() {
        // /login
        LoginVO vo = new LoginVO();
        vo.setAdminUserName("admin");
        vo.setAdminPassword("666666");

        LoginVO loginVO = webTestClient
                .post()
                .uri("/login").header("x-platform", "admin")
                .body(Mono.just(vo), LoginVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginVO.class).returnResult().getResponseBody();

        AdminNCovVO adminNCovVO = new AdminNCovVO();
        adminNCovVO.setUserName("testusr");

        webTestClient
                .post()
                .uri("/NCoV/adminFindByName")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .header("x-platform", "admin")
                .body(Mono.just(adminNCovVO), AdminNCovVO.class)
                .exchange()
                .expectStatus().isOk();

        adminNCovVO.setOpenId("testcode");
        adminNCovVO.setAddr("xxx大街");
        adminNCovVO.setDist("西城区");
        adminNCovVO.setPhoneNo("12345678901");
        webTestClient.post().uri("/NCoV/adminUpdate")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .header("x-platform", "admin")
                .body(Mono.just(adminNCovVO), AdminNCovVO.class)
                .exchange()
                .expectStatus().isOk();

        // TODO 这个为啥是404
//        webTestClient.post().uri("/NCoV/adminFindById")
//                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
//                .header("x-platform", "admin")
//                .body(Mono.just(adminNCovVO), AdminNCovVO.class)
//                .exchange()
//                .expectStatus().isOk();
    }
}
