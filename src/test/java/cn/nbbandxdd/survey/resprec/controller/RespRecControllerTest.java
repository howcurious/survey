package cn.nbbandxdd.survey.resprec.controller;

import cn.nbbandxdd.survey.exam.controller.vo.ExamVO;
import cn.nbbandxdd.survey.exam.controller.vo.ExamWithQuesVO;
import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
import cn.nbbandxdd.survey.ques.controller.vo.QuesVO;
import cn.nbbandxdd.survey.resprec.controller.vo.DtlRecVO;
import cn.nbbandxdd.survey.resprec.controller.vo.ExamStatVO;
import cn.nbbandxdd.survey.resprec.controller.vo.GrpStatVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class RespRecControllerTest {

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

        // /exam/findList
        ExamVO examVO = Objects.requireNonNull(webTestClient
            .post()
            .uri("/exam/findList?pageNum=1&pageSize=10")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<ExamVO>>() {
            }).returnResult().getResponseBody()).get(0);

        // /exam/findToAnsw
        ExamVO vofta = new ExamVO();
        vofta.setExamCd(Objects.requireNonNull(examVO).getExamCd());

        ExamWithQuesVO examWithQuesVO = webTestClient
            .post()
            .uri("/exam/findToAnsw")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofta), ExamVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ExamWithQuesVO.class).returnResult().getResponseBody();

        // /resprec/insertDtl
        DtlRecVO voi = new DtlRecVO();
        voi.setExamCd(Objects.requireNonNull(examWithQuesVO).getExamCd());

        List<QuesVO> quesVOList = Objects.requireNonNull(examWithQuesVO).getQuesList();
        for (int i = 0; i < quesVOList.size(); ++i) {

            List<String> answList = new ArrayList<>();
            answList.add("00");

            voi.setQuesCd(quesVOList.get(i).getQuesCd());
            voi.setAnswList(answList);
            if (quesVOList.size() - 1 == i) {

                voi.setLastInd("1");
            }

            webTestClient
                .post()
                .uri("/resprec/insertDtl")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(voi), DtlRecVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.examCd").isEqualTo(voi.getExamCd())
                    .jsonPath("$.quesCd").isEqualTo(voi.getQuesCd())
                    .jsonPath("$.answList[0]").isNotEmpty();
        }

        // /resprec/findRespList
        webTestClient
            .post()
            .uri("/resprec/findRespList?pageNum=1&pageSize=10")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].examCd").isNotEmpty()
                .jsonPath("$[0].ttl").isNotEmpty()
                .jsonPath("$[0].scre").isNotEmpty()
                .jsonPath("$[0].spnd").isNotEmpty()
                .jsonPath("$[0].dat").isNotEmpty();

        // /resprec/findUsrRank
        ExamVO vofur = new ExamVO();
        vofur.setExamCd(Objects.requireNonNull(examWithQuesVO).getExamCd());

        webTestClient
            .post()
            .uri("/resprec/findUsrRank")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofur), ExamVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].examCd").isEqualTo(vofur.getExamCd())
                .jsonPath("$[0].dprtNam").isNotEmpty()
                .jsonPath("$[0].grpNam").isNotEmpty()
                .jsonPath("$[0].usrNam").isNotEmpty()
                .jsonPath("$[0].scre").isNotEmpty()
                .jsonPath("$[0].spnd").isNotEmpty()
                .jsonPath("$[0].dat").isNotEmpty();

        // /resprec/findGrpStat
        GrpStatVO vofgs = new GrpStatVO();
        vofgs.setExamCd(Objects.requireNonNull(examWithQuesVO).getExamCd());

        webTestClient
            .post()
            .uri("/resprec/findGrpStat")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofgs), GrpStatVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].examCd").isEqualTo(vofgs.getExamCd())
                .jsonPath("$[0].dprtNam").isNotEmpty()
                .jsonPath("$[0].grpNam").isNotEmpty()
                .jsonPath("$[0].cnt").isNotEmpty()
                .jsonPath("$[0].totCnt").isNotEmpty()
                .jsonPath("$[0].ptpnRate").isNotEmpty()
                .jsonPath("$[0].avgScre").isNotEmpty()
                .jsonPath("$[0].avgSpnd").isNotEmpty();

        // /resprec/findExamStat
        ExamStatVO vofes = new ExamStatVO();
        vofes.setExamCd(Objects.requireNonNull(examWithQuesVO).getExamCd());

        webTestClient
            .post()
            .uri("/resprec/findExamStat")
            .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
            .body(Mono.just(vofes), ExamStatVO.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.examCd").isEqualTo(vofgs.getExamCd())
                .jsonPath("$.ttl").isNotEmpty()
                .jsonPath("$.avgScre").isNotEmpty()
                .jsonPath("$.avgSpnd").isNotEmpty()
                .jsonPath("$.cnt").isNotEmpty()
                .jsonPath("$.cntU40").isNotEmpty()
                .jsonPath("$.rateU40").isNotEmpty()
                .jsonPath("$.cntU70").isNotEmpty()
                .jsonPath("$.rateU70").isNotEmpty()
                .jsonPath("$.cntU100").isNotEmpty()
                .jsonPath("$.rateU100").isNotEmpty()
                .jsonPath("$.cnt100").isNotEmpty()
                .jsonPath("$.rate100").isNotEmpty();
    }
}
