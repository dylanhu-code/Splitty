package commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("config.txt");
            properties.load(fis);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading email properties from config file", e);
            return;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error closing file input stream", e);
                }
            }
        }
        try {
            this.emailUsername = properties.getProperty("email");
            this.emailPassword = properties.getProperty("password");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error reading email properties", e);
        }
    }


    public String getToRecipient() {
        return toRecipient;
    }

    public void setToRecipient(String toRecipient) {
        this.toRecipient = toRecipient;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
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
