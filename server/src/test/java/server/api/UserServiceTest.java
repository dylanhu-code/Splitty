package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.services.UserService;
import commons.Participant;
import server.database.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    /**
     * Checkstyle for pipeline
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetAllUsers() {
        Participant user1 = new Participant();
        Participant user2 = new Participant();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Participant> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetUserById() {
        Participant user = new Participant();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Participant result = userService.getUserById(1L);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1L);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testAddUser() {
        Participant user = new Participant();
        when(userRepository.save(any(Participant.class))).thenReturn(user);

        Participant result = userService.addUser(new Participant());

        assertEquals(user, result);
        verify(userRepository, times(1)).save(any(Participant.class));
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testUpdateUser() {
        Participant user = new Participant();
        when(userRepository.save(any(Participant.class))).thenReturn(user);

        Participant result = userService.updateUser(1L, new Participant());

        assertEquals(user, result);
        verify(userRepository, times(1)).save(any(Participant.class));
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testIsValidEmail() {
        assertTrue(userService.isValidEmail("test@example.com"));
        assertFalse(userService.isValidEmail("invalid_email"));
    }
}