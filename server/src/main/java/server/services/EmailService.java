package server.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    /**
     * Sends an email with the provided details.
     *
     * @param to       The recipient's email address.
     * @param subject  The subject of the email.
     * @param text     The content of the email.
     * @param username The username for authentication.
     * @param password The password for authentication.
     */
    public void sendEmail(String to, String subject, String text,
                          String username, String password) {
        this.javaMailSender = getJavaMailSender(username, password);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setCc(username);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Failed to send mail");
        }
    }

    /**
     * Configures the JavaMailSender with the provided username and password.
     *
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @return The configured JavaMailSender.
     */
    private JavaMailSender getJavaMailSender(String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}
