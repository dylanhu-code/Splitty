package commons;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testConstructorNoArgs() {
        Email email = Mockito.spy(Email.class);
        Mockito.doNothing().when(email).loadEmailProperties();
        email.setEmailUsername("username");
        email.setEmailPassword("password");

        assertEquals("username", email.getEmailUsername());
        assertEquals("password", email.getEmailPassword());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testConstructorWithArgs() {
        Email email = Mockito.spy(new Email("recipient", "subject", "body"));
        Mockito.doNothing().when(email).loadEmailProperties();
        email.setEmailUsername("username");
        email.setEmailPassword("password");

        assertEquals("recipient", email.getToRecipient());
        assertEquals("subject", email.getEmailSubject());
        assertEquals("body", email.getEmailBody());
        assertEquals("username", email.getEmailUsername());
        assertEquals("password", email.getEmailPassword());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGettersAndSetters() {
        Email email = new Email();
        email.setToRecipient("newRecipient");
        assertEquals("newRecipient", email.getToRecipient());

        email.setEmailSubject("newSubject");
        assertEquals("newSubject", email.getEmailSubject());

        email.setEmailBody("newBody");
        assertEquals("newBody", email.getEmailBody());

        email.setEmailUsername("newUsername");
        assertEquals("newUsername", email.getEmailUsername());

        email.setEmailPassword("newPassword");
        assertEquals("newPassword", email.getEmailPassword());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testEqualsAndHashCode() {
        Email email1 = new Email("recipient", "subject", "body");
        Email email2 = new Email("recipient", "subject", "body");
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());

        email2.setEmailBody("newBody");
        assertNotEquals(email1, email2);
        assertNotEquals(email1.hashCode(), email2.hashCode());
    }
}