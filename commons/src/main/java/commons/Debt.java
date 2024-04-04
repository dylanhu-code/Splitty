package commons;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represents a debt between two users related to a specific event.
 */
@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long debtId;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private Participant user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private Participant user2;
    private double amount;
    private boolean settled;

    /**
     * Constructs a Debt object.
     * @param user1  The user who owes the debt.
     * @param user2  The user to whom the debt is owed.
     * @param amount The amount of the debt.
     */
    public Debt( Participant user1, Participant user2, double amount) {
        this.user1 = user1;
        this.user2 = user2;
        this.amount = amount;
        this.settled = false;
    }

    /**
     * Empty contructor - object mapping
     */
    public Debt() {

    }

    /**
     * getter for the id of debt
     * @return - id of debt
     */
    public long getDebtId() {
        return debtId;
    }

    /**
     * setter for the id of debt
     * @param debtId - new debt id
     */
    public void setDebtId(long debtId) {
        this.debtId = debtId;
    }


    /**
     * Gets the user who owes the debt.
     *
     * @return The user who owes the debt.
     */

    public Participant getUser1() {
        return user1;
    }

    /**
     * Sets the person who owes the debt
     * @param user1 - person
     */
    public void setUser1(Participant user1) {
        this.user1 = user1;
    }

    /**
     * Gets the user to whom the debt is owed.
     *
     * @return The user to whom the debt is owed.
     */

    public Participant getUser2() {
        return user2;
    }

    /**
     * setter for the person to whom the debt is owed
     * @param user2 - person
     */
    public void setUser2(Participant user2) {
        this.user2 = user2;
    }

    /**
     * Gets the amount of the debt.
     *
     * @return The amount of the debt.
     */

    public double getAmount() {
        return amount;
    }

    /**
     * setter for the amount of the debt
     * @param amount - amount - double
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Checks if the debt is settled.
     *
     * @return True if the debt is settled, false otherwise.
     */

    public boolean isSettled() {
        return settled;
    }

    /**
     * Sets the debts
     * @param settled - whether debt is settled or not
     */
    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    /**
     * Pays a certain amount of the debt.
     * @param amountPaid - amount paid
     * @throws IllegalStateException    If the debt is already settled.
     * @throws IllegalArgumentException If the amount paid is negative.
     */
    public void payDebt(double amountPaid) {
        if (settled) {
            throw new IllegalStateException("Debt is already settled");
        }

        if (amountPaid < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative");
        }

        if (amountPaid >= amount) {
            amount = 0;
            setSettled(true);
        } else {
            amount -= amountPaid;
        }
    }

    /**
     * Checks if this debt is equal to another object.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Debt debt = (Debt) o;

        if (debtId != debt.debtId ||
                Double.compare(amount, debt.amount) != 0) return false;
        if (settled != debt.settled) return false;
        if (!Objects.equals(user1, debt.user1)) return false;
        return Objects.equals(user2, debt.user2);
    }
    /**
     * Generates a hash code for this debt.
     *
     * @return The hash code for this debt.
     */

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (debtId ^ (debtId >>> 32));
        result = 31 * result + (user1 != null ? user1.hashCode() : 0);
        result = 31 * result + (user2 != null ? user2.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (settled ? 1 : 0);
        return result;
    }

    /**
     * Returns a string representation of the debt.
     *
     * @return A string representation of the debt.
     */
    @Override
    public String toString() {
        return "Debt{" +
                "debtor=" + user1 +
                ", creditor=" + user2 +
                ", amount=" + amount +
                '}';
    }

    /**
     * Returns a string representation of the debt in HTML.
     *
     * @return A string representation of the debt in HTML.
     */
    public String toStringHtml() {
        return  "debtor: " + user1 +
                "<br>creditor: " + user2 +
                "<br>amount: " + amount ;
    }

}