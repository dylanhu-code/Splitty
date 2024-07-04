package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Email;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuBarCtrl implements Initializable {
    private final SplittyMainCtrl mainCtrl;
    private final ServerUtils server;
    private ResourceBundle bundle;

    @FXML
    private MenuItem adminLogIn;

    @FXML
    private Menu adminMenu;

    @FXML
    private Menu languageMenu;

    @FXML
    private MenuItem englishButton;

    @FXML
    private MenuItem dutchButton;

    @FXML
    private MenuItem bulgarianButton;

    @FXML
    private MenuItem templateBtn;

    @FXML
    private MenuItem defaultEmail;

    @FXML
    private Menu currency;

    @FXML
    private MenuItem usdButton;

    @FXML
    private MenuItem eurButton;

    @FXML
    private MenuItem chfButton;

    /**
     * Constructs an instance of MenuBarCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public MenuBarCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = ResourceBundle.getBundle("messages", mainCtrl.getCurrentLocale());

        initLanguageMenu();
        putFlag();
        setCurrencyText();
    }

    public void initLanguageMenu(){
        englishButton.setGraphic(createImageView("en_flag.png"));
        bulgarianButton.setGraphic(createImageView("bg_flag.png"));
        dutchButton.setGraphic(createImageView("nl_flag.png"));
        templateBtn.setGraphic(createImageView("template.png"));
    }

    public ImageView createImageView(String url){
        ImageView imageView = new ImageView();
        Image image = new Image(url, true);
        imageView.setFitWidth(32);
        imageView.setFitHeight(16);
        imageView.setImage(image);
        return imageView;
    }

    /**
     * Goes to the admin login page
     */
    @FXML
    public void goToAdminLogIn() {mainCtrl.showAdminLogin();}

    /**
     * Send a default email, to test whether the email credentials are correct
     * and the email is delivered.
     */
    @FXML
    public void checkDefaultEmail() {
        Email defaultEmail = new Email();
        defaultEmail.setToRecipient(defaultEmail.getEmailUsername());
        defaultEmail.setEmailSubject("Default Email");
        defaultEmail.setEmailBody("Default Body - Checking Credentials/Delivery");
        server.sendEmail(defaultEmail);
    }

    @FXML
    private void setEnglish() {
        ConfigUtils.preferredLanguage = "en";
        mainCtrl.setCurrentLocale(new Locale(ConfigUtils.preferredLanguage));
        putFlag();
        mainCtrl.updateLocale(mainCtrl.getCurrentLocale());
    }

    @FXML
    private void setDutch() {
        ConfigUtils.preferredLanguage = "nl";
        mainCtrl.setCurrentLocale(new Locale(ConfigUtils.preferredLanguage));
        putFlag();
        mainCtrl.updateLocale(mainCtrl.getCurrentLocale());
    }

    @FXML
    private void setBulgarian() {
        ConfigUtils.preferredLanguage = "bg";
        mainCtrl.setCurrentLocale(new Locale(ConfigUtils.preferredLanguage));
        putFlag();
        mainCtrl.updateLocale(mainCtrl.getCurrentLocale());
    }

    /**
     * Handles the download button
     */
    @FXML
    public void downloadTemplate() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save English Properties File");
        fileChooser.getExtensionFilters().add(new FileChooser
                .ExtensionFilter("Properties Files", "*.properties"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Files.copy(Paths.get("src/main/resources/messages_en.properties"),
                        file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Language template downloaded");
                alert.setHeaderText(null);
                alert.setContentText( "Language template downloaded successfully to "
                        + file.getPath());
                alert.getDialogPane().setMinWidth(800);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Put a new Image in the languageMenu.
     */
    public void putFlag() {
        String imagePath;
        String language = mainCtrl.getCurrentLocale().getLanguage();
        imagePath = switch (language) {
            case "bg" -> "bg_flag.png";
            case "nl" -> "nl_flag.png";
            default -> "en_flag.png";
        };

        languageMenu.setGraphic(createImageView(imagePath));
    }

    @FXML
    private void setUsd() {
        ConfigUtils.currency = "USD";
        mainCtrl.setCurrency(ConfigUtils.currency);
        setCurrencyText();
    }

    @FXML
    private void setEur() {
        ConfigUtils.currency = "EUR";
        mainCtrl.setCurrency(ConfigUtils.currency);
        setCurrencyText();
    }

    @FXML
    private void setChf() {
        ConfigUtils.currency = "CHF";
        mainCtrl.setCurrency(ConfigUtils.currency);
        setCurrencyText();
    }

    public void setCurrencyText() {
        currency.setText("Currency (" + mainCtrl.getCurrency() + ")");
    }
}
