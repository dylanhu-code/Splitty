package commons;


import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long expenseId;
    @ManyToOne
    @JoinColumn(name = "payor_id")
    private Participant payor;
    private double amount;

    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Participant> beneficiaries;
    private String expenseName;
    private Date date;
    private ExpenseType type;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tag> tags;

    /**
     * Contructor for the Expense class
     * @param payor - user who pays for the expense
     * @param amount - amount of money the expense cost
     * @param beneficiaries - list of users for which the expense was paid for
     * @param expenseName - name of the expense
     * @param date - the date when the expense happened
     * @param type - the type of expense the user made
     */
    public Expense(Participant payor, double amount, List<Participant> beneficiaries,
                   String expenseName, Date date, ExpenseType type) {
        this.payor = payor;
        this.amount = amount;
        this.beneficiaries = beneficiaries;
        this.expenseName = expenseName;
        this.date = date;

    }

    /**
     * Empty constructor
     */
    public Expense() {

    }
    /**
     * Getter for the event tags
     * @return - a set of event tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * setter for tags
     * @param tags - new tags
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Getter for the payor
     * @return - the user who pays for the expense
     */
    public Participant getPayor() {
        return payor;
    }

    /**
     * Getter for the cost of expense
     * @return - a double which represents the cost of the expense
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Getter for the beneficiaries of the expense
     * @return - list of Users for which the expense is paid for
     */
    public List<Participant> getBeneficiaries() {
        return beneficiaries;
    }

    /**
     * Getter for name of Expense
     * @return - a String representing the item or title of expense
     */
    public String getExpenseName() {
        return expenseName;
    }

    /**
     * Getter for the date when the expense was made
     * @return - date of the expense
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter for the type of expense
     * @return - enum of the type of expense
     */
    public ExpenseType getType() {
        return type;
    }

    /**
     * Edits the user who pays for the expense
     * @param payor - updated user who pays for the expense
     */
    public void setPayor(Participant payor) {
        this.payor = payor;
    }

    /**
     * edits the cost of the expense
     * @param amount - new amount to be set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * edits the list of beneficiaries
     * @param beneficiaries - the new list of users to update
     */
    public void setBeneficiaries(List<Participant> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    /**
     * Edits the name of the expense
     * @param expenseName - new name of expense
     */
    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    /**
     * edits the date the expense took place
     * @param date - new date of expense
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * edits the type of expense
     * @param type - new type of expense
     */
    public void setType(ExpenseType type) {
        this.type = type;
    }

    /**
     * getter for expense id
     * @return - the id of the expense
     */
    public long getExpenseId() {
        return expenseId;
    }

    /**
     * setter for expenseId
     * @param expenseId - new expenseId
     */

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }


    /**
     * Checks equality between an Object and an Expense
     * @param o - Object to compare with
     * @return - true iff o is an Expense with same attributes as the comparing Expense
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (Double.compare(amount, expense.amount) != 0) return false;
        if (!Objects.equals(payor, expense.payor)
                || !Objects.equals(beneficiaries, expense.beneficiaries)) return false;
        if (!Objects.equals(expenseName, expense.expenseName)
                || !Objects.equals(date, expense.date)) return false;
        return type == expense.type;
    }

    /**
     * Creates a hashcode for a specific Expense Object
     * @return - an int that uniquely identifies the Expense object
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = payor != null ? payor.hashCode() : 0;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (beneficiaries != null ? beneficiaries.hashCode() : 0);
        result = 31 * result + (expenseName != null ? expenseName.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    /**
     * Turns the object into a human-readable format
     * @return the string representing the object
     */
    @Override
    public String toString() {
        return expenseName +
                ", amount=" + amount +
                ", date=" + date;
    }
}
