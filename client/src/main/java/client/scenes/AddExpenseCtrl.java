package client.scenes;

import commons.*;
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static client.scenes.SplittyMainCtrl.currentLocale;

/**
 * Controller class for the Add Expense view.
 */
public class AddExpenseCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Event event;
    private Stage primaryStage;
    private Scene addExpense;
    private ResourceBundle bundle;
    private Expense editableExpense;

    @FXML
    private ChoiceBox<Participant> whoPaidChoiceBox;

    @FXML
    private ChoiceBox<ExpenseType> expenseTypeChoiceBox;

    @FXML
    private TextField whatFor;

    @FXML
    private TextField howMuch;

    @FXML
    private DatePicker datePicker;

    @FXML
    public Button addExpenseButton;
    @FXML
    public Button abortExpenseButton;
    @FXML
    public Label howToSplitText;
    @FXML
    public Label whenText;
    @FXML
    public Label howMuchText;
    @FXML
    public Label whatForText;
    @FXML
    public Label whoPaidText;
    @FXML
    public Label titleExpenseText;
    @FXML
    public Label expenseTypeText;
    @FXML
    public FlowPane checkBoxContainer;
    private List<Participant> selectedBeneficiaries;

    /**
     * Constructs an instance of AddExpenseCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param addExpense   The page with its controller.
     * @param event        The event.
     * @param expense Expense to pass
     */
    public void initialize(Stage primaryStage, Scene addExpense, Event event, Expense expense) {
        selectedBeneficiaries = new ArrayList<>();
        this.primaryStage = primaryStage;
        this.addExpense = addExpense;
        this.event = event;
        this.editableExpense = expense;

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI(); 

        if (editableExpense != null) {
            selectedBeneficiaries = expense.getBeneficiaries();
            String expenseName = expense.getExpenseName();
            whatFor.setText(expenseName);
            howMuch.setText(String.valueOf(expense.getAmount()));
            Date date = expense.getDate();
            datePicker.setValue(date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
            addExpenseButton.setText("Edit");
            initCheckBoxesEdit();
        } else {
            initChoiceBoxes();
        }
        primaryStage.setScene(addExpense);
        primaryStage.show();
    }

    private void initCheckBoxesEdit() {
        ObservableList<Participant> participants =
                FXCollections.observableArrayList(event.getParticipants());

        for (Participant p : participants) {
            CheckBox checkBox = new CheckBox(p.getName());
            checkBox.setSelected(selectedBeneficiaries.contains(p));
            checkBox.setOnAction(eve -> {
                if (checkBox.isSelected()) {
                    selectedBeneficiaries.add(p);
                } else {
                    selectedBeneficiaries.remove(p);
                }
            });
            checkBoxContainer.getChildren().add(checkBox);
        }
        checkBoxContainer.setPrefWrapLength(100);
        checkBoxContainer.setOrientation(Orientation.VERTICAL);
        checkBoxContainer.setVgap(10);
    }

    /**
     * Updates the language to the preferred setting
     */
    private void updateUI() {
        howToSplitText.setText(bundle.getString("howToSplitText"));
        whenText.setText(bundle.getString("whenText"));
        howMuchText.setText(bundle.getString("howMuchText"));
        whatForText.setText(bundle.getString("whatForText"));
        whoPaidText.setText(bundle.getString("whoPaidText"));
        titleExpenseText.setText(bundle.getString("titleExpenseText"));
        addExpenseButton.setText(bundle.getString("createExpenseButton"));
        abortExpenseButton.setText(bundle.getString("abortExpenseButton"));
        expenseTypeText.setText(bundle.getString("expenseTypeText"));
    }

    /**
     * Initializes the choice boxes with available options.
     */
    private void initChoiceBoxes() {
        clearFields();
        ObservableList<Participant> participants =
                FXCollections.observableArrayList(event.getParticipants());
        whoPaidChoiceBox.setConverter(new StringConverter<Participant>() {
            @Override
            public String toString(Participant participant) {
                return participant == null ? null : participant.getName();
            }
            @Override
            public Participant fromString(String string) {
                return null;
            }
        });
        whoPaidChoiceBox.setItems(participants);
        whoPaidChoiceBox.setItems(participants);
        expenseTypeChoiceBox.getItems().addAll(ExpenseType.FOOD, ExpenseType.TRANSPORTATION,
                ExpenseType.DRINKS, ExpenseType.OTHER);

        for (Participant p: participants) {
            CheckBox checkBox = new CheckBox(p.getName());
            checkBox.setOnAction(eve -> {
                if (checkBox.isSelected()) {
                    selectedBeneficiaries.add(p);
                } else {
                    selectedBeneficiaries.remove(p);
                }
            });
            checkBoxContainer.getChildren().add(checkBox);
        }
        checkBoxContainer.setPrefWrapLength(100);
        checkBoxContainer.setOrientation(Orientation.VERTICAL);
        checkBoxContainer.setVgap(10);
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
    public void abort() {
        clearFields();
        mainCtrl.showOverview(event);
    }

    /**
     * Handles the action when the "Add" button is clicked.
     */
    public void add() {
        try {
            Expense e = getExpense();
            if (this.editableExpense != null) {
                e.setExpenseId(editableExpense.getExpenseId());
                server.updateExpense(e.getExpenseId(), e);
                event = server.getEventById(event.getEventId());
            } else {
                Expense e2 = server.addExpense(e);
                event.addExpense(e2);
                event = server.updateEvent(event.getEventId(), event);
            }

        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        editableExpense = null;
        mainCtrl.showOverview(event);
    }

    /**
     * Initializes an Expense instance from the entered values.
     *
     * @return The Expense instance.
     */
    private Expense getExpense() {
        if(whatFor.getText().isEmpty() || whoPaidChoiceBox.getItems().isEmpty()
                ||  howMuch.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
        }
        else {
            Participant payor = whoPaidChoiceBox.getValue();
            double amount = Double.parseDouble(howMuch.getText());
            if(amount <= 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please insert a valid amount of money");
                alert.showAndWait();
            }
            String expenseName = whatFor.getText();
            Date date = java.util.Date.from(datePicker.getValue()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()); // Convert JavaFX LocalDate to java.util.Date.
            ExpenseType type = expenseTypeChoiceBox.getValue();
            Expense newExpnese = new Expense(payor, amount,
                    selectedBeneficiaries, expenseName, date, type);
            Set<Tag> tags = Set.of(new Tag("food", "green"));
            newExpnese.setTags(tags);
            return newExpnese;
        }
        return null;
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        if (whatFor != null)  whatFor.clear();
        if (howMuch != null) howMuch.clear();
        if (datePicker != null) initDate();
        if (checkBoxContainer != null) checkBoxContainer.getChildren().clear();
    }

    /**
     * Handles the action when common keys are pressed.
     *
     * @param e The key instance.
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
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

    /**
     * add a new event
     * @return the event
     */
    public Event getEvent() {
        return event;
    }
}
