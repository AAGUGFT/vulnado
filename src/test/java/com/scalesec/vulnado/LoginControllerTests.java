package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @Value("${app.secret}")
    private String secret;

    @Test
    public void login_ValidCredentials_ShouldReturnToken() {
        // Arrange
        LoginController loginController = new LoginController();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "validUser";
        loginRequest.password = "validPassword";

        User mockUser = mock(User.class);
        when(mockUser.hashedPassword).thenReturn(Postgres.md5("validPassword"));
        when(mockUser.token(secret)).thenReturn("validToken");
        mockStatic(User.class);
        when(User.fetch("validUser")).thenReturn(mockUser);

        // Act
        LoginResponse response = loginController.login(loginRequest);

        // Assert
        assertNotNull("Token should not be null", response.token);
        assertEquals("Token should match expected value", "validToken", response.token);
    }

    @Test(expected = Unauthorized.class)
    public void login_InvalidCredentials_ShouldThrowUnauthorized() {
        // Arrange
        LoginController loginController = new LoginController();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "validUser";
        loginRequest.password = "invalidPassword";

        User mockUser = mock(User.class);
        when(mockUser.hashedPassword).thenReturn(Postgres.md5("validPassword"));
        mockStatic(User.class);
        when(User.fetch("validUser")).thenReturn(mockUser);

        // Act
        loginController.login(loginRequest);
    }

    @Test(expected = ResponseStatusException.class)
    public void login_NonExistentUser_ShouldThrowUnauthorized() {
        // Arrange
        LoginController loginController = new LoginController();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "nonExistentUser";
        loginRequest.password = "somePassword";

        mockStatic(User.class);
        when(User.fetch("nonExistentUser")).thenReturn(null);

        // Act
        loginController.login(loginRequest);
    }

    @Test
    public void login_NullRequest_ShouldThrowException() {
        // Arrange
        LoginController loginController = new LoginController();

        // Act & Assert
        try {
            loginController.login(null);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue("Exception should be of type IllegalArgumentException", e instanceof IllegalArgumentException);
        }
    }
}
