package commons;

import java.util.*;

public class User {
    private String username;
    private List<Event> events;
    private Optional<String> bankAccount;
    private String language;

    /**
     *Constructor for a basic user
     * @param username of the user
     * @param language the preferred language
     */
    public User(String username, String language) {
        this.username = username;
        events = new ArrayList<>();
        this.language = language;
        this.bankAccount = Optional.empty();
    }

    /**
     * A constructor for a user with a bank account
     * @param username of the user
     * @param bankAccount the bank account
     * @param language the preferred language
     */
    public User(String username, String bankAccount, String language) {
        this.username = username;
        events = new ArrayList<Event>();
        this.bankAccount = Optional.of(bankAccount);
        this.language = language;
    }

    /**
     *Getter for the username
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     *Setter for the username
     * @param username the new username
     */
    public void editUsername(String username) {
        this.username = username;
    }

    /**
     *Getter for the list of event that the user is a participant of
     * @return the lisyt of events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     *Adds a new event to the list of events the user takes part in
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     *Removes an event from the list of events the user takes part
     * @param event the event to be removed
     */
    public void removeEvent(Event event){
        events.remove(event);
    }

    /**
     * Getter for the bank account of the user
     * @return the bank account
     */
    public Optional<String> getBankAccount() {
        return bankAccount;
    }

    /**
     *Setter for the bank account
     * @param bankAccount the bank account
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = Optional.of(bankAccount);
    }

    /**
     * Getter for the preferred language
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Setter for the language
     * @param language the new language
     */
    public void switchLanguage(String language) {
        this.language = language;
    }

    /**
     * Compares 2 object to check whether the values are equal or not
     * @param o the object to be compared to this
     * @return true whether the attributes have equal values or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getEvents(), user.getEvents()) &&
                Objects.equals(getBankAccount(), user.getBankAccount()) &&
                Objects.equals(getLanguage(), user.getLanguage());
    }

    /**
     * Creates a hashcode value for this user
     * @return the value of the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEvents(), getBankAccount(), getLanguage());
    }

    /**
     * Edits the title of the event the user takes part in
     * @param event the event to be edited
     * @param name the new name
     */
    public void editEventTitle(Event event, String name){
        events.remove(event);
        event.setTitle(name);
        events.add(event);
    }

    /**
     * Adds a new expense from the user to the provided event
     * @param event the event that the expense belongs to
     * @param amount the amount of money paid
     * @param title the title of the expense
     * @param people the people that owe money to the user from this expense
     * @param date the date of the expense
     * @param type the type of the expense
     */
    public void createExpense(Event event, double amount, String title,
                              List<User> people, Date date, ExpenseType type){
        Expense expense = new Expense(this, amount, people, title, date, type);
        event.addExpense(expense);
    }

    /**
     * Removes an expense from the specified event
     * @param event the event that contains the specified expense
     * @param expense the expense to be removed
     */
    public void removeExpense(Event event, Expense expense){
        event.removeExpense(expense);
    }

    /**
     * Changes the amount of money associated with a certain expense
     * @param event the event that contains the expense
     * @param expense the expense to be edited
     * @param amount the new amount
     */
    public void editExpenseAmount(Event event, Expense expense, int amount){
        event.removeExpense(expense);
        expense.setAmount(amount);
        event.addExpense(expense);
    }

    /**
     * Edits the title of the expense
     * @param event that contains the expense
     * @param expense the expense to be edited
     * @param title the new title of the expense
     */
    public void editExpenseTitle(Event event, Expense expense, String title){
        event.removeExpense(expense);
        expense.setExpenseName(title);
        event.addExpense(expense);
    }

    /**
     * Edits the list of people that owe money to this user form the specified expense
     * @param event that contains the expense
     * @param expense the expense
     * @param people the new list of people
     */
    public void editExpenseParticipants(Event event, Expense expense, List<User> people){
        event.removeExpense(expense);
        expense.setBeneficiaries(people);
        event.addExpense(expense);
    }

}
