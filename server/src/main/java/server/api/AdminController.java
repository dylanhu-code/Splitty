package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.PasswordPrinter;

@RestController
public class AdminController {

    private final PasswordPrinter passwordPrinter;

    /**
     * Constructs an instance of AdminController with the specified dependencies.
     * @param passwordPrinter The PasswordPrinter instance.
     */
    public AdminController(PasswordPrinter passwordPrinter) {
        this.passwordPrinter = passwordPrinter;
    }

    /**
     * Checks if the entered password is correct.
     * @param enteredPassword The entered password.
     * @return A response entity with the result.
     */
    @PostMapping("/admin/login")
    public ResponseEntity<String> login(@RequestBody String enteredPassword) {
        String generatedPassword = passwordPrinter.getGeneratedPassword();

        if (enteredPassword.equals(generatedPassword)) {
            return ResponseEntity.ok("Access granted");
        } else {
            return ResponseEntity.status(403).body("Access denied");
        }
    }
}
