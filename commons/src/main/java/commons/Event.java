package commons;

import jakarta.persistence.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Event class, used to manage expenses done within a group of people
 */
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventId;
    private String title;
    @ManyToMany(mappedBy = "events", cascade = CascadeType.PERSIST)
    private List<User> participantList;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<Debt> debtList;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<Expense> expenseList;
    private LocalDateTime creationDate;
    private LocalDateTime lastActivity;
    private String inviteCode;


    /**
     * create a new event
     * @param title of the event
     */
    public Event(String title) {
        this.title = title;
        participantList = new ArrayList<User>();
        debtList = new ArrayList<Debt>();
        expenseList = new ArrayList<Expense>();
        inviteCode = null;
    }

    /**
     * Empty Constructor - required for the database connection
     */
    public Event() {
        //for object mappers
    }

    /**
     * add user as participant to the event
     * @param user to add
     */
    public void addParticipant(User user){
        participantList.add(user);
    }
    /**
     * remove user from the event
     * @param user to remove
     */
    public void removeParticipant(User user){
        participantList.remove(user);
    }
    /**
     * add debt to the event
     * @param debt to add
     */
    public void addDebt(Debt debt){
        debtList.add(debt);
    }

    /**
     * remove a debt from the event
     * @param debt to remove
     */
    public void removeDebt(Debt debt){
        debtList.remove(debt);
    }
    /**
     * add expense to the list of expenses of the event
     * @param expense to add
     */
    public void addExpense(Expense expense){
        expenseList.add(expense);
    }
    /**
     * remove expense from the list of expenses of the event
     * @param expense to remove
     */
    public void removeExpense(Expense expense){
        expenseList.remove(expense);
    }

    /**
     * get the title of an event
     * @return title
     */
    public String getTitle() {
        return title;
    }
    /**
     * get the list of participants of an event
     * @return participant list
     */
    public List<User> getParticipants() {
        return participantList;
    }
    /**
     * get the list of debts of an event
     * @return debt list
     */
    public List<Debt> getDebts() {
        return debtList;
    }
    /**
     * get the list of expenses of an event
     * @return expense list
     */
    public List<Expense> getExpenses() {
        return expenseList;
    }

    /**
     * setter for title
     * @param title - new title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * checks whether two events are equal
     * @param o the event
     * @return true iff equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(title, event.title) &&
            Objects.equals(participantList, event.participantList) &&
            Objects.equals(debtList, event.debtList) &&
            Objects.equals(expenseList, event.expenseList) && eventId == event.eventId;
    }

    /**
     * generates a hashcode for an event
     * @return int hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, participantList, debtList, expenseList);
    }

    /**
     * Setter for the creation date
     * @param date - date of creation of event
     */
    public void setCreationdate(LocalDateTime date) {
        creationDate = date;
    }

    /**
     * setter for last activity performed in event
     * @param date - date of last activity
     */
    public void setLastActivity(LocalDateTime date) {
        lastActivity = date;
    }

    /**
     * getter for the creation date
     * @return - the date when the event was created
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * getter for the last activity
     * @return - the date when the event was last active
     */
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    /**
     * Generates an invite code randomly
     */
    public void inviteCodeGeneratorAndSetter() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int codeLength = 8;
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i=0; i<codeLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }
        inviteCode = code.toString();
    }

    /**
     * Getter for the invite code
     * @return - a String representing the invite code
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * gets ID
     * @return - ID
     */
    public long getEventId() {
        return eventId;
    }

    /**
     * sets  ID
     * @param eventId - the new id
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }


    /**
     * Generates a list of debts based on the list of expenses in the event
     * @return - list of debts
     */
    public List<Debt> generateDebts() {
        Map<User, Double> netBalance = getNetBalance();
        List<Debt> debts = new ArrayList<>();
        for (Map.Entry<User, Double> entry : netBalance.entrySet()) {
            User user = entry.getKey();
            double balance = entry.getValue();
            if (balance < 0) {
                for (Map.Entry<User, Double> otherEntry : netBalance.entrySet()) {
                    User otherUser = otherEntry.getKey();
                    double otherBalance = otherEntry.getValue();
                    if (otherBalance > 0 && !user.equals(otherUser)) {
                        double amountToSettle = Math.min(Math.abs(balance), otherBalance);
                        debts.add(new Debt(this, otherUser, user, amountToSettle));
                        balance += amountToSettle;
                        otherBalance -= amountToSettle;
                        if (balance == 0) break;
                    }
                }
            }
        }
        return debts;
    }

    /**
     * Gets the balance of users, after taking into account all event expenses
     * @return - return A map representing net balance where the user is the key
     */
    private Map<User, Double> getNetBalance() {
        Map<User, Double> netBalance = new HashMap<>();
        for (Expense expense : expenseList) {
            User payor = expense.getPayor();
            double amount = expense.getAmount();
            List<User> beneficiaries = expense.getBeneficiaries();
            netBalance.put(payor, netBalance.getOrDefault(payor, 0.0) - amount);
            double beneficiaryShare = amount / beneficiaries.size();
            for (User u : beneficiaries) {
                netBalance.put(u, netBalance.getOrDefault(u, 0.0) + beneficiaryShare);
            }
        }
        return netBalance;
    }
        /**
     * Sets the participant list
     * @param participantList - the list of participants
     */
    public void setParticipantList(List<User> participantList) {
        this.participantList = participantList;
    }

    /**
     * Setter for the debt list
     * @param debtList - the list of debts
     */
    public void setDebtList(List<Debt> debtList) {
        this.debtList = debtList;
    }

    /**
     * Setter for the expenses list
     * @param expenseList - the list of expenses
     */
    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    /**
     * Setter for the creation date
     * @param creationDate - the date the event was created
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Setter for the invite code
     * @param inviteCode - the invite code
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Getter for the list of participants
     * @return the list of users that take part in the event
     */
    public List<User> getParticipantList() {
        return participantList;
    }

    /**
     * Getter for the list of debts
     * @return the list of debts
     */
    public List<Debt> getDebtList() {
        return debtList;
    }

    /**
     * Getter for the list of expenses
     * @return the list of expenses
     */
    public List<Expense> getExpenseList() {
        return expenseList;
    }

    /**
     * Compares 2 events based on the creation date
     * @param o the object to be compared.
     * @return -1 if it is smaller, 0 is equal and 1 if it's bigger
     */
//    @Override
//    public int compareTo(Event o) {
//        return creationDate.compareTo(o.getCreationDate());
//    }
}
