package cn.nbbandxdd.survey.grpinfo.controller;

import cn.nbbandxdd.survey.grpinfo.controller.vo.GrpInfoVO;
import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
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
class GrpInfoControllerTest {

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

        // /grpinfo/findDprtList
        webTestClient
            .post()
            .uri("/grpinfo/findDprtList")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk();

        // /grpinfo/findGrpList
        GrpInfoVO vogi = new GrpInfoVO();
        vogi.setDprtNam("testdprt");

        webTestClient
            .post()
            .uri("/grpinfo/findGrpList")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vogi), GrpInfoVO.class)
            .exchange()
            .expectStatus().isOk();
    }
}
