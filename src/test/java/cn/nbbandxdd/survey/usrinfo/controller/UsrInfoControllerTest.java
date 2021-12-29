package cn.nbbandxdd.survey.usrinfo.controller;

import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
import cn.nbbandxdd.survey.usrinfo.controller.vo.UsrInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class UsrInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test() {

        // /login
        LoginVO vol = new LoginVO();
        vol.setCode("testcodeX");

        LoginVO loginVO = webTestClient
            .post()
            .uri("/login")
            .body(Mono.just(vol), LoginVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LoginVO.class).returnResult().getResponseBody();

        // /usrinfo/insert
        UsrInfoVO voi = new UsrInfoVO();
        voi.setDprtNam("testdprt");
        voi.setGrpNam("testgrp");
        voi.setUsrNam("testusr");

        webTestClient
            .post()
            .uri("/usrinfo/insert")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(voi), UsrInfoVO.class)
            .exchange()
            .expectStatus().isOk();

        // /usrinfo/update
        UsrInfoVO vou = new UsrInfoVO();
        vou.setDprtNam("testdprt");
        vou.setGrpNam("testgrp");
        vou.setUsrNam("testusrX");

        webTestClient
            .post()
            .uri("/usrinfo/update")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vou), UsrInfoVO.class)
            .exchange()
            .expectStatus().isOk();

        // /usrinfo/load
        webTestClient
            .post()
            .uri("/usrinfo/load")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.dprtNam").isEqualTo("testdprt")
                .jsonPath("$.grpNam").isEqualTo("testgrp")
                .jsonPath("$.usrNam").isEqualTo("testusrX");
    }
}
