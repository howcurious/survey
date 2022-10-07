package cn.nbbandxdd.survey.login.controller;

import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient(timeout = "60000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class LoginControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test() {

        LoginVO vol = new LoginVO();
        vol.setCode("testcode");

        webTestClient
            .post()
            .uri("/login")
            .body(Mono.just(vol), LoginVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .jsonPath("$.isRegistered").isEqualTo(true);
    }

    @Test
    public void testAdminLogin() {
        LoginVO loginVO = new LoginVO();
        loginVO.setAdminUserName("admin");
        loginVO.setAdminPassword("666666");
        // 登陆成功
        webTestClient.post().uri("/login").header("x-platform", "admin")
                .body(Mono.just(loginVO), LoginVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.token")
                    .isNotEmpty();

        loginVO.setAdminPassword("aaaaaa");
        // 登陆失败
        webTestClient.post().uri("/login").header("x-platform", "admin")
                .body(Mono.just(loginVO), LoginVO.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}
