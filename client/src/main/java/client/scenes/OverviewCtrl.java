package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static client.scenes.StartScreenCtrl.currentLocale;
import java.util.ResourceBundle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

public class OverviewCtrl {
    private final SplittyMainCtrl mainCtrl;
    private final ServerUtils server;
    private Event event;
    private Stage primaryStage;
    private Scene overview;
    private ResourceBundle bundle;

    @FXML
    public Button goBackButton;
    @FXML
    public Button sendInvitesButton;
    @FXML
    private ListView<String> expensesListView;
    @FXML
    private ComboBox<String> participantsBox;
    @FXML
    private Button allButton;
    @FXML
    private Button fromButton;
    @FXML
    private Button includingButton;
    @FXML
    private Button settleDebtsButton;
    @FXML
    private Button editParticipantsButton;
    @FXML
    private Button addParticipantsButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private Text eventNameText;
    @FXML
    private Text participantNamesText;
    @FXML
    private Text participantsText;
    @FXML
    private Text expensesText;

    /**
     * Constructor
     *
     * @param server The ServerUtils instance
     * @param mainCtrl controller of the main page
     */
    @Inject
    public OverviewCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page
     * @param overview     The page with its controller
     * @param event        The event
     */
    public void initialize(Stage primaryStage, Scene overview, Event event) {
        this.primaryStage = primaryStage;
        this.overview = overview;
        this.event = event;

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        // Fetch real participant names from the Event object
        List<User> participants = event != null ? event.getParticipants() : Collections.emptyList();
        List<String> participantNames = participants.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        setParticipantNames(String.join(", ", participantNames));
        participantsBox.setItems(FXCollections.observableArrayList(participantNames));

        showOverview();

        server.registerForUpdates("/topic/event/update", Event.class, updatedEvent -> {
            // Update the UI with the received event data
            eventNameText.setText(updatedEvent.getTitle());
            updateExpensesListView(updatedEvent.getExpenses());
        });
    }

    private void updateUI() {
        allButton.setText(bundle.getString("allButton"));
        fromButton.setText(bundle.getString("fromButton"));
        includingButton.setText(bundle.getString("includingButton"));
        settleDebtsButton.setText(bundle.getString("settleDebtsButton"));
        editParticipantsButton.setText(bundle.getString("editParticipantsButton"));
        addParticipantsButton.setText(bundle.getString("addParticipantsButton"));
        addExpenseButton.setText(bundle.getString("addExpenseButton"));
        sendInvitesButton.setText(bundle.getString("sendInvitesButtonOverview"));
        participantNamesText.setText(bundle.getString("participantNamesText"));
        participantsText.setText(bundle.getString("participantsText"));
        expensesText.setText(bundle.getString("expensesText"));
        goBackButton.setText(bundle.getString("goBackButton"));

    }

    /**
     * Sets the page (title, scene and the event name text box)
     */
    public void showOverview() {
        primaryStage.setTitle(event.getTitle());
        eventNameText.setText(event.getTitle());
        primaryStage.setScene(overview);
        primaryStage.show();
    }

    /**
     * Updates the expenses list view with the provided list of expenses.
     *
     * @param expenses The list of expenses to display.
     */
    private void updateExpensesListView(List<Expense> expenses) {
        expensesListView.getItems().clear();
        StringBuilder str = new StringBuilder();

        for (Expense expense : expenses) {
            str.append(expense.getDate()).append("  ")
                    .append(expense.getPayor()).append(" paid ")
                    .append(expense.getAmount()).append(" for ")
                    .append(expense.getType()).append("\n");
        }

        String result = str.toString();
        expensesListView.getItems().addAll(result);
    }

    /**
     * Getter for expenses list
     *
     * @return expenses list
     */
    public ListView<String> getExpensesListView() {
        return expensesListView;
    }

    /**
     * Getter for the primary stage
     *
     * @return primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Getter for the overview
     *
     * @return overview
     */
    public Scene getOverview() {
        return overview;
    }

    //TODO the window is not yet created

    /**
     * When clicked it should open an add participant window
     */
    public void addParticipant() {
        mainCtrl.showAddParticipant(event);
    }

    //TODO the window is not yet created

    /**
     * When clicked it should open an edit participants window
     */
    public void editParticipants() {

    }

    //TODO the window is not yet created

    /**
     * When clicked it should open a settle debt window
     */
    public void settleDebts() {

    }

    /**
     * It sets the text to display the names of the participants
     *
     * @param names the names
     */
    public void setParticipantNames(String names) {
        participantsText.setText(names);
    }

    /**
     * When a participant is chosen it changes the contents of the from and including buttons
     */
    public void setNameThreeButtons() {
        fromButton.setText("From " + participantsBox.getValue());
        includingButton.setText("Including " + participantsBox.getValue());
    }

    //TODO The next 3 methods will be finished when the backend is done

    /**
     * Show all the expenses in this event
     */
    public void showAllExpenses() {
        expensesListView.getItems().clear();

        List<Expense> expenseList = event.getExpenses();
        StringBuilder str = new StringBuilder();

        for (Expense expense : expenseList) {
            str.append(expense.getDate()).append("  ")
                    .append(expense.getPayor()).append(" paid ")
                    .append(expense.getAmount()).append(" for ")
                    .append(expense.getType()).append("\n");
        }

        String result = str.toString();
        expensesListView.getItems().addAll(result);
    }

    /**
     * Show all expenses from a user
     */
    public void showFromPersonExpenses() {
        expensesListView.getItems().clear();

        List<Expense> expenseList = event.getExpenses();
        StringBuilder str = new StringBuilder();

        for (Expense expense : expenseList) {
            if (expense.getPayor().getUsername().equals(participantsBox.getValue())) {
                str.append(expense.getDate()).append("  ")
                        .append(expense.getPayor()).append(" paid ")
                        .append(expense.getAmount()).append(" for ")
                        .append(expense.getType()).append("\n");
            }
        }

        String result = str.toString();
        expensesListView.getItems().addAll(result);
    }

    /**
     * Show all expenses including a user
     */
    public void showIncludingPersonExpenses() {
        expensesListView.getItems().clear();

        List<Expense> expenseList = event.getExpenses();
        StringBuilder str = new StringBuilder();

        for (Expense expense : expenseList) {
            List<User> beneficiaries = expense.getBeneficiaries();
            List<String> names = beneficiaries.stream()
                    .map(User::getUsername)
                    .toList();

            if (names.contains(participantsBox.getValue())) {
                str.append(expense.getDate()).append("  ")
                        .append(expense.getPayor()).append(" paid ")
                        .append(expense.getAmount()).append(" for ")
                        .append(expense.getType()).append("\n");
            }
        }

        String result = str.toString();
        expensesListView.getItems().addAll(result);
    }

    /**
     * When clicked it opens the addExpense window
     */
    public void addExpense() {
        mainCtrl.showAddExpense(event);
    }

    /**
     * Handles the action when the "Send Invites" button is clicked.
     */
    public void sendInvites() {
        mainCtrl.showInvitation(event);
    }

    /**
     * Return to the Start Screen Page
     */
    public void returnToStart() {
        mainCtrl.showStartScreen();
    }

    /**
     * Opens the debts window
     */
    public void settleDebtsWindow() {
        mainCtrl.showOpenDebts(event);
    }
}
