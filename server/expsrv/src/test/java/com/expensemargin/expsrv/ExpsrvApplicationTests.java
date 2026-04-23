package com.expensemargin.expsrv;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import com.expensemargin.expsrv.entity.ExpenseCreatedEvent;

@SpringBootTest
class ExpsrvApplicationTests {

	private KafkaTemplate<String, ExpenseCreatedEvent> kafkaTemplate;

	@Test
	void contextLoads() {
	}

}
