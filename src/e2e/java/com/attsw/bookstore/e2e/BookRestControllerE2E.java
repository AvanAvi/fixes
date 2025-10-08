package com.attsw.bookstore.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
class BookRestControllerE2E {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
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
    void test_CreateNewBook_ShouldReturnCreatedBook() {
        String newBookJson = """
            {
                "title": "Clean Code",
                "author": "Robert C. Martin",
                "isbn": "978-0132350884"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(newBookJson)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("title", equalTo("Clean Code"))
            .body("author", equalTo("Robert C. Martin"))
            .body("isbn", equalTo("978-0132350884"));
    }
    
    @Test
    void test_GetBookById_ShouldReturnExistingBook() {
        // ARRANGE: Create a book first
        String newBookJson = """
            {
                "title": "Refactoring",
                "author": "Martin Fowler",
                "isbn": "978-0201485677"
            }
            """;

        // ACT: Create book and extract its ID
        Integer bookId = given()
            .contentType(ContentType.JSON)
            .body(newBookJson)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // ASSERT: Retrieve the book by ID
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/books/" + bookId)
        .then()
            .statusCode(200)
            .body("id", equalTo(bookId))
            .body("title", equalTo("Refactoring"))
            .body("author", equalTo("Martin Fowler"))
            .body("isbn", equalTo("978-0201485677"));
    }
    
    @Test
    void test_UpdateBook_ShouldModifyExistingBook() {
        // ARRANGE: Create a book first
        Integer bookId = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "title": "Old Title",
                    "author": "Old Author",
                    "isbn": "111-1111111111"
                }
                """)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // ACT: Update the book
        String updatedBookJson = """
            {
                "title": "Updated Title",
                "author": "Updated Author",
                "isbn": "222-2222222222"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updatedBookJson)
        .when()
            .put("/api/books/" + bookId)
        .then()
            .statusCode(200)
            .body("id", equalTo(bookId))
            .body("title", equalTo("Updated Title"))
            .body("author", equalTo("Updated Author"))
            .body("isbn", equalTo("222-2222222222"));

        // ASSERT: Verify persistence by retrieving again
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/books/" + bookId)
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Title"))
            .body("author", equalTo("Updated Author"))
            .body("isbn", equalTo("222-2222222222"));
    }
    
    @Test
    void test_DeleteBook_ShouldRemoveBook() {
        // ARRANGE: Create a book first
        Integer bookId = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "title": "The Great Gatsby",
                    "author": "F. Scott Fitzgerald",
                    "isbn": "978-0743273565"
                }
                """)
        .when()
            .post("/api/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // ACT: Delete the book
        given()
        .when()
            .delete("/api/books/" + bookId)
        .then()
            .statusCode(204); // No Content

        // ASSERT: Verify book no longer exists
       
       
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/books/" + bookId)
        .then()
            .statusCode(200);
            
        
    }
}