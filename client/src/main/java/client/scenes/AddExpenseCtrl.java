package client.scenes;

import client.utils.ConfigUtils;
import commons.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private String[] currencies = {"EUR", "USD", "CHF"};

    private Locale currentLocale;

    @FXML
    private ComboBox<String> currency;
    @FXML
    private ChoiceBox<Participant> whoPaidChoiceBox;
    @FXML
    private ComboBox<Tag> expenseTypeChoiceBox;
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
     */
    public void initialize(Stage primaryStage, Scene addExpense) {
        selectedBeneficiaries = new ArrayList<>();
        this.primaryStage = primaryStage;
        this.addExpense = addExpense;
    }

    /**
     * Displays the scene itself
     */
    public void initScene() {
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
        selectedBeneficiaries = new ArrayList<>();
        primaryStage.setScene(addExpense);
        primaryStage.show();
    }

    /**
     * updates the data of the scene
     * @param event - the specific event
     * @param expense - the possible expense to edit
     */
    public void updateAllSceneData(Event event, Expense expense) {
        selectedBeneficiaries = new ArrayList<>();
        this.event = event;
        this.editableExpense = expense;
        if (editableExpense == null) {
            initChoiceBoxes();
        } else {
            updateEditData();
        }

    }

    /**
     * Displays the correct page for edit expense
     */
    public void updateEditData() {
        clearFields();

        selectedBeneficiaries = editableExpense.getBeneficiaries();
        String expenseName = editableExpense.getExpenseName();
        whatFor.setText(expenseName);
        howMuch.setText(String.valueOf(editableExpense.getAmount()));
        Date date = editableExpense.getDate();
        datePicker.setValue(date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        addExpenseButton.setText("Edit");
        Tag t = editableExpense.getTag();
        initTagChoice();
        expenseTypeChoiceBox.setValue(t);
        initPayorBox();
        whoPaidChoiceBox.setValue(editableExpense.getPayor());
        initCheckBoxesEdit();
    }

    /**
     * sets the current locale
     * @param locale - the locale to set
     */
    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
    }

    private void initCheckBoxesEdit() {
        checkBoxContainer.getChildren().clear();
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
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(ConfigUtils.readPreferredCurrency("config.txt"));
        checkBoxContainer.setPrefWrapLength(100);
        checkBoxContainer.setOrientation(Orientation.VERTICAL);
        checkBoxContainer.setVgap(10);
    }

    /**
     * updates the locale
     * @param locale - the locale to update to
     */
    public void updateLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
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
        ObservableList<Participant> participants = initPayorBox();
        initTagChoice();

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
        currency.setItems(FXCollections.observableArrayList(currencies));
        currency.setValue(ConfigUtils.readPreferredCurrency("config.txt"));
        checkBoxContainer.setPrefWrapLength(100);
        checkBoxContainer.setOrientation(Orientation.VERTICAL);
        checkBoxContainer.setVgap(10);
    }

    private ObservableList<Participant> initPayorBox() {
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
        return participants;
    }

    private void initTagChoice() {
        ComboBox<Tag> tagComboBox = new ComboBox<>();
        expenseTypeChoiceBox.setConverter(new StringConverter<Tag>() {
            @Override
            public String toString(Tag tag) {
                return tag == null ? "" : tag.getName();
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });

        expenseTypeChoiceBox.setItems(FXCollections.observableArrayList(server.getTags(event)));

        expenseTypeChoiceBox.setButtonCell(new TagListCell());
        expenseTypeChoiceBox.setCellFactory(param -> new TagListCell());

    }

    private class TagListCell extends ListCell<Tag> {
        @Override
        protected void updateItem(Tag tag, boolean empty) {
            super.updateItem(tag, empty);

            if (empty || tag == null) {
                setText(null);
                setGraphic(null);
            } else {
                Label label = new Label(tag.getName());
                label.setTextFill(Color.WHITE); // Set text color

                Rectangle rectangle = new Rectangle(0, 20); // Height of the rectangle
                rectangle.setFill(Color.web(tag.getColor()));
                rectangle.setArcWidth(10); // Adjust corner radius
                rectangle.setArcHeight(10); // Adjust corner radius

                label.widthProperty().addListener((observable, oldValue, newValue) -> {
                    rectangle.setWidth(newValue.doubleValue() + 20); // Adjust padding
                });

                StackPane stackPane = new StackPane(rectangle, label);
                stackPane.setPadding(new Insets(2)); // Adjust padding as needed

                setGraphic(stackPane);
            }
        }
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
        if (editableExpense != null) {
            selectedBeneficiaries = new ArrayList<>();
        }
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
            Tag tag = expenseTypeChoiceBox.getValue();
            Expense newExpnese = new Expense(payor, amount, currency.getValue(),
                    selectedBeneficiaries, expenseName, date, tag);
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
        if (expenseTypeChoiceBox!= null) {
            expenseTypeChoiceBox.getItems().clear();
        }
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

    @FXML
    private void handleComboBox(ActionEvent actionEvent) {
        String selectedCurrency = currency.getSelectionModel().getSelectedItem();
        if (selectedCurrency != null) {
            ConfigUtils.currency = currency.getValue();
        }
        else ConfigUtils.currency = ConfigUtils.readPreferredCurrency("config.txt");
    }
}
