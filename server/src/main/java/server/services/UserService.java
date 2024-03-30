package server.services;

import commons.Participant;
import com.google.inject.Inject;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<Participant> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Finds in the database user by ID
     *
     * @param id of the user
     * @return the User
     */
    public Participant getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Adds an user to the database
     *
     * @param user the user to be added
     * @return the added user
     */
    public Participant addUser(Participant user) {
        return repository.save(user);
    }

    /**
     * Update user by ID
     *
     * @param id   of user
     * @param user user updates
     * @return the updated user
     */
    public Participant updateUser(Long id, Participant user) {
        user.setUserId(id);
        return repository.save(user);
    }

    /**
     * Delete a user by ID
     *
     * @param id of the user
     */
    public void deleteUser(long id) {
        repository.deleteById(id);
    }

    /**
     * Method that checks that the email inputed follows the correct format
     * @param email - email provided by the user
     * @return - true if the email is valid
     */
    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)" +
                "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        boolean valid = matcher.matches();
        return valid;
    }

}
