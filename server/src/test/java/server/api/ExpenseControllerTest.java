package server.api;

import commons.Expense;
import commons.ExpenseType;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseControllerTest {
    private TestExpenseRepository repo;
    private ExpenseController controller;
    private User user;
    private User user2;
    private Date date;
    private ExpenseType type;
    private Expense expense;
    private Expense expense2;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup(){
        repo = new TestExpenseRepository();
        controller = new ExpenseController(repo);
        user = new User("user", "dutch");
        user2 = new User("user2", "english");
        date = new Date(2023, Calendar.FEBRUARY, 3);
        type = ExpenseType.FOOD;
        expense = new Expense(user,  100, List.of(user2), "expense", date, type);
        expense2 = new Expense(user2, 200, List.of(user), "expense2", date, type);
        controller.addExpense(expense);
    }

    /**
     * Test for get all
     */
    @Test
    public void testGetAll() {
        controller.addExpense(expense2);

        List<Expense> result = controller.getAll();

        assertEquals(2, result.size());
        assertEquals("expense", result.get(0).getExpenseName());
        assertEquals("expense2", result.get(1).getExpenseName());
    }

    /**
     * Test for get by id
     */
    @Test
    public void testGetById() {
        long id = expense.getId();
        ResponseEntity<Expense> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("expense", response.getBody().getExpenseName());
    }

    /**
     * Test for add expense
     */
    @Test
    public void testAddExpense() {
        ResponseEntity<Expense> response = controller.addExpense(expense2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(repo.getAllExpenses().contains(expense2));
    }

    /**
     * Test for delete expense
     */
    @Test
    public void testDeleteExpense() {
        long id = expense.getId();
        ResponseEntity<Void> response = controller.deleteExpense(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(repo.getAllExpenses().isEmpty());
    }
    /**
     * Test for update expense
     */
    @Test
    public void testUpdateExpense() {
        Expense updatedExpense = new Expense(user, 200, List.of(user2), "Updated expense", date, type);
        long id = expense.getId();
        ResponseEntity<Expense> response = controller.updateExpense(id, updatedExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated expense", response.getBody().getExpenseName());
        assertEquals(200, response.getBody().getAmount());
    }

}
