package server.services;

import com.google.inject.Inject;
import commons.Expense;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    /**
     * Constructor for event service
     * @param rep - the expenseRepository(database)
     */
    @Inject
    public ExpenseService(ExpenseRepository rep) {
        this.repository = rep;
    }

    /**
     * method gets the expenses in the database
     * @return a list of expenses in the repository
     */
    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    /**
     * Gets the expense from database depending on the id
     * @param id - the id of the expense
     * @return - the Expense with the corresponding id, or null if nothing found
     */
    public Expense getExpenseById(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * adds an expense to the database
     * @param expense - expense to add
     * @return - the expense added
     */
    public Expense addExpense(Expense expense) {
        return repository.save(expense);
    }

    /**
     * Deletes an expense from the database
     * @param id - the id of the expense
     */
    public void deleteExpense(long id) {
        repository.deleteById(id);
    }

    /**
     * Updates an expense from the database
     * @param id - id of the expense
     * @param updatedExpense - new expense
     * @return - the new expense that was updated
     */
    public Expense updateExpense(long id, Expense updatedExpense) {
        updatedExpense.setId(id);
        return repository.save(updatedExpense);
    }
}
