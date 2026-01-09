package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for the Copilot PR Review Demo Application
 * Tests verify that API endpoints work correctly
 */
@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Test that application context loads successfully
	 */
	@Test
	void contextLoads() {
		// Spring application context should load without errors
	}

	/**
	 * Test health check endpoint
	 * Expected: Returns status UP
	 */
	@Test
	void testHealthEndpoint() throws Exception {
		mockMvc.perform(get("/api/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("UP")))
				.andExpect(jsonPath("$.message", notNullValue()));
	}

	/**
	 * Test echo endpoint with valid message
	 * Expected: Returns the echoed message with timestamp
	 */
	@Test
	void testEchoEndpoint() throws Exception {
		EchoRequest request = new EchoRequest("Hello Copilot");
		
		mockMvc.perform(post("/api/echo")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Hello Copilot")))
				.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	/**
	 * Test echo endpoint with empty message
	 * Expected: Returns 400 Bad Request
	 */
	@Test
	void testEchoEndpointWithEmptyMessage() throws Exception {
		EchoRequest request = new EchoRequest("");
		
		mockMvc.perform(post("/api/echo")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test sum calculation endpoint
	 * Expected: 5 + 3 = 8
	 */
	@Test
	void testSumCalculation() throws Exception {
		mockMvc.perform(get("/api/calculate/sum?a=5&b=3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.operand1", is(5)))
				.andExpect(jsonPath("$.operand2", is(3)))
				.andExpect(jsonPath("$.result", is(8)))
				.andExpect(jsonPath("$.operation", is("sum")));
	}

	/**
	 * Test product calculation endpoint
	 * Expected: 5 * 3 = 15
	 */
	@Test
	void testProductCalculation() throws Exception {
		mockMvc.perform(get("/api/calculate/product?a=5&b=3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.operand1", is(5)))
				.andExpect(jsonPath("$.operand2", is(3)))
				.andExpect(jsonPath("$.result", is(15)))
				.andExpect(jsonPath("$.operation", is("product")));
	}

	/**
	 * Test division calculation endpoint
	 * Expected: 15 / 3 = 5
	 */
	@Test
	void testDivisionCalculation() throws Exception {
		mockMvc.perform(get("/api/calculate/divide?a=15&b=3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.operand1", is(15)))
				.andExpect(jsonPath("$.operand2", is(3)))
				.andExpect(jsonPath("$.result", is(5)))
				.andExpect(jsonPath("$.operation", is("divide")));
	}

	/**
	 * Test division by zero error handling
	 * Expected: Returns 400 Bad Request
	 */
	@Test
	void testDivisionByZeroError() throws Exception {
		mockMvc.perform(get("/api/calculate/divide?a=10&b=0"))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test sum with negative numbers
	 * Expected: -5 + 3 = -2
	 */
	@Test
	void testSumWithNegativeNumbers() throws Exception {
		mockMvc.perform(get("/api/calculate/sum?a=-5&b=3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(-2)));
	}

	/**
	 * Test product with zero
	 * Expected: 5 * 0 = 0
	 */
	@Test
	void testProductWithZero() throws Exception {
		mockMvc.perform(get("/api/calculate/product?a=5&b=0"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(0)));
	}
}
