package server.api;

import com.google.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Participant;
import org.springframework.web.bind.annotation.*;
import server.services.UserService;

import java.util.List;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService service;

    /**
     * Constructor for the user controller
     *
     * @param service for the user
     */
    @Inject
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Get all users form the database
     *
     * @return a list with all the users
     */
    @GetMapping("/")
    public List<Participant> getAllUsers() {
        return service.getAllUsers();
    }

    /**
     * Get an expense by id
     *
     * @param id of the expense
     * @return the user
     */
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getUserById(@PathVariable Long id) {
        try {
            Participant createdUser = service.getUserById(id);
            if (createdUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add a new user to the database
     *
     * @param user to be added
     * @return the added user
     */
    @PostMapping("/")
    public ResponseEntity<Participant> createUser(@RequestBody Participant user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty()||
                    !service.isValidEmail(user.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            Participant createdUser = service.addUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a user by ID
     *
     * @param id   the ID of the user
     * @param user the updated details of the user
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateUser(@PathVariable Long id,
                                                  @RequestBody Participant user) {
        try {
            if (user.getName().isEmpty() || user.getEmail().isEmpty()||
                    !service.isValidEmail(user.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            Participant updatedUser = service.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete user by ID
     *
     * @param id the ID of the user
     * @return the deleted user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            service.deleteUser(id);
            return ResponseEntity.ok("User with ID " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
