package client.scenes;

import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class OpenDebtsCtrl {
    private final SplittyMainCtrl mainCtrl;

    private ArrayList<Debt> debtList;
    private Event event;
    private Stage primaryStage;
    private Scene openDebts;
    private final ServerUtils server;

    @FXML
    private Label noDebtMessage;
    @FXML
    private Accordion accordionDebts;
    @FXML
    public Button abortDebtsButton;

    /**
     * Constructs an instance of OpenDebtsCtrl with the specified dependencies.
     *
     * @param mainCtrl The MainCtrl instance.
     * @param server The ServerUtils instance
     */
    @Inject
    public OpenDebtsCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        debtList = new ArrayList<>();

    }

    /**
     * Initializes the page.
     *
     * @param primaryStage The primary container of this page.
     * @param openDebts    The page with its controller.
     * @param event        The event.
     */
    public void initialize(Stage primaryStage, Scene openDebts, Event event) {
        this.primaryStage = primaryStage;
        this.openDebts = openDebts;
        this.event = event;

        primaryStage.setScene(openDebts);
        primaryStage.show();

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
            titledPane.setText(debt.getDebtor().getUsername() + " gives " + //amount + // toDo
                    "€ to " + debt.getCreditor().getUsername());
            AnchorPane contentPane = new AnchorPane();
            ToggleButton mailButton = new ToggleButton("Mail");
            Button markReceivedButton = new Button("Mark Received");
            ToggleButton bankButton = new ToggleButton("Bank");

            // Add Text for bank details (initially invisible)
            Text bankDetailsText = new Text("Bank information available, transfer money to:\n"
                    + debt.getDebtor().getBankAccount());
            bankDetailsText.setVisible(false);
            contentPane.getChildren().add(bankDetailsText);

            // Set actions for the buttons
            mailButton.setOnAction(event -> handleMailButton(contentPane, debt));
            markReceivedButton.setOnAction(event -> markReceived(debt));
            bankButton.setOnAction(event -> handleBankButton(bankDetailsText, bankButton));

            // Set the positioning for the entities, current values are just a guess
            AnchorPane.setTopAnchor(bankDetailsText, 10.0);
            AnchorPane.setLeftAnchor(bankDetailsText, 10.0);
            AnchorPane.setTopAnchor(mailButton, 30.0);
            AnchorPane.setLeftAnchor(mailButton, 10.0);
            AnchorPane.setTopAnchor(markReceivedButton, 30.0);
            AnchorPane.setLeftAnchor(markReceivedButton, 20.0);
            AnchorPane.setTopAnchor(bankButton, 30.0);
            AnchorPane.setLeftAnchor(bankButton, 30.0);

            // Connect the created entities
            contentPane.getChildren().addAll(mailButton, markReceivedButton, bankButton);
            titledPane.setContent(contentPane);
            accordionDebts.getPanes().add(titledPane);
        }
    }

    /**
     * Handles the action when the "Bank" button is clicked.
     *
     * @param bankDetailsText The bank details text.
     * @param bankButton      The "Bank" button.
     */
    public void handleBankButton(Text bankDetailsText, ToggleButton bankButton) {
        if (bankButton.isSelected()) {
            bankDetailsText.setVisible(true);
        } else {
            bankDetailsText.setVisible(false);
        }
    }

    /**
     * Handles the action when the "Mark Received" button is clicked.
     *
     * @param debt The open debt.
     */
    public void markReceived(Debt debt) {
        int amount = 10; // toDo, hardcoded for now,
        // not sure where to get the amount that the debtor paid the creditor from
        debt.payDebt(amount);
        if (debt.isSettled()) {
            debtList.remove(debt);
        }
        if (debtList.isEmpty()) {
            noDebtMessage.setVisible(true);
        }
    }

    /**
     * Handles the action when the "Mail" button is clicked.
     *
     * @param contentPane The content of the debt.
     * @param debt        The open debt.
     */
    public void handleMailButton(AnchorPane contentPane, Debt debt) {
        // Handles different actions based on if the button was toggled on or off at first
        // (by the presence of the "send reminder" button)
        if (contentPane.getChildren().stream().noneMatch(node -> node instanceof Button)) {
            // If no button is present (it was toggled off), add a new button (now toggle on)
            Text emailConfiguredText = new Text("Email configured: ");
            Button sendReminder = new Button("send reminder");
            sendReminder.setOnAction(event -> sendReminder(debt));
            contentPane.getChildren().add(sendReminder);

            // Set the positioning of the entities, current values are just a guess
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
    public void sendReminder(Debt debt) {
        String reminder = "Dear " + debt.getDebtor().getUsername() + ",\n\n" +
                "This is a friendly reminder regarding the outstanding debt that is currently due."
                + "\n\nDetails of the debt:\n\n" + debt + "\n\nWe kindly request that you make " +
                "the necessary payment at your earliest convenience."
                + "\n\nThank you for your understanding and prompt action."
                + "\n\nBest regards,\n\n" + debt.getCreditor().getUsername();
        // toDo, something like SendMail(debt.getDebtor().getEmail, reminder);
    }

    /**
     * go back to overview page
     */
    public void abortDebts() {
        mainCtrl.showOverview(event);
    }
}
