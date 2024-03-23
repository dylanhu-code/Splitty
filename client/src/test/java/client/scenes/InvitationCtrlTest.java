package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class InvitationCtrlTest {
    private InvitationCtrl invitationCtrl;
    private ServerUtils server;
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setUp() {
        server = new ServerUtils();
        mainCtrl = new SplittyMainCtrl();
        invitationCtrl = new InvitationCtrl(server, mainCtrl);
    }


    @Test
    void initializeTest() {
//        Stage stage = new Stage();
//        Scene scene = new Scene(new StackPane());
//        Event event = new Event("Test Event");
//
//        invitationCtrl.initialize(stage, scene, event);
//
//        assertEquals(stage, invitationCtrl.getPrimaryStage());
//        assertEquals(scene, invitationCtrl.getOverview());
//        assertEquals(event, invitationCtrl.getEvent());

    }

    @Test
    void showInvitePage() {
    }

    @Test
    void back() {
    }
}