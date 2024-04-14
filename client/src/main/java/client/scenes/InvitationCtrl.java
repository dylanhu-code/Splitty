package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import commons.Email;
import commons.Event;
import commons.Participant;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationCtrl {

    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Event event;
    private Stage primaryStage;
    private Scene invitation;
    private ResourceBundle bundle;
    private Locale currentLocale;


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
     * Updates the UI elements with the current locale.
     */
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
    private void sendInvites() throws MalformedURLException {
        checkDefaultEmail();
        String emailAddresses = inviteesText.getText().trim();
        if (!emailAddresses.isEmpty()) {
            String[] addresses = emailAddresses.split("\\r?\\n");
            for (int i = 0; i < addresses.length; i++) {
                String address = addresses[i];
                if (isValidEmail(address)) {
                    Email request = new Email(
                            address,
                            "Invitation to Event!",
                            "Dear " + address.split("@")[0] + "," +
                                    "<br><br>You have been invited to the event: " +
                                    event.getTitle() + "<br><br>The invite code is: " +
                                    event.getInviteCode() + "<br><br>The server URL is: " +
                                    ConfigUtils.readServerUrl("config.txt")
                    );
                    Participant newParticipant = new Participant(address.split("@")[0], address,
                            null, null);
                    server.addParticipant(newParticipant);
                    event.addParticipant(newParticipant);
                    server.sendEmail(request);
                    event = server.updateEvent(event.getEventId(), event);
                    if (i == addresses.length - 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Invitation sent");
                        alert.setHeaderText(null);
                        alert.setContentText("Invitation(s) sent successfully");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a valid email");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No emails are entered");
            alert.showAndWait();
        }
        inviteesText.clear();
        mainCtrl.showOverview(event, "1");
    }

    /**
     * Send a default email, to test whether the email credentials are correct
     * and the email is delivered.
     */
    public void checkDefaultEmail() {
        Email defaultEmail = new Email();
        defaultEmail.setToRecipient(defaultEmail.getEmailUsername());
        defaultEmail.setEmailSubject("Default Email");
        defaultEmail.setEmailBody("Default Body - Checking Credentials/Delivery");
        server.sendEmail(defaultEmail);
    }
    /**
     * Checks if an email is valid or not.
     * @param email The email to check
     * @return Valid or not.
     */
    public boolean isValidEmail(String email) {
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(email);
        return matcher.matches();
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
        mainCtrl.showOverview(event, "1");
    }

    /**
     * Handles the action when common keys are pressed.
     *
     * @param e The key instance.
     */
    public void keyPressed(KeyEvent e) throws MalformedURLException {
        switch (e.getCode()){
            case ENTER:
                sendInvites();
                break;
            case ESCAPE:
                abortInvitation();
            default:
                break;
        }
    }
}

