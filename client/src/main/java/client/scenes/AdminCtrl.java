package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private Scene overview;

    @FXML
    private PasswordField passwordField;

    /**
     * Constructs an instance of AdminCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public AdminCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param overview     The page with its controller.
     */
    public void initialize(Stage primaryStage, Scene overview) {
        this.primaryStage = primaryStage;
        this.overview = overview;
    }

    /**
     * validates the admin information when the button is pressed
     */
    public void validate() {
        String password = passwordField.getText();

        if (isValid(password)) {
            // Navigate to the management overview screen
            // You would have some method to switch the scene or update the UI
            // For example:
            // switchScene("managementOverview.fxml");
        } else {
            // Display error message to the user
            // For example:
            // showError("Invalid username or password");
        }
        clearField();
    }

    /**
     * Validate the admin information
     * @param password the password associated with the username
     * @return true if the information is correct and false otherwise
     */
    public boolean isValid(String password) {
        // TODO check against the database
        return true;
    }

    /**
     * Clears all input fields.
     */
    private void clearField() {
        if(passwordField != null)
            passwordField.clear();
    }
    /**
     * Goes back to the start screen
     */
    public void back(){
        mainCtrl.showStartScreen();
    }
}
