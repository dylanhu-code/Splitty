package commons;

import jakarta.persistence.*;

import java.security.SecureRandom;
import java.text.DecimalFormat;
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
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Participant> participantList;

    @OneToMany( cascade = CascadeType.ALL)
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
        participantList = new ArrayList<>();
        expenseList = new ArrayList<>();
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
    public void addParticipant(Participant user){
        participantList.add(user);
    }
    /**
     * remove user from the event
     * @param user to remove
     */
    public void removeParticipant(Participant user){
        participantList.remove(user);
    }

//    public void addTag(Tag tag) {
//        tags.add(tag);
//    }
//    public void removeTag(Tag tag) {
//        tags.remove(tag);
//    }

    /**
     * add expense to the list of expenses of the event
     * @param expense to add
     */
    public void addExpense(Expense expense){
        expenseList.add(expense);
    }

    /**
     * Getter for the event tags
     * @return - a set of event tags
     */
//    public Set<Tag> getTags() {
//        return tags;
//    }
//
//    /**
//     * setter for tags
//     * @param tags - new tags
//     */
//    public void setTags(Set<Tag> tags) {
//        this.tags = tags;
//    }

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
    public List<Participant> getParticipants() {
        return participantList;
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
     * Checks for equality
     * @param o - object to compare
     * @return - true is o is same Event, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (eventId != event.eventId
                ||!Objects.equals(title, event.title)
                ||!Objects.equals(expenseList, event.expenseList)
                ||!Objects.equals(participantList, event.participantList)) return false;
        if (!Objects.equals(creationDate, event.creationDate)) return false;
        if (!Objects.equals(lastActivity, event.lastActivity)) return false;
        return Objects.equals(inviteCode, event.inviteCode);
    }

    /**
     * hashcode function
     * @return - int that uniquely identifies event
     */
    @Override
    public int hashCode() {
        int result = (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (participantList != null ? participantList.hashCode() : 0);
        result = 31 * result + (expenseList != null ? expenseList.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (lastActivity != null ? lastActivity.hashCode() : 0);
        result = 31 * result + (inviteCode != null ? inviteCode.hashCode() : 0);
        return result;
    }

    /**
     * Setter for the creation date
     * @param date - date of creation of event
     */
    public void setCreationDate(LocalDateTime date) {
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
     *
     * @return list of debts
     */
    public List<Debt> generateDebts() {
        Map<Participant, Double> netBalance = getNetBalance();
        List<Debt> debts = new ArrayList<>();
        for (Map.Entry<Participant, Double> entry : netBalance.entrySet()) {
            Participant user = entry.getKey();
            double balance = entry.getValue();
            if (balance < 0) {
                debts.addAll(generateDebtsForUser(user, balance, netBalance));
            }
        }
        return debts;
    }

    /**
     * Generates debts for a specific user based on their
     * net balance and the net balance of other users.
     *
     * @param user       The user for whom to generate debts.
     * @param balance    The net balance of the user.
     * @param netBalance The net balances of all users.
     * @return The list of debts generated for the user.
     */
    private List<Debt> generateDebtsForUser(Participant user,
                                            double balance, Map<Participant, Double> netBalance) {
        List<Debt> debts = new ArrayList<>();
        for (Map.Entry<Participant, Double> otherEntry : netBalance.entrySet()) {
            Participant otherUser = otherEntry.getKey();
            double otherBalance = otherEntry.getValue();
            if (otherBalance > 0 && !user.equals(otherUser)) {
                double amountToSettle = Math.min(Math.abs(balance), otherBalance);
                DecimalFormat df = new DecimalFormat("0.00");
                String formattedAmount = df.format(amountToSettle);
                double res = Double.parseDouble(formattedAmount);
                debts.add(new Debt(otherUser, user, res));
                balance += amountToSettle;
                if (balance == 0) {
                    break;
                }
            }
        }
        return debts;
    }

    /**
     * Gets the balance of users, after taking into account all event expenses
     * @return - return A map representing net balance where the user is the key
     */
    private Map<Participant, Double> getNetBalance() {
        Map<Participant, Double> netBalance = new HashMap<>();
        for (Expense expense : expenseList) {
            Participant payor = expense.getPayor();
            double amount = expense.getAmount();
            List<Participant> beneficiaries = expense.getBeneficiaries();
            netBalance.put(payor, netBalance.getOrDefault(payor, 0.0) - amount);
            double beneficiaryShare = amount / beneficiaries.size();
            for (Participant u : beneficiaries) {
                netBalance.put(u, netBalance.getOrDefault(u, 0.0) + beneficiaryShare);
            }
        }
        return netBalance;
    }
        /**
     * Sets the participant list
     * @param participantList - the list of participants
     */
    public void setParticipants(List<Participant> participantList) {
        this.participantList = participantList;
    }

    /**
     * Setter for the expenses list
     * @param expenseList - the list of expenses
     */
    public void setExpenses(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }


    /**
     * Setter for the invite code
     * @param inviteCode - the invite code
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Turns the object into a human-readable format
     * @return the string representing the object
     */
    @Override
    public String toString() {
        return "Event {" +
                "\n  Title: '" + title + '\'' +
                "\n  Id: " + eventId +
                "\n  Participants: " + participantList +
                "\n  Expenses: " + expenseList +
                "\n  Created: " + creationDate +
                "\n  Last Activity: " + lastActivity +
                "\n  inviteCode: '" + inviteCode + '\'' +
                "\n}";
    }

}
