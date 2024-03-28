package client.scenes;

import client.EventStorageManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;
import static client.scenes.SplittyMainCtrl.currentLocale;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class OverviewCtrl {
    private final SplittyMainCtrl mainCtrl;
    private final ServerUtils server;
    private Event event;
    private Stage primaryStage;
    private Scene overview;
    private ResourceBundle bundle;
    private StartScreenCtrl startScreenCtrl;
    private String[] languages = {"English", "Dutch", "Bulgarian"};

    @FXML
    public Button goBackButton;
    @FXML
    public Button sendInvitesButton;
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
    private Label eventNameText;
    @FXML
    private Text participantNamesText;
    @FXML
    public Text participantsText;
    @FXML
    private Text expensesText;
    @FXML
    public ComboBox<String> languagesBox;
    @FXML
    public Button flagButton;

    /**
     * Constructor
     *
     * @param server   The ServerUtils instance
     * @param mainCtrl controller of the main page
     * @param storageManager - manager for the event-user file
     */
    @Inject
    public OverviewCtrl(ServerUtils server, SplittyMainCtrl mainCtrl,
                        EventStorageManager storageManager) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        startScreenCtrl = new StartScreenCtrl(mainCtrl, server, storageManager);
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

        changeFlagImage();
        languagesBox.setValue(currentLocale.getDisplayLanguage());
        languagesBox.setItems(FXCollections.observableArrayList(languages));

        // Fetch real participant names from the Event object
        List<Participant> participants = event != null ? event.getParticipants()
                : Collections.emptyList();
        List<String> participantNames = participants.stream()
                .map(Participant::getName)
                .collect(Collectors.toList());

        setParticipantNames(String.join(", ", participantNames));
        participantsBox.setItems(FXCollections.observableArrayList(participantNames));

        assert event != null;
        eventNameText.setText(event.getTitle());
        primaryStage.setScene(overview);
        primaryStage.show();

        showAllExpenses();
        server.registerForUpdates("/topic/event/update", Event.class, updatedEvent -> {
            // Update the UI with the received event data
            eventNameText.setText(updatedEvent.getTitle());
            updateExpensesListView(updatedEvent.getExpenses());
        });
    }

    @FXML
    private void handleComboBoxAction(javafx.event.ActionEvent actionEvent) {
        String selectedLanguage = languagesBox.getSelectionModel().getSelectedItem();
        if (selectedLanguage != null) {
            switch (selectedLanguage) {
                case "English":
                    currentLocale = new Locale("en");
                    break;
                case "Dutch":
                    currentLocale = new Locale("nl");
                    break;
                case "Bulgarian":
                    currentLocale = new Locale("bg");
                    break;
            }
            changeFlagImage();
            bundle = ResourceBundle.getBundle("messages", currentLocale);
            updateUI();
        }
    }


    /**
     * Change the image path, call the update UI method and do the animation
     */
    private void changeFlagImage() {
        ScaleTransition shrinkTransition = new ScaleTransition(Duration.millis(100), flagButton);
        shrinkTransition.setToY(0);
        shrinkTransition.setOnFinished(event -> {
            putFlag();
            ScaleTransition restoreTransition = new
                    ScaleTransition(Duration.millis(100), flagButton);
            restoreTransition.setToY(1);
            restoreTransition.play();
        });
        shrinkTransition.play();
    }

    /**
     * Put a new Image in the button
     */
    public void putFlag() {
        String imagePath;
        String language = currentLocale.getLanguage();
        imagePath = switch (language) {
            case "bg" -> "bg_flag.png";
            case "nl" -> "nl_flag.png";
            default -> "en_flag.png";
        };
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new
                BackgroundImage(imageView.snapshot(null, null),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);

        flagButton.setBackground(new Background(backgroundImage));
    }

    /**
     * Updates to language setting
     */
    private void updateUI() {
        allButton.setText(bundle.getString("allButton"));
        fromButton.setText(bundle.getString("fromButton"));
        includingButton.setText(bundle.getString("includingButton"));
        settleDebtsButton.setText(bundle.getString("settleDebtsButton"));
        editParticipantsButton.setText(bundle.getString("editParticipantsButton"));
        addParticipantsButton.setText(bundle.getString("addParticipantsButton"));
        addExpenseButton.setText(bundle.getString("addExpenseButton"));
        sendInvitesButton.setText(bundle.getString("sendInvitesButtonOverview"));
        participantsText.setText(bundle.getString("participantsText"));
        expensesText.setText(bundle.getString("expensesText"));
        goBackButton.setText(bundle.getString("goBackButton"));

    }

    /**
     * open combo box when the button is clicked
     */
    @FXML
    private void flagClick() {
        languagesBox.show();
    }

    /**
     * Updates the expenses list view with the provided list of expenses.
     *
     * @param expenses The list of expenses to display.
     */
    private void updateExpensesListView(List<Expense> expenses) {
        expensesListView.getItems().clear();
        expensesListView.setCellFactory(param -> new ExpenseCell(mainCtrl, event));
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
     * When clicked it should open an add participant window
     */
    public void addParticipant() {
        mainCtrl.showAddParticipant(event, null);
    }

    /**
     * When clicked it should open an edit participants window
     */
    public void editParticipants() {
        List<Participant> allParticipants = event.getParticipants();
        List<String> participantsNames = allParticipants.stream()
                .map(Participant::getName).collect(Collectors.toList());
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, participantsNames);
        dialog.setTitle("Edit Participant");
        dialog.setHeaderText("Select a participant to edit: ");
        dialog.setContentText("Participant: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedName -> {
            Participant selectedParticipant = allParticipants.stream()
                    .filter(p-> p.getName().equals(selectedName))
                    .findFirst().orElse(null);
            if (selectedParticipant!= null) {
                mainCtrl.showAddParticipant(event, selectedParticipant);
            }
        });
    }


    /**
     * It sets the text to display the names of the participants
     *
     * @param names the names
     */
    public void setParticipantNames(String names) {
        participantNamesText.setText(names);
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
            if (expense.getPayor().getName().equals(participantsBox.getValue())) {
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
            List<Participant> beneficiaries = expense.getBeneficiaries();
            List<String> names = beneficiaries.stream()
                    .map(Participant::getName)
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
        mainCtrl.showEditExpense(null, event);
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

    /**
     * getter for the event
     * @return the event
     */
    public Event getEvent() {
        return event;

    }

    /**
     * When the title is clicked, new title can be inputed,
     * (edit title functionalitu)
     */
    public void editTitle() {
        TextField textField = new TextField(eventNameText.getText());
        Button submitButton = new Button("Submit");
        HBox hbox = new HBox(textField, submitButton);
        eventNameText.setGraphic(hbox);
        textField.requestFocus();

        submitButton.setOnAction(e -> {
            eventNameText.setGraphic(null);
            eventNameText.setText(textField.getText());
            event.setTitle(textField.getText());
            event = server.updateEvent(event.getEventId(), event);
            initialize(primaryStage, overview, event);
        });
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                eventNameText.setGraphic(null);
                eventNameText.setText(textField.getText());
                event.setTitle(textField.getText());
                event = server.updateEvent(event.getEventId(), event);
                initialize(primaryStage, overview, event);
            }
        });

        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                submitButton.fire();
            }
        });
    }
    public class ExpenseCell extends ListCell<Expense> {
        private ServerUtils server = new ServerUtils();
        private Event currentE;
        HBox box = new HBox();
        Pane pane = new Pane();
        Button deleteButton = new Button("Delete");
        Button editButton = new Button("Edit");
        Label dateLabel = new Label();
        Label payorLabel = new Label();
        Label paidLabel = new Label(" paid ");
        Label amountLabel = new Label();
        Label forLabel = new Label(" for ");
        Label expenseNameLabel = new Label();
        Label beneficiariesLabel = new Label();
        Region spacer = new Region();
        SplittyMainCtrl  mainCtrl;


        /**
         * Expense Cell Class, in order to be able to then retrieve the expenses from the view
         * @param event - the event the expenses belong to
         * @param mainCtrl - the main control of app so that we can switch scenes
         */

        public ExpenseCell(SplittyMainCtrl mainCtrl, Event event) {
            super();
            this.mainCtrl = mainCtrl;
            this.currentE = event;
            box.getChildren().addAll(dateLabel, payorLabel, paidLabel, amountLabel, forLabel,
                    expenseNameLabel, beneficiariesLabel, spacer, deleteButton, editButton);
            box.setHgrow(pane, Priority.ALWAYS);

            deleteButton.setOnAction(e -> {
                // Handle delete action
                Expense expense1 = getItem();
                getExpensesListView().getItems().remove(expense1);
                currentE.getExpenses().remove(expense1);
                try {
                    server.updateEvent(currentE.getEventId(), currentE);
                    server.deleteExpense(expense1.getExpenseId());
                } catch (WebApplicationException err) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(err.getMessage());
                    alert.showAndWait();
                    return;
                }
            });

            editButton.setOnAction(eve -> {
                // Handle edit action
                mainCtrl.showEditExpense(getItem(), currentE);
            });

        }
        @Override
        protected void updateItem(Expense expense, boolean empty) {
            super.updateItem(expense, empty);

            if (expense != null && !empty) {
                LocalDate localDate = expense.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM"));
                String payor = expense.getPayor().getName();
                String amount = String.format("%.2f EUR", expense.getAmount());

                StringBuilder beneficiaries = new StringBuilder();
                if (expense.getBeneficiaries() != null && expense.getBeneficiaries().size() != 0) {
                    beneficiaries.append(" (");
                    int sizeOfList = expense.getBeneficiaries().size();
                    for (int i=0; i<sizeOfList; i++){
                        String currentName = expense.getBeneficiaries().get(i).getName();
                        if (i == sizeOfList- 1) {
                            beneficiaries.append(currentName+")");
                        } else {
                            beneficiaries.append(currentName+", ");
                        }
                    }
                }

                // Create Labels for each part of the expense
                dateLabel.setText(formattedDate);
                dateLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 10 0 0;");

                payorLabel.setText(payor);
                payorLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 5 0 0;");

                amountLabel.setText(amount);
                amountLabel.setStyle("-fx-font-weight: bold;-fx-padding: 0 2 0 0;");

                expenseNameLabel.setText(expense.getExpenseName());
                expenseNameLabel.setStyle("-fx-font-weight: bold;");
                beneficiariesLabel.setText(beneficiaries.toString());
                beneficiariesLabel.setStyle("-fx-text-fill: grey; -fx-padding: 0 10 0 0;");

                //this is so that the button are at the end of the box
                HBox.setHgrow(spacer, Priority.ALWAYS);
                setGraphic(box);
            } else {
                setGraphic(null);
            }
        }
    }
}
