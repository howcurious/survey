package cn.nbbandxdd.survey.resprec.logic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class RespRecLogicTest {

	@Autowired
	private RespRecLogic respRecLogic;
	
	@Test
	void test() {
		
		assertDoesNotThrow(respRecLogic::sendWeekRpt);
	}
}
