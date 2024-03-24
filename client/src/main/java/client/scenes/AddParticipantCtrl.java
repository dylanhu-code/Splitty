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
import javafx.stage.Stage;

public class AddParticipantCtrl {
    private final ServerUtils server;
    private Scene addParticipant;
    private Event currentEvent;
    private Stage primaryStage;

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

        primaryStage.setScene(addParticipant);
        primaryStage.show();
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
