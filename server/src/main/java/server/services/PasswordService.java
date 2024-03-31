package server.services;

import org.springframework.stereotype.Service;
import server.api.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PasswordService {
    private final String generatedPassword;

    /**
     * Automatically creates a new password
     * @param passwordGenerator the class where we have the generating method
     */
    @Autowired
    public PasswordService(PasswordGenerator passwordGenerator) {
        this.generatedPassword = passwordGenerator.generatePassword(10);
    }

    /**
     * Getter for the randomly generated password
     * @return the password String
     */
    public String getPassword() {
        return generatedPassword;
    }
}
