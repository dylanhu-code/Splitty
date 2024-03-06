package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import commons.Event;
import commons.Expense;
import commons.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class OverviewCtrl {
    private final splittyMainCtrl mainCtrl;

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
    private Button sendinvitesButton;
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
    private Pair<OverviewCtrl, Parent> overview;

    /**
     * Constructor
     *
     * @param mainCtrl controller of the main page
     */
    @Inject
    public OverviewCtrl(splittyMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page
     * @param overview     The page with its controller
     * @param event        The event
     */
    public void initialize(Stage primaryStage, Pair<OverviewCtrl, Parent> overview, Event event) {
        this.primaryStage = primaryStage;
        this.overview = overview;
        this.event = event;

        setParticipantNames("Pesho, Tosho, Gosho");
        String[] testNames = new String[]{"Pesho", "Tosho", "Gosho"};
        participantsBox.setItems(FXCollections.observableArrayList(testNames));

        showOverview();
    }

    /**
     * Sets the page (title, scene and the event name text box)
     */
    public void showOverview() {
        primaryStage.setTitle(event.getTitle());
        eventNameText.setText(event.getTitle());
        primaryStage.setScene(new Scene(overview.getValue()));
        primaryStage.show();
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
    public Pair<OverviewCtrl, Parent> getOverview() {
        return overview;
    }

    //TODO the window is not yet created

    /**
     * When clicked it should open an add participant window
     */
    public void addParticipant() {
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
        Injector injector = createInjector(new MyModule());
        MyFXML fxml = new MyFXML(injector);
        var overview = fxml.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var addExpenseCtrl = injector.getInstance(AddExpenseCtrl.class);
        addExpenseCtrl.initialize(primaryStage, overview, event);
    }
}
