package commons;

import java.util.ArrayList;
import java.util.Objects;
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

    public void editUsername(String username) {
        this.username = username;
    }
    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public Optional<String> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = Optional.of(bankAccount);
    }

    public String getLanguage() {
        return language;
    }

    public void switchLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEvents(), user.getEvents()) && Objects.equals(getBankAccount(), user.getBankAccount()) && Objects.equals(getLanguage(), user.getLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEvents(), getBankAccount(), getLanguage());
    }
}
