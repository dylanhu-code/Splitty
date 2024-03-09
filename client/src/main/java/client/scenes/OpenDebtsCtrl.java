package client.scenes;

import commons.Debt;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class OpenDebtsCtrl {
    private final SplittyMainCtrl mainCtrl;
    private ArrayList<Debt> debtList;
    private Event event;
    private Stage primaryStage;
    private Pair<AddExpenseCtrl, Parent> overview;

    @FXML
    private Label noDebtMessage;

    @FXML
    private Accordion accordionDebts;

    /**
     * Constructs an instance of OpenDebtsCtrl with the specified dependencies.
     *
     * @param mainCtrl The MainCtrl instance.
     */
    public OpenDebtsCtrl(SplittyMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        debtList = new ArrayList<>();
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param overview     The page with its controller.
     * @param event        The event.
     */

    public void initialize(Stage primaryStage, Pair<AddExpenseCtrl, Parent> overview, Event event) {
        this.primaryStage = primaryStage;
        this.overview = overview;
        this.event = event;

        noDebtMessage.setVisible(false);
        if (debtList.isEmpty()) {
            noDebtMessage.setVisible(true);
        } else {
            initTitledPanes();
        }
    }

    /**
     * Initializes the titled-panes in the accordion container. Each titled-pane shows an open debt.
     */
    private void initTitledPanes() {
        // Dynamically create TitledPanes based on debtList
    }

    /**
     * Handles the action when the "Mark Received" button is clicked.
     */
    private void markReceived() {
        // toDO
    }
}
