package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Autowired
    private CommentsController commentsController;

    @MockBean
    private User userMock;

    @MockBean
    private Comment commentMock;

    @Value("${app.secret}")
    private String secret;

    @Test
    public void contextLoads() {
    }

    @Test
    public void comments_ShouldReturnListOfComments_WhenAuthenticated() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = Arrays.asList(new Comment(), new Comment());
        doNothing().when(userMock).assertAuth(secret, token);
        when(commentMock.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match mock comments size", mockComments.size(), result.size());
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.comments(token);
    }

    @Test
    public void createComment_ShouldReturnCreatedComment_WhenValidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";
        Comment mockComment = new Comment();
        doNothing().when(userMock).assertAuth(secret, token);
        when(commentMock.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void createComment_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.createComment(token, input);
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentDeletedSuccessfully() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        doNothing().when(userMock).assertAuth(secret, token);
        when(commentMock.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Result should be true", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        String commentId = "123";
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.deleteComment(token, commentId);
    }
}
