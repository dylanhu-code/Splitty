package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddParticipantCtrl {
    private final ServerUtils server;
    private Scene addParticipant;
    private Event currentEvent;
    private Stage primaryStage;
    private ResourceBundle bundle;
    private final SplittyMainCtrl mainCtrl;
    private Locale currentLocale;

    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField iban;
    @FXML
    private TextField bic;
    @FXML
    private Button abortParticipantButton;
    @FXML
    private Button addParticipantButton;
    @FXML
    public Text titleText;
    @FXML
    public Text contactDetailsText;
    @FXML
    public Text nameText;
    @FXML
    public Text emailText;
    @FXML
    public Text ibanText;
    @FXML
    public Text bicText;
    public Participant currentP;

    /**
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public AddParticipantCtrl(ServerUtils server, SplittyMainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage   The primary container of this page
     * @param addParticipant The page with its controller
     * @param event          The event
     * @param participant - the participant to edit (if that is the case)
     */
    public void initialize(Stage primaryStage, Scene addParticipant,
                           Event event, Participant participant) {
        this.primaryStage = primaryStage;
        this.addParticipant = addParticipant;
        this.currentEvent = event;

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
        if (participant != null) {
            name.setText(participant.getName());
            email.setText(participant.getEmail());
            iban.setText(participant.getBankAccount());
            bic.setText(participant.getBic());
            addParticipantButton.setText("Edit");
        }
        this.currentP = participant;
        primaryStage.setScene(addParticipant);
        primaryStage.show();
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
     * Updates to preferred language
     */
    private void updateUI() {
        titleText.setText(bundle.getString("titleParticipantText"));
        contactDetailsText.setText(bundle.getString("contactDetailsText"));
        abortParticipantButton.setText(bundle.getString("abortParticipantButton"));
        addParticipantButton.setText(bundle.getString("addParticipantButton"));
        nameText.setText(bundle.getString("nameTextField"));
        emailText.setText(bundle.getString("emailTextField"));
        ibanText.setText(bundle.getString("ibanTextField"));
        bicText.setText(bundle.getString("bicTextField"));

    }

    /**
     *
     * @return User from text boxes
     */
    private Participant getParticipant() {
        if(name.getText().isEmpty() || email.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
        }
        else
            return new Participant(name.getText(), email.getText(), iban.getText(), bic.getText());
        return null;
    }


    /**
     * clears text fields
     */
    private void clearFields(){
        if(name != null)
            name.clear();
        if(email != null)
            email.clear();
        if(iban != null)
            iban.clear();
        if(bic != null)
            bic.clear();
    }

    /**
     *
     * @param e key that is pressed
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                addParticipant();
                break;
            case ESCAPE:
                abortAdding();
                break;
            default:
                break;
        }
    }

    @FXML
    void abortAdding() {
        clearFields();
        mainCtrl.showOverview(currentEvent);
    }

    @FXML
    private void addParticipant() {
        try {
            if (currentP != null) {
                Participant editedParticipant = getParticipant();
                currentP = server.updateParticipant(currentP.getUserId(), editedParticipant);
                currentEvent = server.getEventById(currentEvent.getEventId());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Participant edited");
                alert.setHeaderText(null);
                alert.setContentText("Participant " + editedParticipant.getName() +
                        " is edited successfully in event " + currentEvent.getTitle());
                alert.showAndWait();
                currentEvent = server.updateEvent(currentEvent.getEventId(), currentEvent);
            } else {
                if(!isValidEmail(email.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please add a valid email");
                    alert.showAndWait();
                }
                else{
                Participant p = getParticipant();
                Participant savedParticipant = server.addParticipant(p);
                currentEvent.addParticipant(savedParticipant);
                currentEvent = server.updateEvent(currentEvent.getEventId(), currentEvent);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Participant added");
                alert.setHeaderText(null);
                alert.setContentText("Participant " + savedParticipant.getName() +
                        " is successfully added to event " + currentEvent.getTitle());
                alert.showAndWait();
                }
            }

        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showOverview(currentEvent);
    }
    /**
     * Getter for the current event
     * @return the event
     */
    public Event getEvent() {
        return currentEvent;
    }

    /**
     * Method that checks that the email inputted follows the correct format
     * @param email - email provided by the user
     * @return - true if the email is valid
     */
    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)" +
                "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        boolean valid = matcher.matches();
        return valid;
    }
}
