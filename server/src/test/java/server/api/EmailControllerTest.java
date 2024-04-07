package server.api;

import commons.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.EmailService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class EmailControllerTest {

    private EmailController emailController;
    private EmailService emailService;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setUp() {
        emailService = Mockito.mock(EmailService.class);
        emailController = new EmailController(emailService);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testSendEmailSuccess() throws Exception {
        Email email = new Email("to@example.com", "subject", "body");
        email.setEmailUsername("username");
        email.setEmailPassword("password");

        ResponseEntity<?> response = emailController.sendEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(emailService).sendEmail(email.getToRecipient(), email.getEmailSubject(),
                email.getEmailBody(), email.getEmailUsername(), email.getEmailPassword());
    }
}