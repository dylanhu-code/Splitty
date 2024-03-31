package server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import server.services.PasswordService;

@Component
public class PasswordPrinter implements CommandLineRunner {

    private final PasswordService passwordService;
    private String generatedPassword;

    /**
     * Constructor
     * @param passwordService password service
     */
    public PasswordPrinter(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * Prints the admin password on server start
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        generatedPassword = passwordService.getPassword();
        System.out.println("Admin Password: " + generatedPassword);
    }

    /**
     * Getter for the generated password to check for validity later
     * @return the generated password
     */
    public String getGeneratedPassword() {
        return generatedPassword;
    }
}
