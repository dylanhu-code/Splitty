package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class EditNameCtrl {

    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private Scene editNameScene;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private Event event;

    @FXML
    public Text titleText;
    @FXML
    public Text nameText;
    @FXML
    public TextField eventNameText;
    @FXML
    public Button cancelButton;
    @FXML
    public Button applyButton;

    /**
     * Constructs an instance of AdminCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public EditNameCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param editNameScene     The page with its controller.
     * @param event - the event to edit
     */
    public void initialize(Stage primaryStage, Scene editNameScene, Event event) {
        this.primaryStage = primaryStage;
        this.editNameScene = editNameScene;
        this.event = event;

        primaryStage.setScene(editNameScene);
        primaryStage.show();

        eventNameText.setText(event.getTitle());

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
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
     * Update UI to language setting
     */
    private void updateUI() {
        titleText.setText(bundle.getString("editNameTitle"));
        nameText.setText(bundle.getString("editNameText"));
        cancelButton.setText(bundle.getString("cancelButton"));
        applyButton.setText(bundle.getString("applyButton"));
    }

    /**
     * Cancel the edit name process
     */
    public void setCancelButton() {
        mainCtrl.showOverview(event);
    }

    /**
     * Apply the new name
     */
    public void setApplyButton() {
        String newName = eventNameText.getText();
        event.setTitle(newName);
        long eventId = event.getEventId();
        server.updateEvent(eventId, event);
        mainCtrl.showOverview(event);
    }
}
