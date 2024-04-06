package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.PasswordPrinter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AdminControllerTest {

    private AdminController adminController;
    private PasswordPrinter passwordPrinter;

    @BeforeEach
    public void setUp() {
        passwordPrinter = Mockito.mock(PasswordPrinter.class);
        adminController = new AdminController(passwordPrinter);
    }

    @Test
    public void testLoginCorrectPassword() {
        String correctPassword = "correctPassword";
        when(passwordPrinter.getGeneratedPassword()).thenReturn(correctPassword);

        ResponseEntity<String> response = adminController.login(correctPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Access granted", response.getBody());
    }

    @Test
    public void testLoginIncorrectPassword() {
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";
        when(passwordPrinter.getGeneratedPassword()).thenReturn(correctPassword);

        ResponseEntity<String> response = adminController.login(incorrectPassword);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody());
    }
}