package com.attsw.bookstore.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CategoryWebE2E {

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

    @Test
    void test_CreateNewCategory_ViaWebForm() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Start from home page
            driver.get("http://localhost:" + port + "/");
            
            // Navigate to Categories
            driver.findElement(By.cssSelector("a[href='/categories']")).click();
            
            // Click "New Category"
            driver.findElement(By.cssSelector("a[href='/categories/new']")).click();
            
            // Fill the form
            driver.findElement(By.name("name")).sendKeys("Science Fiction");
            
            // Submit
            driver.findElement(By.name("btn_submit")).click();
            
            // Verify redirect to list page
            assertThat(driver.getCurrentUrl()).contains("/categories");
            
            // Verify category appears in the list
            assertThat(driver.getPageSource()).contains("Science Fiction");
        } finally {
            driver.quit();
        }
    }
    
    @Test
    void test_ListCategories_ShowsAllCategories() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Create test data via REST API
            RestAssured.port = port;
            
            given()
                .contentType("application/json")
                .body("{\"name\":\"Science Fiction\"}")
                .when().post("/api/categories")
                .then().statusCode(201);
            
            given()
                .contentType("application/json")
                .body("{\"name\":\"Programming\"}")
                .when().post("/api/categories")
                .then().statusCode(201);
            
            // Navigate to category list via web UI
            driver.get("http://localhost:" + port + "/");
            driver.findElement(By.cssSelector("a[href='/categories']")).click();
            
            // Verify both categories appear in the list
            String pageSource = driver.getPageSource();
            assertThat(pageSource).contains("Science Fiction");
            assertThat(pageSource).contains("Programming");
            
        } finally {
            driver.quit();
        }
    }
    
    @Test
    void test_EditCategory_ViaWebForm() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Create a category via REST API
            RestAssured.port = port;
            
            Integer categoryId = given()
                .contentType("application/json")
                .body("{\"name\":\"Original Category\"}")
                .when().post("/api/categories")
                .then().statusCode(201)
                .extract().path("id");
            
            // Navigate to edit page
            driver.get("http://localhost:" + port + "/");
            driver.findElement(By.cssSelector("a[href='/categories']")).click();
            driver.findElement(By.cssSelector("a[href='/categories/" + categoryId + "/edit']")).click();
            
            // Update the form field
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys("Updated Category");
            
            // Submit
            driver.findElement(By.name("btn_submit")).click();
            
            // Verify redirect and updated data appears
            assertThat(driver.getCurrentUrl()).contains("/categories");
            assertThat(driver.getPageSource()).contains("Updated Category");
            assertThat(driver.getPageSource()).doesNotContain("Original Category");
            
        } finally {
            driver.quit();
        }
    }
}