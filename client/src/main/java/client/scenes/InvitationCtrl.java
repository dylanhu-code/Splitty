package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static client.scenes.StartScreenCtrl.currentLocale;

public class InvitationCtrl {

    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Event event;
    private Stage primaryStage;
    private Scene invitation;
    private ResourceBundle bundle;

    @FXML
    private Text title;
    @FXML
    private Text inviteCode;
    @FXML
    public Text infoText1;
    @FXML
    public Text infoText2;
    @FXML
    private TextArea inviteesText;
    @FXML
    private Button sendInvites;
    @FXML
    private Button abortInvitation;

    /**
     * Constructs an instance of InvitationCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public InvitationCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param invitation   The page with its controller.
     * @param event        The event.
     */
    public void initialize(Stage primaryStage, Scene invitation, Event event) {
        this.primaryStage = primaryStage;
        this.invitation = invitation;
        this.event = event;

        primaryStage.setScene(invitation);
        primaryStage.show();

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        title.setText(event.getTitle());
        inviteCode.setText(event.getInviteCode());
    }

    private void updateUI() {
        infoText1.setText(bundle.getString("infoText1"));
        infoText2.setText(bundle.getString("infoText2"));
        sendInvites.setText(bundle.getString("sendInvitesButton"));
        abortInvitation.setText(bundle.getString("abortInvitationButton"));
    }

    /**
     * Handles the action when the "Send Invites" button is clicked.
     */
    @FXML
    private void sendInvites() {
        String emailAddresses = inviteesText.getText().trim();
        if (!emailAddresses.isEmpty()) {
            String[] addresses = emailAddresses.split("\\r?\\n");
            for (int i = 0; i < addresses.length; i++) {
                //toDO smth like sendMail(addresses[i], inviteCode.getText());
            }
        }
        inviteesText.clear();
        mainCtrl.showOverview(event);
        mainCtrl.showOverview(event);
    }

    /**
     * getter for the event
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * getter for the primary stage(needed for the tests)
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * getter for the overview scene
     * @return the scene
     */
    public Scene getOverview() {
        return invitation;
    }

    @FXML
    void abortInvitation() {
        mainCtrl.showOverview(event);
    }

}

