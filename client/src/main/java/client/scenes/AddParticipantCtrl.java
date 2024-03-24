package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static client.scenes.StartScreenCtrl.currentLocale;

public class AddParticipantCtrl {
    private final ServerUtils server;
    private Scene addParticipant;
    private Event currentEvent;
    private Stage primaryStage;
    private ResourceBundle bundle;
    private final SplittyMainCtrl mainCtrl;

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
     * @param primaryStage The primary container of this page
     * @param addParticipant     The page with its controller
     * @param event        The event
     */
    public void initialize(Stage primaryStage, Scene addParticipant, Event event) {
        this.primaryStage = primaryStage;
        this.addParticipant = addParticipant;
        this.currentEvent = event;

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        primaryStage.setScene(addParticipant);
        primaryStage.show();
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
    private User getUser(){
        return null;
    }

    /**
     * clears text fields
     */
    private void clearFields(){
        name.clear();
        email.clear();
        iban.clear();
        bic.clear();
    }

    /**
     *
     * @param e key that is pressed
     */
    public void keyInput(KeyEvent e){
        switch (e.getCode()){
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
    private void abortAdding() {
        clearFields();
        mainCtrl.showOverview(currentEvent);
    }

    @FXML
    private void addParticipant() {
        //TODO adding a participant needs to be implemented
    }
}
