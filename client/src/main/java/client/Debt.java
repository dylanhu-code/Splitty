package client;

import java.util.Objects;

public class Debt {
    private Event event;
    private User user1;
    private User user2;
    private double amount;

    public Debt(Event event, User debtor, User creditor, double amount) {
        this.event = event;
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public Event getEvent() {
        return event;
    }

    public User getDebtor() {
        return user1;
    }

    public User getCreditor() {
        return user2;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Double.compare(debt.amount, amount) == 0 &&
                user1.equals(debt.user1) &&
                user2.equals(debt.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2, amount);
    }

    @Override
    public String toString() {
        return "Debt{" +
                "debtor=" + user1 +
                ", creditor=" + user2 +
                ", amount=" + amount +
                '}';
    }
}