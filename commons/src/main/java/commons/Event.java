package commons;

import java.util.*;


public class Event {
    private String title;
    private String inviteCode;
    private ArrayList<User> participantList;
    private ArrayList<Debt> debtList;
    private ArrayList<Expense> expenseList;

    public Event(String title) {
        this.title = title;
        participantList = new ArrayList<User>();
        debtList = new ArrayList<Debt>();
        expenseList = new ArrayList<Expense>();
    }

    public void addParticipant(User user){
        participantList.add(user);
    }

    public void removeParticipant(User user){
        participantList.remove(user);
    }

    public void addDebt(Debt debt){
        debtList.add(debt);
    }

    public void removeDebt(Debt debt){
        debtList.remove(debt);
    }
    public String getTitle() {
        return title;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public ArrayList<User> getParticipants() {
        return participantList;
    }

    public ArrayList<Debt> getDebts() {
        return debtList;
    }

    public ArrayList<Expense> getExpenses() {
        return expenseList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(title, event.title) &&
            Objects.equals(inviteCode, event.inviteCode) &&
            Objects.equals(participantList, event.participantList) &&
            Objects.equals(debtList, event.debtList) &&
            Objects.equals(expenseList, event.expenseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, inviteCode, participantList, debtList, expenseList);
    }
}
