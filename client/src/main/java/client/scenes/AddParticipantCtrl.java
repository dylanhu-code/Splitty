package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class AddParticipantCtrl {
    private final ServerUtils server;

    private final splittyMainCtrl mainCtrl;
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
    public AddParticipantCtrl(ServerUtils server, splittyMainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    /**
     * used for the abort button.
     */
    public void abort(){
        clearFields();
        mainCtrl.showOverview();
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
