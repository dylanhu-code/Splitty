package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.services.PasswordService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PasswordServiceTest {

    @Mock
    private PasswordGenerator passwordGenerator;

    private PasswordService passwordService;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(passwordGenerator.generatePassword(10)).thenReturn("password");
        passwordService = new PasswordService(passwordGenerator);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testPasswordGeneration() {
        assertEquals("password", passwordService.getPassword());
    }
}