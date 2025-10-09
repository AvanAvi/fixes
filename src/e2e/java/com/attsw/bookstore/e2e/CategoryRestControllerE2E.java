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
	
	@Test
	void test_GetCategoryById_ShouldReturnExistingCategory() {
		// ARRANGE: Create a category first
		String newCategoryJson = """
			{
				"name": "Programming"
			}
			""";

		// ACT: Create category and extract its ID
		Integer categoryId = given()
			.contentType(ContentType.JSON)
			.body(newCategoryJson)
		.when()
			.post("/api/categories")
		.then()
			.statusCode(201)
			.extract()
			.path("id");

		// ASSERT: Retrieve the category by ID
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/api/categories/" + categoryId)
		.then()
			.statusCode(200)
			.body("id", equalTo(categoryId))
			.body("name", equalTo("Programming"));
	}
	
	@Test
	void test_UpdateCategory_ShouldModifyExistingCategory() {
		// ARRANGE: Create a category first
		Integer categoryId = given()
			.contentType(ContentType.JSON)
			.body("""
				{
					"name": "Old Category"
				}
				""")
		.when()
			.post("/api/categories")
		.then()
			.statusCode(201)
			.extract()
			.path("id");

		// ACT: Update the category
		String updatedCategoryJson = """
			{
				"name": "Updated Category"
			}
			""";

		given()
			.contentType(ContentType.JSON)
			.body(updatedCategoryJson)
		.when()
			.put("/api/categories/" + categoryId)
		.then()
			.statusCode(200)
			.body("id", equalTo(categoryId))
			.body("name", equalTo("Updated Category"));

		// ASSERT: Verify persistence by retrieving again
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/api/categories/" + categoryId)
		.then()
			.statusCode(200)
			.body("name", equalTo("Updated Category"));
	}
}