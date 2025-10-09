package com.attsw.bookstore.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CategoryRestControllerE2E {

	@Container
	static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
	}

	@Test
	void test_CreateNewCategory_ShouldReturnCreatedCategory() {
		String newCategoryJson = """
			{
				"name": "Science Fiction"
			}
			""";

		given()
			.contentType(ContentType.JSON)
			.body(newCategoryJson)
		.when()
			.post("/api/categories")
		.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("name", equalTo("Science Fiction"));
	}
}