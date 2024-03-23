package client.scenes;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SplittyMainCtrlTest {

    private SplittyMainCtrl splittyMainCtrl;
    private Stage primaryStage;
    private Scene startScreen;

    @BeforeEach
    public void setUp() {
        splittyMainCtrl = new SplittyMainCtrl();
        primaryStage = new Stage();
        startScreen = new Scene(new StackPane());
    }

    @Test
    void initialize() {

    }

    @Test
    void showOverview() {
    }

    @Test
    void showStartScreen() {

    }

    @Test
    void showAddParticipant() {
    }

    @Test
    void showAddExpense() {
    }

    @Test
    void showInvitation() {
    }

    @Test
    void showOpenDebts() {
    }
}