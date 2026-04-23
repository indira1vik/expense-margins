package com.expensemargin.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@SpringBootTest
class ApigatewayApplicationTests {

	@MockBean
	private ReactiveJwtDecoder reactiveJwtDecoder;

	@Test
	void contextLoads() {
	}

}
