package cn.nbbandxdd.survey.exam.controller;

import cn.nbbandxdd.survey.exam.controller.vo.ExamInsertVO;
import cn.nbbandxdd.survey.exam.controller.vo.ExamQuesRlnVO;
import cn.nbbandxdd.survey.exam.controller.vo.ExamQuesTypRlnVO;
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
import java.util.stream.Collectors;

@AutoConfigureWebTestClient(timeout = "60000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class ExamControllerTest {

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

        // typCd = 1
        {
            // /exam/insert
            ExamInsertVO voi = new ExamInsertVO();
            voi.setTtl("testexamX");
            voi.setDsc("testdscX");
            voi.setRpetInd("0");
            voi.setBgnTime(1640966400000L);
            voi.setEndTime(4070880000000L);

            ExamVO examVO = webTestClient
                .post()
                .uri("/exam/insert")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(voi), ExamInsertVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExamVO.class).returnResult().getResponseBody();

            // /exam/update
            ExamVO vou = new ExamVO();
            vou.setExamCd(Objects.requireNonNull(examVO).getExamCd());
            vou.setTtl("testexamXX");
            vou.setDsc("testdscXX");
            vou.setRpetInd("0");

            webTestClient
                .post()
                .uri("/exam/update")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vou), ExamVO.class)
                .exchange()
                .expectStatus().isOk();

            // /ques/insert
            List<AnswVO> lisAnswVO = new ArrayList<>();
            for (int i = 0; i < 1; ++i) {

                AnswVO tmp = new AnswVO();
                tmp.setDsc("testanswX" + i);
                tmp.setScre(i);

                lisAnswVO.add(tmp);
            }

            QuesByPronVO voqi = new QuesByPronVO();
            voqi.setDsc("testquesX");
            voqi.setComm("testcommX");
            voqi.setTypCd("1");
            voqi.setAnswList(lisAnswVO);

            webTestClient
                .post()
                .uri("/ques/insert")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(voqi), QuesByPronVO.class)
                .exchange()
                .expectStatus().isOk();

            // /exam/findAvaQues
            ExamVO vofal = new ExamVO();
            vofal.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            List<QuesVO> lisQuesVO = webTestClient
                .post()
                .uri("/exam/findAvaQues?pageNum=1&pageSize=10")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofal), ExamVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<QuesVO>>() {}).returnResult().getResponseBody();

            // /exam/insertQues
            ExamQuesRlnVO voiq = new ExamQuesRlnVO();
            voiq.setExamCd(Objects.requireNonNull(examVO).getExamCd());
            voiq.setQuesCds(Objects.requireNonNull(lisQuesVO).stream().map(QuesVO::getQuesCd).collect(Collectors.toList()));

            webTestClient
                .post()
                .uri("/exam/insertQues")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(voiq), ExamQuesRlnVO.class)
                .exchange()
                .expectStatus().isOk();

            // /exam/findQuesList
            ExamVO vofql = new ExamVO();
            vofql.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/findQuesList?pageNum=1&pageSize=10")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofql), ExamVO.class)
                .exchange()
                .expectStatus().isOk();

            // /exam/findStatus
            ExamVO vofs = new ExamVO();
            vofs.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/findStatus")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofs), ExamVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.status").isEqualTo("to_be_completed");

            // /exam/findDetail
            ExamVO vofd = new ExamVO();
            vofd.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/findDetail")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofd), ExamVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.examCd").isEqualTo(Objects.requireNonNull(examVO).getExamCd())
                    .jsonPath("$.typCd").isNotEmpty()
                    .jsonPath("$.rpetInd").isNotEmpty()
                    .jsonPath("$.cntdwnInd").isNotEmpty()
                    .jsonPath("$.answImmInd").isNotEmpty()
                    .jsonPath("$.ttl").isNotEmpty()
                    .jsonPath("$.dsc").isNotEmpty()
                    .jsonPath("$.bgnTime").isNotEmpty()
                    .jsonPath("$.endTime").isNotEmpty()
                    .jsonPath("$.quesCnt").isNotEmpty()
                    .jsonPath("$.quesTypRlnList").isEmpty();

            // /exam/findToAnsw
            ExamVO vofta1 = new ExamVO();
            vofta1.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/findToAnsw")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofta1), ExamVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.examCd").isEqualTo(Objects.requireNonNull(examVO).getExamCd())
                    .jsonPath("$.typCd").isNotEmpty()
                    .jsonPath("$.rpetInd").isNotEmpty()
                    .jsonPath("$.cntdwnInd").isNotEmpty()
                    .jsonPath("$.answImmInd").isNotEmpty()
                    .jsonPath("$.ttl").isNotEmpty()
                    .jsonPath("$.dsc").isNotEmpty()
                    .jsonPath("$.bgnTime").isNotEmpty()
                    .jsonPath("$.endTime").isNotEmpty();

            ExamVO vofta2 = new ExamVO();
            vofta2.setExamCd("19700101000000000");

            webTestClient
                .post()
                .uri("/exam/findToAnsw")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofta2), ExamVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.examCd").isEqualTo("19700101000000000")
                    .jsonPath("$.typCd").isNotEmpty()
                    .jsonPath("$.rpetInd").isNotEmpty()
                    .jsonPath("$.cntdwnInd").isNotEmpty()
                    .jsonPath("$.answImmInd").isNotEmpty()
                    .jsonPath("$.ttl").isNotEmpty()
                    .jsonPath("$.dsc").isNotEmpty()
                    .jsonPath("$.bgnTime").isNotEmpty()
                    .jsonPath("$.endTime").isNotEmpty()
                    .jsonPath("$.quesList[0].quesCd").isNotEmpty()
                    .jsonPath("$.quesList[0].typCd").isNotEmpty()
                    .jsonPath("$.quesList[0].dsc").isNotEmpty();

            // /exam/findList
            webTestClient
                .post()
                .uri("/exam/findList?pageNum=1&pageSize=10")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .exchange()
                .expectStatus().isOk();

            // /exam/findCommonList
            webTestClient
                .post()
                .uri("/exam/findCommonList?pageNum=1&pageSize=10")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .exchange()
                .expectStatus().isOk();

            // /exam/deleteQues
            ExamQuesRlnVO vodq = new ExamQuesRlnVO();
            vodq.setExamCd(Objects.requireNonNull(examVO).getExamCd());
            vodq.setQuesCds(Objects.requireNonNull(lisQuesVO).stream().map(QuesVO::getQuesCd).collect(Collectors.toList()));

            webTestClient
                .post()
                .uri("/exam/deleteQues")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vodq), ExamQuesRlnVO.class)
                .exchange()
                .expectStatus().isOk();

            // /exam/delete
            ExamVO vod = new ExamVO();
            vod.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/delete")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vod), ExamVO.class)
                .exchange()
                .expectStatus().isOk();
        }

        // typCd = 2
        {
            // /exam/insert
            ExamInsertVO voi = new ExamInsertVO();
            voi.setTtl("testexamX");
            voi.setDsc("testdscX");
            voi.setRpetInd("0");

            // random
            ExamQuesTypRlnVO votr1 = new ExamQuesTypRlnVO();
            votr1.setQuesTypCd("1");
            votr1.setScre(2);
            votr1.setCnt(4);
            ExamQuesTypRlnVO votr2 = new ExamQuesTypRlnVO();
            votr2.setQuesTypCd("2");
            votr2.setScre(4);
            votr2.setCnt(2);
            List<ExamQuesTypRlnVO> listr = new ArrayList<>();
            listr.add(votr1);
            listr.add(votr2);
            voi.setQuesTypRlnList(listr);
            voi.setTypCd("2");

            ExamVO examVO = webTestClient
                .post()
                .uri("/exam/insert")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(voi), ExamInsertVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExamVO.class).returnResult().getResponseBody();

            // /exam/findDetail
            ExamVO vofd = new ExamVO();
            vofd.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/findDetail")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vofd), ExamVO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.examCd").isEqualTo(Objects.requireNonNull(examVO).getExamCd())
                    .jsonPath("$.typCd").isNotEmpty()
                    .jsonPath("$.rpetInd").isNotEmpty()
                    .jsonPath("$.cntdwnInd").isNotEmpty()
                    .jsonPath("$.answImmInd").isNotEmpty()
                    .jsonPath("$.ttl").isNotEmpty()
                    .jsonPath("$.dsc").isNotEmpty()
                    .jsonPath("$.bgnTime").isNotEmpty()
                    .jsonPath("$.endTime").isNotEmpty()
                    .jsonPath("$.quesCnt").isNotEmpty()
                    .jsonPath("$.quesTypRlnList[0].quesTypCd").isNotEmpty()
                    .jsonPath("$.quesTypRlnList[0].scre").isNotEmpty()
                    .jsonPath("$.quesTypRlnList[0].cnt").isNotEmpty();

            // /exam/delete
            ExamVO vod = new ExamVO();
            vod.setExamCd(Objects.requireNonNull(examVO).getExamCd());

            webTestClient
                .post()
                .uri("/exam/delete")
                .header("authorization", "Bearer " + Objects.requireNonNull(loginVO).getToken())
                .body(Mono.just(vod), ExamVO.class)
                .exchange()
                .expectStatus().isOk();
        }
    }
}
