package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LinksControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private LinkLister linkLister;

    // Test for /links endpoint
    @Test
    public void links_ValidUrl_ShouldReturnLinks() throws Exception {
        // Mocking the LinkLister.getLinks method
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        Mockito.when(linkLister.getLinks(testUrl)).thenReturn(mockLinks);

        // Sending request to the endpoint
        ResponseEntity<List> response = restTemplate.getForEntity("/links?url=" + testUrl, List.class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("http://example.com/link1", response.getBody().get(0));
        assertEquals("http://example.com/link2", response.getBody().get(1));
    }

    @Test
    public void links_InvalidUrl_ShouldReturnBadRequest() throws Exception {
        // Mocking the LinkLister.getLinks method to throw IOException
        String testUrl = "invalid-url";
        Mockito.when(linkLister.getLinks(testUrl)).thenThrow(new IOException("Invalid URL"));

        // Sending request to the endpoint
        ResponseEntity<String> response = restTemplate.getForEntity("/links?url=" + testUrl, String.class);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // Test for /links-v2 endpoint
    @Test
    public void linksV2_ValidUrl_ShouldReturnLinks() throws Exception {
        // Mocking the LinkLister.getLinksV2 method
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        Mockito.when(linkLister.getLinksV2(testUrl)).thenReturn(mockLinks);

        // Sending request to the endpoint
        ResponseEntity<List> response = restTemplate.getForEntity("/links-v2?url=" + testUrl, List.class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("http://example.com/link1", response.getBody().get(0));
        assertEquals("http://example.com/link2", response.getBody().get(1));
    }

    @Test
    public void linksV2_InvalidUrl_ShouldReturnBadRequest() throws Exception {
        // Mocking the LinkLister.getLinksV2 method to throw BadRequest
        String testUrl = "invalid-url";
        Mockito.when(linkLister.getLinksV2(testUrl)).thenThrow(new BadRequest("Invalid URL"));

        // Sending request to the endpoint
        ResponseEntity<String> response = restTemplate.getForEntity("/links-v2?url=" + testUrl, String.class);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
