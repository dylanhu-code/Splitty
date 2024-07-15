package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.util.ResourceBundle;

public class EditNameCtrl {

    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private ResourceBundle bundle;
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
     * @param event         - the event to edit
     */
    public void initialize(Event event) {
        this.event = event;

        eventNameText.setText(event.getTitle());
        bundle = ResourceBundle.getBundle("messages", mainCtrl.getCurrentLocale());
        updateUI();
    }

    /**
     * updates the bundle
     */
    public void updateLocale() {
        bundle = ResourceBundle.getBundle("messages", mainCtrl.getCurrentLocale());
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
        mainCtrl.showOverview(event, "-1");
    }

    /**
     * Apply the new name
     */
    public void setApplyButton() {
        String newName = eventNameText.getText();
        event.setTitle(newName);
        long eventId = event.getEventId();
        server.updateEvent(eventId, event);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Title edited");
        alert.setHeaderText(null);
        alert.setContentText( "The title of the event is successfully edited to " + newName);
        alert.showAndWait();
        mainCtrl.showOverview(event, "-1");
    }

    /**
     * Handles actions when common keys are pressed
     *
     * @param e key event taking place
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                setApplyButton();
                break;
            case ESCAPE:
                setCancelButton();
                break;
            default:
                break;
        }
    }
}
