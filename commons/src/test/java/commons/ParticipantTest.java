package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    private Participant user;

    @BeforeEach
    void setUp() {
        user = new Participant("user", "mm@gmail.com", "48392020", "ADDF");
    }


    @Test
    void getName() {
        assertEquals("user", user.getName());
    }

    @Test
    void setName() {
        assertNotEquals("mm", user.getName());
        user.setName("mm");
        assertEquals("mm", user.getName());
    }

    @Test
    void getEmail() {
        assertEquals("mm@gmail.com", user.getEmail());
    }

    @Test
    void setEmail() {
    }

    @Test
    void getBankAccount() {
        assertEquals("48392020", user.getBankAccount());
    }

    @Test
    void setBankAccount() {
    }

    @Test
    void getBic() {
        assertEquals("ADDF", user.getBic());
    }

    @Test
    void setBic() {
    }

    @Test
    void testEquals() {
        Participant user2 = new Participant("user", "mm@gmail.com", "48392020", "ADDF");
        Participant user3 = new Participant("user", "mm@gmail.com", "48392020", "ADF");
        assertEquals(user, user2);
        assertNotEquals(user3, user);
    }

    @Test
    void testHashCode() {
        Participant user2 = new Participant("user", "mm@gmail.com", "48392020", "ADDF");
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        Participant user2 = new Participant("user", "mm@gmail.com", "48392020", "ADDF");
        assertEquals("user", user2.toString());
    }
}
