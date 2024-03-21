package client.scenes;

import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

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
     * Initialises all scenes and controls
     * @param primaryStage - the primary stage
     * @param overview - overviewCtrl and parent pair
     * @param startScreen - StartScreenCtrl and parent pair
     * @param addParticipant - addParticipantCtrl and parent pair
     * @param addExpense - AddExpenseCtrl and parent pair
     * @param invitation - InvitationCtrl and parent pair
     * @param openDebts - DebtsCtl and parent pari
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
     * used to show the overview of a certain event.
     * @param event - specific
     */
    public void showOverview(Event event){
        primaryStage.setTitle("Event overview");
        //primaryStage.setScene(overview);
        overviewCtrl.initialize(primaryStage, overview, event);
//        overviewCtrl.showOverview();
        // overviewCtrl.refresh(); TODO should also be implemented such that it shows specific event
    }

    /**
     *
     */
    public void showStartScreen(){
        primaryStage.setTitle("Start screen");
        primaryStage.setScene(startScreen);
    }

    /**
     *
     */
    public void showAddParticipant(){
        primaryStage.setTitle("Add Participant");
        primaryStage.setScene(addParticipant);
        addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyInput(e));
    }

    /**
     *
     */
    public void showAddExpense(Event event){
        primaryStage.setTitle("Add/Edit expense");
        addExpenseCtrl.initialize(primaryStage, addExpense, event);
//        primaryStage.setTitle("Add Expense");
//        primaryStage.setScene(addExpense);
//        addExpense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
    }

    /**
     *
     */
    public void showInvitation(Event event) {
        invitationCtrl.initialize(primaryStage, invitation, event);
//        primaryStage.setTitle("Invitation");
//        primaryStage.setScene(invitation);
    }

    /**
     *
     */
    public void showOpenDebts() {
        primaryStage.setTitle("Open Debts");
        primaryStage.setScene(openDebts);
    }
}
