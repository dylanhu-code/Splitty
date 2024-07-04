package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;

import java.util.ResourceBundle;

public class AdminLoginCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private ResourceBundle bundle;

    @FXML
    public Button backButton;
    @FXML
    public Button loginButton;
    @FXML
    private PasswordField passwordField;

    /**
     * Constructs an instance of AdminCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public AdminLoginCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     */
    public void initialize() {
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
        passwordField.setPromptText(bundle.getString("passwordField"));
        loginButton.setText(bundle.getString("loginButton"));
        backButton.setText(bundle.getString("abortLoginButton"));

    }

    /**
     * Validates the admin information
     */
    public void validate() {
        String password = null;
        if(passwordField != null )
            password = passwordField.getText();

        if (isValid(password)) {
            mainCtrl.showAdmin();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Wrong password. Please try again.");
            alert.showAndWait();
        }
        clearField();
    }

    /**
     * Validate the admin information
     * @param password the password associated with the username
     * @return true if the information is correct and false otherwise
     */
    public boolean isValid(String password) {
        if(password == null) return false;
        String response = server.post("/admin/login", password);
        return "Access granted".equals(response);
    }

    /**
     * Clears all input fields.
     */
    public void clearField() {
        if(passwordField != null)
            passwordField.clear();
    }

    /**
     * Goes back to the start screen
     */
    public void back(){
        mainCtrl.showStartScreen();
    }

    /**
     * Handles actions when common keys are pressed
     * @param e key event taking place
     */
    public void keyPressed(KeyEvent e) {
        switch(e.getCode()){
            case ENTER:
                validate();
                break;
            case ESCAPE:
                back();
                break;
            default:
                break;
        }
    }
}
