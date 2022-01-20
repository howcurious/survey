package cn.nbbandxdd.survey.ques.controller;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.exam.controller.vo.ExamVO;
import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
import cn.nbbandxdd.survey.ques.controller.vo.AnswVO;
import cn.nbbandxdd.survey.ques.controller.vo.QuesByPronVO;
import cn.nbbandxdd.survey.ques.controller.vo.QuesVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AutoConfigureWebTestClient(timeout = "60000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class QuesControllerTest {

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

        // /ques/insert
        List<AnswVO> lisAnswVO = new ArrayList<>();
        for (int i = 0; i < 1; ++i) {

            AnswVO tmp = new AnswVO();
            tmp.setDsc("testanswX1" + i);
            tmp.setScre(i);

            lisAnswVO.add(tmp);
        }

        QuesByPronVO voi1 = new QuesByPronVO();
        voi1.setDsc("testquesX1");
        voi1.setComm("testcommX1");
        voi1.setTypCd(ICommonConstDefine.QUES_TYP_CD_SINGLE_CHOICE);
        voi1.setAnswList(lisAnswVO);

        QuesVO quesVO = webTestClient
            .post()
            .uri("/ques/insert")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(voi1), QuesByPronVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(QuesVO.class).returnResult().getResponseBody();

        QuesByPronVO voi3 = new QuesByPronVO();
        voi3.setDsc("testquesX3");
        voi3.setComm("testcommX3");
        voi3.setTypCd(ICommonConstDefine.QUES_TYP_CD_SHORT_ANSWER);

        webTestClient
            .post()
            .uri("/ques/insert")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(voi3), QuesByPronVO.class)
            .exchange()
            .expectStatus().isOk();

        // /ques/update
        List<AnswVO> lisAnswVOu = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {

            AnswVO tmp = new AnswVO();
            tmp.setDsc("testanswX2" + i);
            tmp.setScre(i);

            lisAnswVOu.add(tmp);
        }

        QuesByPronVO vou = new QuesByPronVO();
        vou.setQuesCd(Objects.requireNonNull(quesVO).getQuesCd());
        vou.setDsc("testquesX2");
        vou.setComm("testcommX2");
        vou.setTypCd(ICommonConstDefine.QUES_TYP_CD_MULTIPLE_CHOICE);
        vou.setAnswList(lisAnswVOu);

        webTestClient
            .post()
            .uri("/ques/update")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vou), QuesByPronVO.class)
            .exchange()
            .expectStatus().isOk();

        // /exam/findList
        ExamVO examVO = Objects.requireNonNull(webTestClient
            .post()
            .uri("/exam/findList?pageNum=1&pageSize=10")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<ExamVO>>() {
            }).returnResult().getResponseBody()).get(0);

        // /exam/findQuesList
        ExamVO vofql = new ExamVO();
        vofql.setExamCd(Objects.requireNonNull(examVO).getExamCd());

        QuesVO quesVO1 = Objects.requireNonNull(webTestClient
            .post()
            .uri("/exam/findQuesList?pageNum=1&pageSize=10")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofql), ExamVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<QuesVO>>() {
            }).returnResult().getResponseBody()).get(0);

        // /ques/findByPron
        QuesVO vofbp = new QuesVO();
        vofbp.setQuesCd(Objects.requireNonNull(quesVO1).getQuesCd());

        webTestClient
            .post()
            .uri("/ques/findByPron")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofbp), QuesVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.quesCd").isEqualTo(Objects.requireNonNull(quesVO1).getQuesCd())
                .jsonPath("$.typCd").isNotEmpty()
                .jsonPath("$.dsc").isNotEmpty()
                .jsonPath("$.answList[0].answCd").isNotEmpty()
                .jsonPath("$.answList[0].dsc").isNotEmpty()
                .jsonPath("$.answList[0].scre").isNotEmpty();

        // /ques/findByResp
        QuesVO vofbr = new QuesVO();
        vofbr.setQuesCd(Objects.requireNonNull(quesVO1).getQuesCd());

        webTestClient
            .post()
            .uri("/ques/findByResp")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofbr), QuesVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.quesCd").isEqualTo(Objects.requireNonNull(quesVO1).getQuesCd())
                .jsonPath("$.typCd").isNotEmpty()
                .jsonPath("$.dsc").isNotEmpty()
                .jsonPath("$.answList[0].answCd").isNotEmpty()
                .jsonPath("$.answList[0].dsc").isNotEmpty()
                .jsonPath("$.answList[0].scre").isEqualTo(0);

        // /ques/findByExp
        ExamVO vofbe = new ExamVO();
        vofbe.setExamCd(Objects.requireNonNull(examVO).getExamCd());

        webTestClient
            .post()
            .uri("/ques/findByExp")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofbe), ExamVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].quesCd").isEqualTo(Objects.requireNonNull(quesVO1).getQuesCd())
                .jsonPath("$[0].typCd").isNotEmpty()
                .jsonPath("$[0].dsc").isNotEmpty()
                .jsonPath("$[0].answList[0].answCd").isNotEmpty()
                .jsonPath("$[0].answList[0].dsc").isNotEmpty()
                .jsonPath("$[0].answList[0].scre").isNotEmpty()
                .jsonPath("$[0].selList[0]").isNotEmpty();

        // /ques/findList
        webTestClient
            .post()
            .uri("/ques/findList?pageNum=1&pageSize=10")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].quesCd").isNotEmpty()
                .jsonPath("$[0].typCd").isNotEmpty()
                .jsonPath("$[0].dsc").isNotEmpty();

        // /ques/findCommonList
        webTestClient
            .post()
            .uri("/ques/findCommonList?pageNum=1&pageSize=10")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk().expectBody()
                .jsonPath("$[0].quesCd").isNotEmpty()
                .jsonPath("$[0].typCd").isNotEmpty()
                .jsonPath("$[0].dsc").isNotEmpty();

        // /ques/delete
        QuesByPronVO vod = new QuesByPronVO();
        vod.setQuesCd(Objects.requireNonNull(quesVO).getQuesCd());

        webTestClient
            .post()
            .uri("/ques/delete")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vod), QuesByPronVO.class)
            .exchange()
            .expectStatus().isOk();
    }
}