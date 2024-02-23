package commons;

import java.util.*;

/**
 * Event class, used to manage expenses done within a group of people
 */
public class Event {
    private String title;
    private static int staticId = 0;
    private final int id;
    private ArrayList<User> participantList;
    private ArrayList<Debt> debtList;
    private ArrayList<Expense> expenseList;

    public Event(String title) {
        this.title = title;
        participantList = new ArrayList<User>();
        debtList = new ArrayList<Debt>();
        expenseList = new ArrayList<Expense>();
        this.id = staticId++;
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
     * get the id of an event
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * get the static id
     * @return static id
     */
    public int getStaticId() {
        return staticId;
    }
    /**
     * get the list of participants of an event
     * @return participant list
     */
    public ArrayList<User> getParticipants() {
        return participantList;
    }
    /**
     * get the list of debts of an event
     * @return debt list
     */
    public ArrayList<Debt> getDebts() {
        return debtList;
    }
    /**
     * get the list of expenses of an event
     * @return expense list
     */
    public ArrayList<Expense> getExpenses() {
        return expenseList;
    }
    /**
     * set the title of an event
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
