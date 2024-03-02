package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.time.LocalDate;

/**
 * Controller class for the Add Expense view.
 */
public class AddExpenseCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ChoiceBox<String> whoPaidChoiceBox;

    @FXML
    private ChoiceBox<String> expenseTypeChoiceBox;

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
     * @param server   The ServerUtils instance.
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

    }

    /**
     * Initializes the choice boxes with available options.
     */
    private void initChoiceBoxes() {
        whoPaidChoiceBox.getItems().addAll("Person A", "Person B", "Person C");
        expenseTypeChoiceBox.getItems().addAll("Type 1", "Type 2", "Type 3");
    }

    /**
     * Initializes the datepicker with the current date.
     */
    private void initDate() {
        datePicker.getEditor().setEditable(false); // To make the date not changeable
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
        // ToDO
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
