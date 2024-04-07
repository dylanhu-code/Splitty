package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordGeneratorTest {

    private PasswordGenerator passwordGenerator;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setUp() {
        passwordGenerator = new PasswordGenerator();
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGeneratePassword() {
        int length = 10;
        String password = passwordGenerator.generatePassword(length);

        assertEquals(length, password.length());

        String validCharacters = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";
        for (char c : password.toCharArray()) {
            assertTrue(validCharacters.indexOf(c) != -1);
        }
    }
}
