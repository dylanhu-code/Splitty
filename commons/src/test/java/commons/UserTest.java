package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserTest {
    @Test
    void getUsernameTest() {
        User user = new User("user", "english");
        assertEquals("user", user.getUsername());
    }

    @Test
    void editUsernameTest() {
        User user = new User("user", "english");
        user.editUsername("new");
        assertEquals("new", user.getUsername());
    }

    @Test
    void getEventsNullTest() {
        User user = new User("user", "english");
        assertEquals(new ArrayList<Event>(), user.getEvents());
    }
    @Test
    void getEventsTest() {
        //to be done after the event class exists as well
    }

    @Test
    void addEventTest() {
        //to be done after the event class exists as well
    }

    @Test
    void getBankAccountTest() {
        User user = new User("user", "03664748","english");
        assertEquals("03664748", user.getBankAccount());
    }

    @Test
    void setBankAccountTest() {
        User user = new User("user", "03664748","english");
        user.setBankAccount("0745632");
        assertEquals("0745632", user.getBankAccount());
    }

    @Test
    void getLanguageTest() {
        User user = new User("user", "english");
        assertEquals("english", user.getLanguage());
    }

    @Test
    void switchLanguageTest() {
        User user = new User("user", "english");
        user.switchLanguage("dutch");
        assertEquals("dutch", user.getLanguage());
    }

    @Test
    void testNotEquals() {
        User user = new User("user", "english");
        User user2 = new User("user2", "english");
        assertNotEquals(user, user2);
    }
    @Test
    void testEquals() {
        User user = new User("user", "english");
        User user2 = new User("user", "english");
        assertEquals(user, user2);
    }

    @Test
    void testHashCode() {
        User user = new User("user", "english");
        User user2 = new User("user", "english");
        assertEquals(user.hashCode(), user2.hashCode());
    }

}
