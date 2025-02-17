package server.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.ExpenseRepository;
import server.services.ExpenseService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseControllerTest {
    @Inject
    private TestExpenseRepository repo;
    @Inject
    private ExpenseService service;
    @Inject
    private ExpenseController controller;
    private Participant user;
    private Participant user2;
    private Date date;
    private Tag type;
    private Expense expense;
    private Expense expense2;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup(){
        Injector injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
        user = new Participant("user", "mm.@gmail.com",  "dutch", null);
        user2 = new Participant("user2", "mm.@gmail.com",  "english",null);
        date = new Date(2023, Calendar.FEBRUARY, 3);

        type = new Tag("food", "red",1L);
        expense = new Expense(user,  100, "EUR", List.of(user2), "expense", date, type);
        expense2 = new Expense(user2, 200, "EUR", List.of(user), "expense2", date, type);

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
        long id = expense.getExpenseId();
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
        List<Expense> expectedExpenses = List.of(expense, expense2);
        assertEquals(expectedExpenses, controller.getAll());
    }

    /**
     * Test for delete expense
     */
    @Test
    public void testDeleteExpense() {
        long id = expense.getExpenseId();
        ResponseEntity<Void> response = controller.deleteExpense(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(repo.getAllExpenses().isEmpty());
    }
    /**
     * Test for update expense
     */
    @Test
    public void testUpdateExpense() {
        Expense updatedExpense = new Expense(user, 200, "EUR", List.of(user2),
                "Updated expense", date, type);
        long id = expense.getExpenseId();
        ResponseEntity<Expense> response = controller.updateExpense(id, updatedExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated expense", response.getBody().getExpenseName());
        assertEquals(200, response.getBody().getAmount());
    }

    /**
     * Checkstyle for pipeline
     */
    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(ExpenseRepository.class).to(TestExpenseRepository.class);
            bind(ExpenseService.class);
            bind(ExpenseController.class);
        }
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testGetByIdNotFound() {
        long id = 100L;

        ResponseEntity<Expense> response = controller.getById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testAddExpenseInvalid() {

        Expense invalidExpense = new Expense(user, 0,"EUR",

                List.of(user2), "", date, type);

        ResponseEntity<Expense> response = controller.addExpense(invalidExpense);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testDeleteExpenseNotFound() {
        long id = 100L;

        ResponseEntity<Void> response = controller.deleteExpense(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testUpdateExpenseNotFound() {
        long id = 100L; // non-existing id

        Expense updatedExpense = new Expense(user, 200, "EUR", List.of(user2),

                "Updated expense", date, type);

        ResponseEntity<Expense> response = controller.updateExpense(id, updatedExpense);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Checkstyle for pipeline
     */
    @Test
    public void testUpdateExpenseInvalid() {
        long id = expense.getExpenseId();
        Expense invalidExpense = new Expense(user, 0,"EUR",
                List.of(user2), "", date, type);

        ResponseEntity<Expense> response = controller.updateExpense(id, invalidExpense);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
