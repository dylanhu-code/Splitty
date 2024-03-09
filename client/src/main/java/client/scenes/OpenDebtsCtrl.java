package client.scenes;

import commons.Debt;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
     * Initializes the TitledPanes in the accordion container. Each TitledPane shows an open debt.
     */
    private void initTitledPanes() {
        // Dynamically create TitledPanes and their content based on debtList
        for (Debt debt : debtList) {
            TitledPane titledPane = new TitledPane();
            titledPane.setText(debt.getDebtor().getUsername() + " gives " + debt.getAmount() +
                    "€ to " + debt.getCreditor().getUsername());
            AnchorPane contentPane = new AnchorPane();
            ToggleButton mailButton = new ToggleButton("Mail");
            Button markReceivedButton = new Button("Mark Received");
            ToggleButton bankButton = new ToggleButton("Bank");

            // Set actions for the buttons
            mailButton.setOnAction(event -> handleMailButton(contentPane, debt));
            markReceivedButton.setOnAction(event -> markReceived(debt));
            bankButton.setOnAction(event -> handleBankButton(contentPane));

            // Set the positioning for each button, current values are just a guess
            AnchorPane.setTopAnchor(mailButton, 30.0);
            AnchorPane.setLeftAnchor(mailButton, 10.0);
            AnchorPane.setTopAnchor(markReceivedButton, 30.0);
            AnchorPane.setLeftAnchor(markReceivedButton, 70.0);
            AnchorPane.setTopAnchor(bankButton, 30.0);
            AnchorPane.setLeftAnchor(bankButton, 130.0);

            // Connect the created entities
            contentPane.getChildren().addAll(mailButton, markReceivedButton, bankButton);
            titledPane.setContent(contentPane);
            accordionDebts.getPanes().add(titledPane);
        }
    }

    /**
     * Handles the action when the "Bank" button is clicked.
     *
     * @param contentPane The content of the debt.
     */
    private void handleBankButton(AnchorPane contentPane) {
        // toDO
    }

    /**
     * Handles the action when the "Mark Received" button is clicked.
     *
     * @param debt The open debt.
     */
    private void markReceived(Debt debt) {
        // toDO
    }

    /**
     * Handles the action when the "Mail" button is clicked.
     *
     * @param contentPane The content of the debt.
     * @param debt The open debt.
     */
    private void handleMailButton(AnchorPane contentPane, Debt debt) {
        // Handles different actions based on if the button was toggled on or off at first
        // (by the presence of the "send reminder" button)
        if (contentPane.getChildren().stream().noneMatch(node -> node instanceof Button)) {
            // If no button is present (it was toggled off), add a new button (now toggle on)
            Text emailConfiguredText = new Text("Email configured: ");
            Button sendReminder = new Button("send reminder");
            sendReminder.setOnAction(event -> sendReminder(debt));
            contentPane.getChildren().add(sendReminder);

            // Adjust the positioning of the entities, current values are just a guess
            AnchorPane.setTopAnchor(emailConfiguredText, 10.0);
            AnchorPane.setLeftAnchor(emailConfiguredText, 10.0);
            AnchorPane.setTopAnchor(sendReminder, 30.0);
            AnchorPane.setLeftAnchor(sendReminder, 10.0);

        } else {
            // If a button is present (it was toggled on), remove it (now toggle off)
            contentPane.getChildren().removeIf(node -> node instanceof Button);
        }
    }

    /**
     * Handles the action when the "send reminder" button is clicked.
     *
     * @param debt The open debt.
     */
    private void sendReminder(Debt debt) {
        String reminder = "Dear " + debt.getDebtor().getUsername() + ",\n\n" +
                "This is a friendly reminder regarding the outstanding debt that is currently due.\n\n"
                + "Details of the debt:\n\n" + debt + "We kindly request that you make the necessary " +
                "payment at your earliest convenience.\n\nThank you for your understanding and prompt action."
                + "\n\nBest regards,\n\n" + debt.getCreditor().getUsername();
        // toDo, something like SendMail(debt.getDebtor().getEmail, reminder);
    }
}
