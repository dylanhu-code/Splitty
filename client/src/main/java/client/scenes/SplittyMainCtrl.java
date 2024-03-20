package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Controller class for managing different scenes in the application.
 */
public class SplittyMainCtrl {

    private Stage primaryStage;

    private OverviewCtrl overviewCtrl;
    private Scene overview;

    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpense;

    private AddParticipantCtrl addParticipantCtrl;
    private Scene addParticipant;

    private InvitationCtrl invitationCtrl;
    private Scene invitation;

    private OpenDebtsCtrl openDebtsCtrl;
    private Scene openDebts;

    /**
     * Initializes the controller with the primary stage and scenes.
     *
     * @param primaryStage the primary stage of the application
     * @param overview a pair containing the OverviewCtrl and its corresponding parent
     * @param startScreen a pair containing the StartScreenCtrl and its corresponding parent
     * @param addParticipant a pair containing the AddParticipantCtrl and its corresponding parent
     * @param addExpense a pair containing the AddExpenseCtrl and its corresponding parent
     * @param invitation a pair containing the InvitationCtrl and its corresponding parent
     * @param openDebts a pair containing the OpenDebtsCtrl and its corresponding parent
     */
    public void initialize(Stage primaryStage, Pair<OverviewCtrl, Parent> overview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<AddParticipantCtrl, Parent> addParticipant,
                           Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<InvitationCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> openDebts) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());

        this.openDebtsCtrl = openDebts.getKey();
        this.openDebts = new Scene(openDebts.getValue());

        showStartScreen();
        primaryStage.show();
    }

    /**
     * Shows the overview of a certain event.
     */
    public void showOverview(){
        primaryStage.setTitle("Event overview");
        primaryStage.setScene(overview);
        // overviewCtrl.showOverview();
        // overviewCtrl.refresh(); TODO should also be implemented such that it shows specific event
    }

    /**
     * Shows the start screen of the application.
     */
    public void showStartScreen(){
        primaryStage.setTitle("Start screen");
        primaryStage.setScene(startScreen);
    }

    /**
     * Shows the add participant screen.
     */
    public void showAddParticipant(){
        primaryStage.setTitle("Add Participant");
        primaryStage.setScene(addParticipant);
        addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyInput(e));
    }

    /**
     * Shows the add expense screen.
     */
    public void showAddExpense(){
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpense);
        addExpense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
    }

    /**
     * Shows the invitation screen.
     */
    public void showInvitation() {
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
    }

    /**
     * Shows the open debts screen.
     */
    public void showOpenDebts() {
        primaryStage.setTitle("Open Debts");
        primaryStage.setScene(openDebts);
    }
}
