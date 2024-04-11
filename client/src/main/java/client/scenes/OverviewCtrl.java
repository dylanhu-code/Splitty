package client.scenes;

import client.EventStorageManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

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
    private String[] languages = {"English", "Dutch", "Bulgarian"};
    private Locale currentLocale;

    @FXML
    public Button manageTagsButton;

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
    public Text participantsText;
    @FXML
    private Text expensesText;
    @FXML
    public ComboBox<String> languagesBox;
    @FXML
    public Button flagButton;
    @FXML
    private FlowPane participantsFlowPane;
    @FXML
    private Button statisticsButton;
    @FXML
    public Button editNameButton;

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
        initializeParticipants();
        showAllExpenses();
        assert event != null;
        eventNameText.setText(event.getTitle());
        primaryStage.setScene(overview);
        primaryStage.show();

        editNameButton.setGraphic(generateIcons("edit_icon"));
        editNameButton.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: transparent;");

        server.registerForEventUpdates(event, e ->{
            if (e.getEventId() == event.getEventId()) {
                System.out.println("an update has occurred:\n" + e);
                try {
                    Platform.runLater(()->{
                        initialize(primaryStage, overview, e);
                    });
                    System.out.println("the page was refreshed");
                } catch (Exception ex) {
                    System.out.println("an exception has occurred trying to refresh");
                    ex.printStackTrace();
                }
            }
        });

    }

    private void initializeParticipants() {
        participantsFlowPane.getChildren().clear();
        participantsBox.setItems(null);
        List<String> participantsNames = new ArrayList<>();
        for (Participant participant : event.getParticipants()) {
            HBox participantHBox = new HBox();
            participantsNames.add(participant.getName());
            Text participantName = new Text(participant.getName());
            participantName.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Button deleteButton = new Button("x");
            deleteButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; " +
                    "-fx-font-size: 10px; -fx-border-color: red; -fx-border-radius: 15; " +
                    "-fx-padding: 2px 5px;");
            deleteButton.setOnAction(event -> confirmDeleteParticipant(participant));

            participantHBox.getChildren().addAll(participantName, deleteButton);

            participantHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(deleteButton, new Insets(0, 0, 7, 0));

            participantsFlowPane.getChildren().add(participantHBox);
            participantsFlowPane.setHgap(10);
        }
        participantsBox.setItems(FXCollections.observableArrayList(participantsNames));
    }

    private void confirmDeleteParticipant(Participant participant) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation to delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the participant: "
                + participant.getName() +  "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteParticipant(participant);
        }
    }

    private void deleteParticipant(Participant participant) {
        event.removeParticipant(participant);
        try {
            event = server.updateEvent(event.getEventId(), event);
            server.deleteParticipant(participant.getUserId());
        } catch (WebApplicationException err) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(err.getMessage());
            alert.showAndWait();
            return;
        }
        initializeParticipants();

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
            mainCtrl.updateLocale(currentLocale);
        }
    }

    /**
     * generates the icons for the download button
     * @param path - the path to the icon
     * @return - the image view of the icon
     */
    private ImageView generateIcons(String path) {
        String iconPath = "file:src/main/resources/" + path + ".png";
        Image image = new Image(iconPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(18);
        imageView.setFitHeight(18);
        return imageView;
    }

    /**
     * sets the current locale
     * @param locale - the locale to set
     */
    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
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
        statisticsButton.setText(bundle.getString("statisticsButton"));
        manageTagsButton.setText(bundle.getString("manageTags"));
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
        if (event.getParticipants().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Edit participant warning");
            a.setContentText("Cannot edit participant as there are no participants in this event");
            a.show();
            return;
        }
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
     * Goes to the statistics page
     */
    public void goToStatisticsPage() {
        mainCtrl.showStatistics(event);
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
        mainCtrl.showAddOrEditExpense(null, event);
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
     * Opens the edit name window
     */
    public void openEditNameWindow() {
        mainCtrl.showEditName(event);
    }

    /**
     * Goes to the tag page
     */
    public void goToTagsPage() {
        mainCtrl.showTags(event);
    }
    /**
     * getter for the event
     * @return the event
     */
    public Event getEvent() {
        return event;

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
        Label tagLabel = new Label();
        Region spacer = new Region();
        SplittyMainCtrl mainCtrl;

        /**
         * Expense Cell Class, in order to be able to then retrieve the expenses from the view
         *
         * @param event    - the event the expenses belong to
         * @param mainCtrl - the main control of app so that we can switch scenes
         */
        public ExpenseCell(SplittyMainCtrl mainCtrl, Event event) {
            super();
            this.mainCtrl = mainCtrl;
            this.currentE = event;
            box.getChildren().addAll(dateLabel, payorLabel, paidLabel, amountLabel, forLabel,
                    expenseNameLabel, beneficiariesLabel, tagLabel,
                    spacer, deleteButton, editButton);
            box.setHgrow(pane, Priority.ALWAYS);

            deleteButton.setOnAction(e -> {
                Expense expense = getItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this expense?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    getExpensesListView().getItems().remove(expense);
                    currentE.getExpenses().remove(expense);
                    try {
                        server.updateEvent(currentE.getEventId(), currentE);
                        server.deleteExpense(expense.getExpenseId());
                    } catch (WebApplicationException err) {
                        var errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.initModality(Modality.APPLICATION_MODAL);
                        errorAlert.setContentText(err.getMessage());
                        errorAlert.showAndWait();
                        return;
                    }
                }
            });

            editButton.setOnAction(eve -> {
                mainCtrl.showAddOrEditExpense(getItem(), currentE);
            });

            styleProperty().bind(Bindings.createStringBinding(() -> {
                if (getIndex() % 2 == 0) {
                    return "-fx-background-color: #b7f3ff;";
                } else {
                    return "-fx-background-color: #d2f7ff;";
                }
            }, indexProperty()));
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
                    for (int i = 0; i < sizeOfList; i++) {
                        String currentName = expense.getBeneficiaries().get(i).getName();
                        if (i == sizeOfList - 1) {
                            beneficiaries.append(currentName + ")");
                        } else {
                            beneficiaries.append(currentName + ", ");
                        }
                    }
                }
                Tag tag = expense.getTag();
                String tagName = tag.getName();
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

                tagLabel.setText(tagName);

                tagLabel.setStyle("-fx-background-color: " +
                        (tag != null ? tag.getColor() : "transparent") + ";" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 2 5 2 5;" +
                        "-fx-text-fill: white;");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                setGraphic(box);
            } else {
                setGraphic(null);
            }
        }
    }
    /**
     * Handles the action when common keys are pressed.
     *
     * @param k The key instance.
     */
    public void keyPressed(KeyEvent k) {
        if (Objects.requireNonNull(k.getCode()) == KeyCode.ESCAPE) {
            returnToStart();
        }
    }


    /**
     * shuts down the thread
     */
    public void stop(){
        server.stop();
        System.out.println("Stop method in overviewCtrl was called.");
    }
}
