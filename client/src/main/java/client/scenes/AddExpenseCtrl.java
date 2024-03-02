package client.scenes;

import commons.Expense;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ExpenseType;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller class for the Add Expense view.
 */
public class AddExpenseCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ChoiceBox<User> whoPaidChoiceBox;

    @FXML
    private ChoiceBox<ExpenseType> expenseTypeChoiceBox;

    @FXML
    private TextField whatFor;

    @FXML
    private TextField howMuch;

    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox participant1;

    @FXML
    private CheckBox participant2;

    /**
     * Constructs an instance of AddExpenseCtrl with the specified dependencies.
     *
     * @param server The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initChoiceBoxes();
        initDate();
    }

    /**
     * Initializes the choice boxes with available options.
     */
    private void initChoiceBoxes() {
        whoPaidChoiceBox.getItems().
                addAll(/* The participants of the specific event. */); //toDO
        expenseTypeChoiceBox.getItems().addAll(ExpenseType.FOOD, ExpenseType.TRANSPORTATION,
                ExpenseType.DRINKS, ExpenseType.OTHER);
    }

    /**
     * Initializes the datepicker with the current date.
     */
    private void initDate() {
        datePicker.getEditor().setEditable(false); // To make the date not changeable.
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Handles the action when the "Abort" button is clicked.
     */
    private void abort() {
        clearFields();
        mainCtrl.showOverview();
    }

    /**
     * Handles the action when the "Add" button is clicked.
     */
    private void add() {
        //toDO, probably something like database.addExpense(getExpense()).
        clearFields();
        mainCtrl.showOverview();
    }

    /**
     * Initializes an Expense instance from the entered values.
     * @return The Expense instance.
     */
    private Expense getExpense() {
        User payor = whoPaidChoiceBox.getValue();
        double amount = Double.parseDouble(howMuch.getText());
        List<User> beneficiaries = new ArrayList<>();
        String expenseName = whatFor.getText();
        Date date = java.util.Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())
                .toInstant()); // Convert JavaFX LocalDate to java.util.Date.
        ExpenseType type = expenseTypeChoiceBox.getValue();

        //toDO, the users should obviously be real participants.
        if (participant1.isSelected()) {
            beneficiaries.add(new User("participant1", "English"));
        }
        if (participant2.isSelected()) {
            beneficiaries.add(new User("participant2", "Dutch"));
        }

        return new Expense(payor, amount, beneficiaries, expenseName, date, type);
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        whatFor.clear();
        howMuch.clear();
        datePicker.getEditor().clear();
        participant1.setSelected(false);
        participant2.setSelected(false);
    }

    /**
     * Handles the action when common keys are pressed.
     *
     * @param k The key instance.
     */
    public void keyPressed(KeyEvent k) {
        switch (k.getCode()) {
            case ENTER:
                add();
                break;
            case ESCAPE:
                abort();
                break;
            default:
                break;
        }
    }
}
