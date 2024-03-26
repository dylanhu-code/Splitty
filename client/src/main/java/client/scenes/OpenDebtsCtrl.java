package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.inject.Inject;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static client.scenes.SplittyMainCtrl.currentLocale;

import java.time.LocalDate;
import java.util.*;

public class OpenDebtsCtrl {
    private final SplittyMainCtrl mainCtrl;


    private ArrayList<Debt> debtList;
    private Event event;
    private Stage primaryStage;
    private Scene openDebts;
    private final ServerUtils server;
    private ResourceBundle bundle;
    private String[] languages = {"English", "Dutch", "Bulgarian"};
    @FXML
    private Label noDebtMessage;

    @FXML
    private Accordion accordionDebts;
    @FXML
    public Button abortDebtsButton;
    @FXML
    public Text titleText;
    @FXML
    public ComboBox<String> languagesBox;
    @FXML
    public Button flagButton;

    /**
     * Constructs an instance of OpenDebtsCtrl with the specified dependencies.
     *
     * @param mainCtrl The MainCtrl instance.
     * @param server   The ServerUtils instance
     */
    @Inject
    public OpenDebtsCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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

        // HARDCODED EXPENSES AND PARTICIPANTS
        List<User> beneficiaries = new ArrayList<>();
        beneficiaries.add(new User("Test2", "ING02", "Dutch"));
        event.addExpense(new Expense(new User("Test1", "ING01", "Dutch"), 100.0,
                beneficiaries, "Trakkie", new Date(124, 3, 26), ExpenseType.FOOD));
        debtList = (ArrayList<Debt>) event.generateDebts();

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        changeFlagImage();
        languagesBox.setValue(currentLocale.getDisplayLanguage());
        languagesBox.setItems(FXCollections.observableArrayList(languages));

        primaryStage.setScene(openDebts);
        primaryStage.show();
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
     * open combo box when the button is clicked
     */
    @FXML
    private void flagClick() {
        languagesBox.show();
    }

    /**
     * Handles the action when the language button is clicked
     * @param actionEvent
     */
    @FXML
    private void handleComboBoxAction(ActionEvent actionEvent) {
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
     * Updates to the language setting
     */
    public void updateUI() {
        abortDebtsButton.setText(bundle.getString("abortDebtsButton"));
        noDebtMessage.setText(bundle.getString("noDebtMessage"));
        titleText.setText(bundle.getString("titleText"));
        accordionDebts.getPanes().clear();
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
            titledPane.setText(debt.getDebtor().getUsername() + " " + bundle.getString("owes")
                    + debt.getAmount() + bundle.getString("to") + debt.getCreditor().getUsername());
            AnchorPane contentPane = new AnchorPane();
            ToggleButton mailButton = new ToggleButton();
            mailButton.setGraphic(generateIcons("mail"));
            mailButton.setOpacity(0.5);
            Button markReceivedButton = new Button(bundle.getString("markReceived"));
            ToggleButton bankButton = new ToggleButton();
            bankButton.setGraphic(generateIcons("bank"));
            bankButton.setOpacity(0.5);

            // Add Text for bank details (initially invisible)
            Text bankDetailsText = new Text(bundle.getString("bankDetails") + "\n"
                    + debt.getDebtor().getBankAccount());
            bankDetailsText.setVisible(false);
            contentPane.getChildren().add(bankDetailsText);

            // Set actions for the buttons
            mailButton.setOnAction(event -> handleMailButton(contentPane, debt, mailButton));
            markReceivedButton.setOnAction(event -> markReceived(debt, titledPane));
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
            bankButton.setOpacity(1.0);
            bankDetailsText.setVisible(true);
        } else {
            bankButton.setOpacity(0.5);
            bankDetailsText.setVisible(false);
        }
    }

    /**
     * Handles the action when the "Mark Received" button is clicked.
     *
     * @param debt       The open debt.
     * @param titledPane
     */
    public void markReceived(Debt debt, TitledPane titledPane) {
        debt.settleDebt();
        debtList.remove(debt);
        accordionDebts.getPanes().remove(titledPane);
        if (debtList.isEmpty()) {
            noDebtMessage.setVisible(true);
        }
    }

    /**
     * Handles the action when the "Mail" button is clicked.
     *
     * @param contentPane The content of the debt.
     * @param debt        The open debt.
     * @param mailButton
     */
    public void handleMailButton(AnchorPane contentPane, Debt debt, ToggleButton mailButton) {
        // Handles different actions based on if the button was toggled on or off at first
        // (by the presence of the "send reminder" button)
        if (contentPane.getChildren().stream().noneMatch(node -> node instanceof Button)) {
            // If no button is present (it was toggled off), add a new button (now toggle on)
            mailButton.setOpacity(1.0);
            Text emailConfiguredText = new Text(bundle.getString("emailConfigured"));
            Button sendReminder = new Button(bundle.getString("sendReminder"));
            sendReminder.setOnAction(event -> sendReminder(debt));
            contentPane.getChildren().add(sendReminder);

            // Set the positioning of the entities, current values are just a guess
            AnchorPane.setTopAnchor(emailConfiguredText, 10.0);
            AnchorPane.setLeftAnchor(emailConfiguredText, 10.0);
            AnchorPane.setTopAnchor(sendReminder, 30.0);
            AnchorPane.setLeftAnchor(sendReminder, 10.0);
        } else {
            // If a button is present (it was toggled on), remove it (now toggle off)
            mailButton.setOpacity(0.5);
            contentPane.getChildren().removeIf(node -> node instanceof Button);
        }
    }

    /**
     * Handles the action when the "send reminder" button is clicked.
     *
     * @param debt The open debt.
     */
    public void sendReminder(Debt debt) {
        String reminder = bundle.getString("dear") + debt.getDebtor().getUsername() + ",\n\n" +
                bundle.getString("reminderStart") + "\n\n" + debt + "\n\n" + bundle.getString("reminderEnd")
                + "\n\n" + debt.getCreditor().getUsername();
        // toDo, something like SendMail(debt.getDebtor().getEmail, reminder);
    }

    private ImageView generateIcons(String path) {
        String iconPath = "file:client/src/main/resources/" + path + ".png";
        Image image = new Image(iconPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        return imageView;
    }

    /**
     * go back to overview page
     */
    public void abortDebts() {
        debtList.clear();
        accordionDebts.getPanes().clear();
        mainCtrl.showOverview(event);
    }
    /**
     * Getter for the event
     * @return the event
     */
    public Event getEvent() {
        return event;
    }
}
