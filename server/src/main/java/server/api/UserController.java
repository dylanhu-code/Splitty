package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.User;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserRepository repository;

    /**
     * Constructor for the user controller
     *
     * @param repository for the user
     */
    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all users form the database
     *
     * @return a list with all the users
     */
    @GetMapping("/")
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Get an expense by id
     *
     * @param id of the expense
     * @return the user
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        if (id < 0 || !repository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOptional = repository.findById(id);
        return userOptional.map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add a new user to the database
     *
     * @param user to be added
     * @return the added user
     */
    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            if (user.getLanguage().isEmpty() || user.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            User savedUser = repository.save(user);
            return ResponseEntity.ok(savedUser);

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
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            if (user.getLanguage().isEmpty() || user.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Optional<User> userOptional = repository.findById(id);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();

                existingUser.setUsername(user.getUsername());
                existingUser.setBankAccount(user.getBankAccount());
                existingUser.switchLanguage(user.getLanguage());


                User savedUser = repository.save(existingUser);
                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete user by ID
     * @param id the ID of the user
     * @return the deleted user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            if (id < 0 || !repository.existsById(id)) {
                return ResponseEntity.badRequest().build();
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
