package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Test for getLinks method
    @Test
    public void getLinks_ValidUrl_ShouldReturnLinks() throws IOException {
        // Arrange
        String url = "https://example.com";
        LinkLister linkLister = Mockito.spy(LinkLister.class);

        // Mock Jsoup.connect(url).get() behavior
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        when(mockElement.absUrl("href")).thenReturn("https://example.com/link1");
        when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);
        doReturn(mockDocument).when(linkLister).getDocument(url);

        // Act
        List<String> links = linkLister.getLinks(url);

        // Assert
        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 1", 1, links.size());
        assertEquals("First link should match", "https://example.com/link1", links.get(0));
    }

    @Test(expected = IOException.class)
    public void getLinks_InvalidUrl_ShouldThrowIOException() throws IOException {
        // Arrange
        String url = "invalid-url";
        LinkLister linkLister = Mockito.spy(LinkLister.class);

        // Mock Jsoup.connect(url).get() behavior to throw IOException
        doThrow(new IOException("Invalid URL")).when(linkLister).getDocument(url);

        // Act
        linkLister.getLinks(url);
    }

    // Test for getLinksV2 method
    @Test
    public void getLinksV2_ValidUrl_ShouldReturnLinks() throws BadRequest {
        // Arrange
        String url = "https://example.com";
        LinkLister linkLister = Mockito.spy(LinkLister.class);

        // Mock getLinks behavior
        doReturn(List.of("https://example.com/link1")).when(linkLister).getLinks(url);

        // Act
        List<String> links = linkLister.getLinksV2(url);

        // Assert
        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 1", 1, links.size());
        assertEquals("First link should match", "https://example.com/link1", links.get(0));
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_PrivateIp_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        String url = "http://192.168.1.1";
        LinkLister linkLister = Mockito.spy(LinkLister.class);

        // Act
        linkLister.getLinksV2(url);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_InvalidUrl_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        String url = "invalid-url";
        LinkLister linkLister = Mockito.spy(LinkLister.class);

        // Mock getLinks behavior to throw IOException
        doThrow(new IOException("Invalid URL")).when(linkLister).getLinks(url);

        // Act
        linkLister.getLinksV2(url);
    }

    // Helper method to mock Jsoup.connect(url).get()
    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
