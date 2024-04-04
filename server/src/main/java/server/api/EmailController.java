package server.api;

import commons.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService service;

    /**
     * Constructs an EmailController with the specified EmailService.
     *
     * @param emailService The EmailService to be used for sending emails.
     */
    @Autowired
    public EmailController(EmailService emailService) {
        this.service = emailService;
    }

    /**
     * Handles the HTTP POST request to send an email.
     *
     * @param request The email request object containing details of the email to be sent.
     * @return A ResponseEntity containing the sent email object.
     */
    @PostMapping
    public ResponseEntity<?> sendEmail(@RequestBody Email request) {
        try {
            service.sendEmail(request.getToRecipient(), request.getEmailSubject(),
                    request.getEmailBody(), request.getEmailUsername(), request.getEmailPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

}
