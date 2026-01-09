package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main Spring Boot Application
 * Demonstrates automated PR review with GitHub Copilot
 * Triggers workflow based on code changes
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

/**
 * REST Controller for API endpoints
 * This controller's changes will be analyzed by the Copilot PR review workflow
 */
@RestController
@RequestMapping("/api")
class PrReviewController {

	/**
	 * Health check endpoint
	 * @return application status
	 */
	@GetMapping("/health")
	public HealthResponse health() {
		return new HealthResponse("UP", "Application is running successfully");
	}

	/**
	 * Echo endpoint - returns the input message
	 * @param request contains the message to echo
	 * @return echo response with timestamp
	 */
	@PostMapping("/echo")
	public EchoResponse echo(@RequestBody EchoRequest request) {
		if (request.getMessage() == null || request.getMessage().isEmpty()) {
			throw new IllegalArgumentException("Message cannot be empty");
		}
		return new EchoResponse(request.getMessage(), System.currentTimeMillis());
	}

	/**
	 * Calculate sum of two numbers
	 * Example: /api/calculate/sum?a=5&b=3 returns 8
	 * @param a first operand
	 * @param b second operand
	 * @return sum result
	 */
	@GetMapping("/calculate/sum")
	public CalculationResponse calculateSum(@RequestParam int a, @RequestParam int b) {
		int result = a + b;
		return new CalculationResponse(a, b, result, "sum");
	}

	/**
	 * Calculate product of two numbers
	 * Example: /api/calculate/product?a=5&b=3 returns 15
	 * @param a first operand
	 * @param b second operand
	 * @return product result
	 */
	@GetMapping("/calculate/product")
	public CalculationResponse calculateProduct(@RequestParam int a, @RequestParam int b) {
		int result = a * b;
		return new CalculationResponse(a, b, result, "product");
	}

	/**
	 * Calculate division of two numbers
	 * @param a dividend
	 * @param b divisor
	 * @return division result
	 */
	@GetMapping("/calculate/divide")
	public CalculationResponse calculateDivide(@RequestParam int a, @RequestParam int b) {
		if (b == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		int result = a / b;
		return new CalculationResponse(a, b, result, "divide");
	}
}

/**
 * Response DTO for health checks
 */
class HealthResponse {
	private String status;
	private String message;

	public HealthResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

/**
 * Request DTO for echo endpoint
 */
class EchoRequest {
	private String message;

	public EchoRequest() {
	}

	public EchoRequest(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

/**
 * Response DTO for echo endpoint
 */
class EchoResponse {
	private String message;
	private long timestamp;

	public EchoResponse(String message, long timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}

/**
 * Response DTO for calculation endpoints
 */
class CalculationResponse {
	private int operand1;
	private int operand2;
	private int result;
	private String operation;

	public CalculationResponse(int operand1, int operand2, int result, String operation) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.result = result;
		this.operation = operation;
	}

	public int getOperand1() {
		return operand1;
	}

	public void setOperand1(int operand1) {
		this.operand1 = operand1;
	}

	public int getOperand2() {
		return operand2;
	}

	public void setOperand2(int operand2) {
		this.operand2 = operand2;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
