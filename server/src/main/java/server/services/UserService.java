package server.services;

import commons.User;
import com.google.inject.Inject;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    /**
     * Constructor for the service class
     *
     * @param repository for the user
     */
    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all users from the database
     *
     * @return list of the users
     */
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Finds in the database user by ID
     *
     * @param id of the user
     * @return the User
     */
    public User getUserById(Long id) {
        if (id < 0 || !repository.existsById(id)) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        Optional<User> userOptional = repository.findById(id);
        return userOptional.orElse(null);
    }

    /**
     * Adds an user to the database
     *
     * @param user the user to be added
     * @return the added user
     */
    public User addUser(User user) {
        if (user.getUsername().isEmpty() || user.getLanguage().isEmpty()) {
            throw new IllegalArgumentException("Not enough information to add the user");
        }
        repository.save(user);
        return user;
    }

    /**
     * Update user by ID
     *
     * @param id   of user
     * @param user user updates
     * @return the updated user
     */
    public User updateUser(Long id, User user) {
        if (id < 0 || !repository.existsById(id) ||
                user.getUsername().isEmpty() || user.getLanguage().isEmpty()) {
            throw new IllegalArgumentException("Invalid ID or empty info");
        }
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isPresent()) {
            User newUser = optionalUser.get();

            newUser.setUsername(user.getUsername());
            newUser.switchLanguage(user.getLanguage());
            newUser.setBankAccount(user.getBankAccount());

            repository.save(newUser);
            return newUser;
        } else throw new IllegalArgumentException("Couldn't update the user");
    }

    /**
     * Delete a user by ID
     *
     * @param id of the user
     */
    public void deleteUser(long id) {
        if (id < 0 || !repository.existsById(id)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        repository.deleteById(id);
    }
}
