package com.sinignaci.edgeservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EdgeServiceApplicationTests {

	@Autowired
	ApplicationContext context;

	@Test
	void contextLoads() {

		assertTrue(Objects.nonNull(context.getBean("edgeServiceApplication", EdgeServiceApplication.class)));
	}
}
