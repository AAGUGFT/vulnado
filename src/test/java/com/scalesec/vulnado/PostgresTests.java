package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    @Test
    public void connection_ShouldReturnValidConnection() {
        try {
            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should not be closed", connection.isClosed());
        } catch (Exception e) {
            fail("Exception occurred while testing connection: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertSeedData() {
        try {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);

            when(mockConnection.createStatement()).thenReturn(mockStatement);

            Postgres.setup();

            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM users");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM comments");
        } catch (Exception e) {
            fail("Exception occurred while testing setup: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6"; // Precomputed MD5 hash for "test"
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() {
        try {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            Postgres.insertUser("testUser", "testPassword");

            verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
            verify(mockPreparedStatement, times(1)).setString(eq(2), eq("testUser"));
            verify(mockPreparedStatement, times(1)).setString(eq(3), eq(Postgres.md5("testPassword")));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (Exception e) {
            fail("Exception occurred while testing insertUser: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        try {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            Postgres.insertComment("testUser", "testComment");

            verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
            verify(mockPreparedStatement, times(1)).setString(eq(2), eq("testUser"));
            verify(mockPreparedStatement, times(1)).setString(eq(3), eq("testComment"));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (Exception e) {
            fail("Exception occurred while testing insertComment: " + e.getMessage());
        }
    }
}
