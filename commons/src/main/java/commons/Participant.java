package commons;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long userId;
    private String name;
    private String email;
    private String bankAccount;
    private String bic;

    /**
     * Empty constructor - for object mapping
     */
    public Participant() {
    }

    /**
     * Contructor for a participant
     * @param username - name of participant
     * @param email - email of participant
     * @param bankAccount - bankaccount of participant
     * @param bic - bic of bankaccount
     */
    public Participant(String username, String email, String bankAccount, String bic) {
        this.name = username;
        this.email = email;
        this.bankAccount = bankAccount;
        this.bic = bic;
    }

    /**
     * Getter for user id
     * @return - id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Setter for user id
     * @param userId - the new user id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Getter for hte participants name
     * @return - String representing the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the participants name
     * @param username - name of the participant
     */
    public void setName(String username) {
        this.name = username;
    }

    /**
     * Getter for the email
     * @return - email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter for the email
     * @param email - new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the bank account
     * @return - bankaccount
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * setter for hte bank account
     * @param bankAccount - bank account
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * bic of bank account
     * @return - bic
     */
    public String getBic() {
        return bic;
    }

    /**
     * setter for bic
     * @param bic - new bic
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * checks for equality
     * @param o - object to compare
     * @return - true if the same Participant, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant user = (Participant) o;

        if (userId != user.userId) return false;
        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(bankAccount, user.bankAccount)) return false;
        return Objects.equals(bic, user.bic);
    }

    /**
     * hashcode method
     * @return - int uniquely representing the participant
     */
    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
        result = 31 * result + (bic != null ? bic.hashCode() : 0);
        return result;
    }
}
