package server.api;

import com.google.inject.Inject;
import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;
import server.services.ExpenseService;

import java.util.*;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final EventService eventService;
    private final Queue<Expense> updateQueue = new LinkedList<>();


    /**
     * Constructor for expense controller
     * @param expenseService - service that helps implement CRUD operations
     * @param service - event service that helps implement the Crud operations
     */
    @Inject
    public ExpenseController(ExpenseService expenseService, EventService service){
        this.expenseService = expenseService;
        this.eventService = service;
    }

    /**
     * Get all expenses in the database
     * @return a list with all expenses
     */
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll(){
        return expenseService.getAllExpenses();
    }

    /**
     * Get expense by id
     * @param id - id of the requested expense
     * @return the expense
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") long id){
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expense);
    }

    /**
     * Add a new expense to the database
     * @param expense - expense to be added
     * @return - the added expense
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense){
        try {
//            if (expense.getAmount() <= 0 || expense.getExpenseName().isEmpty()
//                    || expense.getBeneficiaries().isEmpty()){
//                return ResponseEntity.badRequest().build();
//            }
            Expense newExpense = expenseService.addExpense(expense);
            updateQueue.add(newExpense);
            return ResponseEntity.ok(newExpense);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete expense by ID
     * @param id - id of the expense to be deleted
     * @return - the deleted expense
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") long id){
        try {
            if (id < 0 || expenseService.getExpenseById(id) == null) {
                return ResponseEntity.badRequest().build();
            }
            expenseService.deleteExpense(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Long-polling endpoint for getting updates
     * @return - updates when available
     */
    @GetMapping("/updates")
    public ResponseEntity<Expense> getUpdates() {
        synchronized (updateQueue) {
            try {
                if (!updateQueue.isEmpty()) {
                    Expense update = updateQueue.poll();
                    return ResponseEntity.ok(update);
                } else {
                    return ResponseEntity.noContent().build();
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }


    /**
     * Update an expense in the database
     * @param id - id of the expense to be edited
     * @param updatedExpense - the updated expense
     * @return the updated expense
     */
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") long id,
                                                 @RequestBody Expense updatedExpense){
        try {
            if (id < 0 || expenseService.getExpenseById(id)==null) {
                return ResponseEntity.notFound().build();
            }
            if (updatedExpense.getAmount() <= 0 || updatedExpense.getExpenseName().isEmpty()
                    || updatedExpense.getBeneficiaries().isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            Expense updated = expenseService.updateExpense(id, updatedExpense);
            updateQueue.add(updated);
            return ResponseEntity.ok(updated);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
