package commons;

import java.util.ArrayList;
import java.util.Optional;

public class User {
    private String username;
    private List<Event> events;
    private Optional<String> bankAccount;
    private String language;

    public User(String username, String language) {
        this.username = username;
        events = new ArrayList<Event>();
        this.language = language;
        this.bankAccount = Optional.empty();
    }

    public User(String username, String bankAccount, String language) {
        this.username = username;
        events = new ArrayList<Event>();
        this.bankAccount = Optional.of(bankAccount);
        this.language = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
