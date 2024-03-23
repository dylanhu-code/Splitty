package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OverviewCtrl {
    private final SplittyMainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private ListView<Expense> expensesListView;
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
    private Button sendInvitesButton;
    @FXML
    private Text eventNameText;
    @FXML
    private Text participantNamesText;
    @FXML
    private Text participantsText;
    @FXML
    private Text expensesText;

    private Event event;
    private Stage primaryStage;
    private Scene overview;

    /**
     * Constructor
     *
     * @param server   The ServerUtils instance
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

        // Fetch real participant names from the Event object
        List<User> participants = event.getParticipants();
        List<String> participantNames = participants.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        setParticipantNames(String.join(", ", participantNames));
        participantsBox.setItems(FXCollections.observableArrayList(participantNames));

        showOverview();
        showAllExpenses();
        server.registerForUpdates("/topic/event/update", Event.class, updatedEvent -> {
            // Update the UI with the received event data
            eventNameText.setText(updatedEvent.getTitle());
            updateExpensesListView(updatedEvent.getExpenses());
        });
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
        expensesListView.setCellFactory(param -> new ExpenseCell());
        expensesListView.getItems().addAll(expenses);
    }

    /**
     * Getter for expenses list
     *
     * @return expenses list
     */
    public ListView<Expense> getExpensesListView() {
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
        mainCtrl.showAddParticipant();
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
        updateExpensesListView(event.getExpenses());
    }

    /**
     * Show all expenses from a user
     */
    public void showFromPersonExpenses() {
        List<Expense> expenseList = event.getExpenses();
        List<Expense> personExpenses = new ArrayList<>();

        for (Expense expense : expenseList) {
            if (expense.getPayor().getUsername().equals(participantsBox.getValue())) {
                personExpenses.add(expense);
            }
        }
        updateExpensesListView(personExpenses);
    }

    /**
     * Show all expenses including a user
     */
    public void showIncludingPersonExpenses() {

        List<Expense> expenseList = event.getExpenses();
        List<Expense> expenseseIncluding = new ArrayList<>();

        for (Expense expense : expenseList) {
            List<User> beneficiaries = expense.getBeneficiaries();
            List<String> names = beneficiaries.stream()
                    .map(User::getUsername)
                    .toList();

            if (names.contains(participantsBox.getValue())) {
                expenseseIncluding.add(expense);
            }
        }
        updateExpensesListView(expenseseIncluding);
    }

    /**
     * When clicked it opens the addExpense window
     */
    public void addExpense() {
//        Injector injector = createInjector(new MyModule());
//        MyFXML fxml = new MyFXML(injector);
//        var overview = fxml.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
//        var addExpenseCtrl = injector.getInstance(AddExpenseCtrl.class);
//        addExpenseCtrl.initialize(primaryStage, overview, event);
        mainCtrl.showAddExpense(event);
        //TODO figure out which one of these is smart to use
    }

    /**
     * Handles the action when the "Send Invites" button is clicked.
     */
    public void sendInvites() {
        mainCtrl.showInvitation(event);
    }

    public class ExpenseCell extends ListCell<Expense> {
        @Override
        protected void updateItem(Expense expense, boolean empty) {
            super.updateItem(expense, empty);

            if (expense != null && !empty) {
                LocalDate localDate = expense.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM"));
                String payor = "JOhn";// this has to be changed to getPayor().getUsername() when its not null
                String amount = String.format("%.2f EUR", expense.getAmount());

                StringBuilder beneficiaries = new StringBuilder();
                //deal with beneficiaries format


                // Create Labels for each part of the expense
                Label dateLabel = new Label(formattedDate);
                dateLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 10 0 0;");

                Label payorLabel = new Label(payor);
                payorLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 5 0 0;");

                Label amountLabel = new Label(amount);
                amountLabel.setStyle("-fx-font-weight: bold;-fx-padding: 0 2 0 0;");

                Label paidLabel = new Label(" paid ");
                Label forLabel = new Label(" for ");

                Label expenseNameLabel = new Label(expense.getExpenseName());
                expenseNameLabel.setStyle("-fx-font-weight: bold;");
                Label beneficiariesLabel = new Label(beneficiaries.toString());
                beneficiariesLabel.setStyle("-fx-text-fill: grey; -fx-padding: 0 10 0 0;");

                Button deleteButton = new Button("Delete");
                Button editButton = new Button("Edit");

                deleteButton.setOnAction(event -> {
                    // Handle delete action
                });

                editButton.setOnAction(event -> {
                    // Handle edit action
                });
                //this is so that the button are at the end of the box
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox hbox = new HBox(dateLabel, payorLabel, paidLabel, amountLabel, forLabel, expenseNameLabel, beneficiariesLabel, spacer, deleteButton, editButton);
                setGraphic(hbox);
            } else {
                setGraphic(null);
            }
        }
    }

}
