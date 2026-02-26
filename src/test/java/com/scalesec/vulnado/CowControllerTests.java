package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CowControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    // Test default input value
    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay", String.class);
        assertEquals("Expected default message from cowsay", Cowsay.run("I love Linux!"), response.getBody());
    }

    // Test custom input value
    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() {
        String customMessage = "Hello, World!";
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=" + customMessage, String.class);
        assertEquals("Expected custom message from cowsay", Cowsay.run(customMessage), response.getBody());
    }

    // Test empty input value
    @Test
    public void cowsay_EmptyInput_ShouldReturnDefaultMessage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=", String.class);
        assertEquals("Expected default message from cowsay when input is empty", Cowsay.run("I love Linux!"), response.getBody());
    }
}
