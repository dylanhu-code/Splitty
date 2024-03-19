package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

public class InvitationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Stage primaryStage;
    private Pair<OverviewCtrl, Parent> overview;

    @FXML
    private Text title;

    @FXML
    private Text inviteCode;

    @FXML
    private TextArea inviteesText;

    /**
     * Constructs an instance of InvitationCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param overview     The page with its controller.
     * @param event        The event.
     */
    public void initialize(Stage primaryStage, Pair<OverviewCtrl, Parent> overview, Event event) {
        this.primaryStage = primaryStage;
        this.overview = overview;
        this.event = event;

        title.setText(event.getTitle());
        inviteCode.setText(event.getInviteCode());
    }

    /**
     * Handles the action when the "Send Invites" button is clicked.
     */
    private void sendInvites() {
        String emailAddresses = inviteesText.getText().trim();
        if (!emailAddresses.isEmpty()) {
            String[] addresses = emailAddresses.split("\\r?\\n");
            for (int i = 0; i < addresses.length; i++) {
                //toDO smth like sendMail(addresses[i], inviteCode.getText());
            }
        }
        inviteesText.clear();
        mainCtrl.showOverview();
    }
}

