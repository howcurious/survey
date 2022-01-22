package cn.nbbandxdd.survey.ncov.controller;

import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
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
    }
}
