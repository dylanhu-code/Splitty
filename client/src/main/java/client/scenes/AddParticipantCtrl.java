package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddParticipantCtrl {
    private final ServerUtils server;

    private final SplittyMainCtrl mainCtrl;
    private Event event;
    private Stage primaryStage;
    private Scene overview;
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField iban;
    @FXML
    private TextField bic;


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
     * @param primaryStage The primary container of this page.
     * @param overview     The page with its controller.
     * @param event        The event.
     */
    public void initialize(Stage primaryStage, Scene overview, Event event) {
        this.primaryStage = primaryStage;
        this.overview = overview;
        this.event = event;
        showAddParticipantScene();
    }

    /**
     * Display the Add Expense Scene
     */
    public void showAddParticipantScene() {
        primaryStage.setTitle("Add Participant");
        primaryStage.setScene(overview);
        primaryStage.show();
    }

    /**
     * used for the abort button.
     */
    public void abort(){
        clearFields();
        mainCtrl.showOverview(event);
    }

    /**
     * used to for the add button, to add a new participant
     */
    public void add(){
        // TODO will have to query whether a user exists, then add the user to an event
        // so also needs a way to know what event it's supposed to add it to.
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
                add();
                break;
            case ESCAPE:
                abort();
                break;
            default:
                break;
        }
    }

}
