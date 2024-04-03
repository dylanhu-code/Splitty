package commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * Represents a custom email request with properties loaded from a configuration file.
 */
public class Email {

    private String toRecipient;
    private String emailSubject;
    private String emailBody;
    private String emailUsername;
    private String emailPassword;

    /**
     * Constructs a new Email and loads properties from the configuration file.
     */
    public Email() {
        loadEmailProperties();
    }

    /**
     * Constructs a new Email with specified recipient, subject, and body.
     * @param toRecipient The recipient email address.
     * @param emailSubject The subject of the email.
     * @param emailBody The body content of the email.
     */
    public Email(String toRecipient, String emailSubject, String emailBody) {
        this.toRecipient = toRecipient;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        loadEmailProperties();
    }

    /**
     * Loads email properties from the configuration file.
     */
    private void loadEmailProperties() {
        Properties emailProperties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    "config.txt");
            emailProperties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return;
        }
        try {
            this.emailUsername = emailProperties.getProperty("email.username");
            this.emailPassword = emailProperties.getProperty("email.password");
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Checks whether this Email is equal to another object.
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email request = (Email) o;

        if (!Objects.equals(toRecipient, request.toRecipient)
                || !Objects.equals(emailSubject, request.emailSubject)) return false;
        if (!Objects.equals(emailBody, request.emailBody)) return false;
        if (!Objects.equals(emailUsername, request.emailUsername)) return false;
        return Objects.equals(emailPassword, request.emailPassword);
    }

    /**
     * Returns the hash code value for this Email.
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        int result = toRecipient != null ? toRecipient.hashCode() : 0;
        result = 31 * result + (emailSubject != null ? emailSubject.hashCode() : 0);
        result = 31 * result + (emailBody != null ? emailBody.hashCode() : 0);
        result = 31 * result + (emailUsername != null ? emailUsername.hashCode() : 0);
        result = 31 * result + (emailPassword != null ? emailPassword.hashCode() : 0);
        return result;
    }
}
