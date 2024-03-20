package server.api;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/events")
public class WebSocketExpenseController {

    private final ExpenseController expenseController;

    /**
     * Constructor with the event controller
     * @param expenseController - the expense controller
     */
    @Autowired
    public WebSocketExpenseController(ExpenseController expenseController) {
        this.expenseController = expenseController;
    }

    /**
     * Add an expense via Websockets
     * @param expense The expense to add
     * @return The added expense
     */
    @MessageMapping("/expense/add")
    @SendTo("/topic/expense/update")
    public Expense addExpense(Expense expense) {
        return expenseController.addExpense(expense).getBody();
    }

    /**
     * Delete expense by ID using Websockets
     *
     * @param id - id of the expense to be deleted
     * @return - ResponseEntity indicating the success or failure of the operation
     */
    @MessageMapping("/expense/delete")
    @SendTo("/topic/expense/update")
    public ResponseEntity<Void> deleteExpense(long id) {
        return expenseController.deleteExpense(id);
    }

    /**
     * Update an expense via Websockets
     * @param id The ID of the expense to update
     * @param updatedExpense The updated expense
     * @return The updated expense
     */
    @MessageMapping("/expense/update")
    @SendTo("/topic/expense/update")
    public Expense updateExpenseViaWebSocket(Long id, Expense updatedExpense) {
        return expenseController.updateExpense(id, updatedExpense).getBody();
    }
}
