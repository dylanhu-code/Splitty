package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import commons.User;
import server.services.UserService;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("user1", "03664748", "english"));
        users.add(new User("user2", "12344345", "dutch"));

        when(userService.getAllUsers()).thenReturn(users);
        List<User> result = userController.getAllUsers();
        assertEquals(users, result);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User("user1", "03664748", "english");

        when(userService.getUserById(userId)).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void testCreateUser() {
        User user = new User("user1", "03664748", "english");

        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.createUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        User user = new User("user1", "03664748", "english");

        when(userService.updateUser(userId, user)).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.updateUser(userId, user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void testDeleteUser() {
        long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<String> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User with ID " + userId + " deleted successfully", responseEntity.getBody());
    }
}