package commons;

import java.util.ArrayList;

public class Event {
    private String title;
    private String inviteCode;
    private ArrayList<User> participants;
    private ArrayList<Debt> debts;
    private ArrayList<Expense> expenses;

    public Event(String title, String inviteCode, ArrayList<User> participants,
                 ArrayList<Debt> debts,
                 ArrayList<Expense> expenses) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.debts = debts;
        this.expenses = expenses;
    }
}
