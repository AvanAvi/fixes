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
class BookWebE2EE {                                      

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
    void homePageShouldShowBookstoreHeading() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        driver.get("http://localhost:" + port + "/");

        String heading = driver.findElement(By.tagName("h1")).getText();
        assertThat(heading).isEqualTo("Bookstore");

        driver.quit();
    }
    
    
    @Test
    void test_CreateNewBook_ViaWebForm() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Start from home page
            driver.get("http://localhost:" + port + "/");
            
            // Navigate to Books
            driver.findElement(By.cssSelector("a[href='/books']")).click();
            
            // Click "New Book"
            driver.findElement(By.cssSelector("a[href='/books/new']")).click();
            
            // Fill the form
            driver.findElement(By.name("title")).sendKeys("Clean Code");
            driver.findElement(By.name("author")).sendKeys("Robert C. Martin");
            driver.findElement(By.name("isbn")).sendKeys("978-0132350884");
            
            // Submit
            driver.findElement(By.name("btn_submit")).click();
            
            // Verify redirect to list page
            assertThat(driver.getCurrentUrl()).contains("/books");
            
            // Verify book appears in the list
            assertThat(driver.getPageSource()).contains("Clean Code");
            assertThat(driver.getPageSource()).contains("Robert C. Martin");
            assertThat(driver.getPageSource()).contains("978-0132350884");
        } finally {
            driver.quit();
        }
    }
    
    @Test
    void test_ListBooks_ShowsAllBooks() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            RestAssured.port = port;
            
            given()
                .contentType("application/json")
                .body("{\"title\":\"Clean Code\",\"author\":\"Robert C. Martin\",\"isbn\":\"978-0132350884\"}")
                .when().post("/api/books")
                .then().statusCode(201);
            
            given()
                .contentType("application/json")
                .body("{\"title\":\"The Pragmatic Programmer\",\"author\":\"Andrew Hunt\",\"isbn\":\"978-0201616224\"}")
                .when().post("/api/books")
                .then().statusCode(201);
            
            // Navigate to book list via web UI
            driver.get("http://localhost:" + port + "/");
            driver.findElement(By.cssSelector("a[href='/books']")).click();
            
            // Verify both books appear in the list
            String pageSource = driver.getPageSource();
            assertThat(pageSource).contains("Clean Code");
            assertThat(pageSource).contains("Robert C. Martin");
            assertThat(pageSource).contains("978-0132350884");
            
            assertThat(pageSource).contains("The Pragmatic Programmer");
            assertThat(pageSource).contains("Andrew Hunt");
            assertThat(pageSource).contains("978-0201616224");
            
        } finally {
            driver.quit();
        }
    }
    
    @Test
    void test_EditBook_ViaWebForm() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Create a book via REST API
            RestAssured.port = port;
            
            Integer bookId = given()
                .contentType("application/json")
                .body("{\"title\":\"Original Title\",\"author\":\"Original Author\",\"isbn\":\"111-1111111111\"}")
                .when().post("/api/books")
                .then().statusCode(201)
                .extract().path("id");
            
            // Navigate to edit page
            driver.get("http://localhost:" + port + "/");
            driver.findElement(By.cssSelector("a[href='/books']")).click();
            driver.findElement(By.cssSelector("a[href='/books/" + bookId + "/edit']")).click();
            
            // Update the form fields
            driver.findElement(By.name("title")).clear();
            driver.findElement(By.name("title")).sendKeys("Updated Title");
            driver.findElement(By.name("author")).clear();
            driver.findElement(By.name("author")).sendKeys("Updated Author");
            driver.findElement(By.name("isbn")).clear();
            driver.findElement(By.name("isbn")).sendKeys("222-2222222222");
            
            // Submit
            driver.findElement(By.name("btn_submit")).click();
            
            // Verify redirect and updated data appears
            assertThat(driver.getCurrentUrl()).contains("/books");
            assertThat(driver.getPageSource()).contains("Updated Title");
            assertThat(driver.getPageSource()).contains("Updated Author");
            assertThat(driver.getPageSource()).contains("222-2222222222");
            
        } finally {
            driver.quit();
        }
    }
}