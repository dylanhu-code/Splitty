package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
 * Controller class for the Add Expense view.
 */
public class AddExpenseCtrl {

    @FXML
    private ChoiceBox<String> whoPaidChoiceBox;

    @FXML
    private ChoiceBox<String> expenseTypeChoiceBox;


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
}
