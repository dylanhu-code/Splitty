package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import server.services.EmailService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    private EmailService emailService;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService();
        emailService.setJavaMailSender(javaMailSender);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetJavaMailSender() {
        JavaMailSender result = emailService.getJavaMailSender("username", "password");

        assertNotNull(result);
    }
}