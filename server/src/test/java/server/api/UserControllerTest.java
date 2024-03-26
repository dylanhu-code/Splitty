package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import commons.Participant;
import server.services.UserService;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    /**
     * setup
     */
    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    /**
     * test for getAll
     */
    @Test
    public void testGetAllUsers() {
        List<Participant> users = new ArrayList<>();
        users.add(new Participant("user1", "mm.@gmail.com", "03664748", null));
        users.add(new Participant("user2",  "mm.@gmail.com", "12344345", null));

        when(userService.getAllUsers()).thenReturn(users);
        List<Participant> result = userController.getAllUsers();
        assertEquals(users, result);
    }

    /**
     * test for getByID
     */
    @Test
    public void testGetUserById() {
        Long userId = 1L;
        Participant user = new Participant("user1", "mm.@gmail.com", "03664748", null);

        when(userService.getUserById(userId)).thenReturn(user);
        ResponseEntity<Participant> responseEntity = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    /**
     * test for adding a user
     */
    @Test
    public void testCreateUser() {
        Participant user = new Participant("user1","mm.@gmail.com", "03664748", "english");

        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<Participant> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    /**
     * test for updating a user
     */
    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        Participant user = new Participant("user1", "mm.@gmail.com", "03664748", "ADD");

        when(userService.updateUser(userId, user)).thenReturn(user);
        ResponseEntity<Participant> responseEntity = userController.updateUser(userId, user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    /**
     * test for deleting a user
     */
    @Test
    public void testDeleteUser() {
        long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<String> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User with ID " + userId + " deleted successfully", responseEntity.getBody());
    }

}