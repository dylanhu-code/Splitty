package client.scenes;

import client.EventStorageManager;
import commons.Event;
import commons.Expense;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Locale;

/**
 * Controller class for managing different scenes in the application.
 */
public class SplittyMainCtrl {

    private Stage primaryStage;
    private OverviewCtrl overviewCtrl;
    private Scene overview;
    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;
    private BackupsCtrl backupsCtrl;
    private Scene backups;
    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpense;
    private AddParticipantCtrl addParticipantCtrl;
    private Scene addParticipant;
    private InvitationCtrl invitationCtrl;
    private Scene invitation;
    private OpenDebtsCtrl openDebtsCtrl;
    private Scene openDebts;
    private AdminLoginCtrl adminCtrl;
    private Scene adminLogin;
    private EventsOverviewCtrl eventsOverviewCtrl;
    private Scene eventsOverview;

    private EventStorageManager storageManager;
    protected static Locale currentLocale = new Locale("en");


    /**
     * Initialises all scenes and controls
     * @param primaryStage - the primary stage
     * @param overview - overviewCtrl and parent pair
     * @param startScreen - StartScreenCtrl and parent pair
     * @param backups - BackupsCtrl and parent pair
     * @param addParticipant - addParticipantCtrl and parent pair
     * @param addExpense - AddExpenseCtrl and parent pair
     * @param invitation - InvitationCtrl and parent pair
     * @param openDebts - DebtsCtl and parent pair
     * @param adminLogin - AdminCtrl and parent pair
     * @param storageManager - the manager for the events in the user file
     */
    public void initialize(Stage primaryStage, Pair<OverviewCtrl, Parent> overview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<BackupsCtrl, Parent> backups,
                           Pair<AddParticipantCtrl, Parent> addParticipant,
                           Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<InvitationCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> openDebts,
                           Pair<EventsOverviewCtrl, Parent> eventsOverview,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           EventStorageManager storageManager) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        this.backupsCtrl = backups.getKey();
        this.backups = new Scene(backups.getValue());

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());

        this.openDebtsCtrl = openDebts.getKey();
        this.openDebts = new Scene(openDebts.getValue());

        this.adminCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());
        this.storageManager = storageManager;

        this.eventsOverviewCtrl = eventsOverview.getKey();
        this.eventsOverview = new Scene(eventsOverview.getValue());

        showStartScreen();
        primaryStage.show();
    }

    /**
     * used to show the overview of a certain event.
     *
     * @param event - current event
     */
    public void showOverview(Event event) {
        primaryStage.setTitle("Event overview");
        overviewCtrl.initialize(primaryStage, overview, event);

        // overviewCtrl.refresh(); TODO should also be implemented such that it shows specific event
    }

    /**
     * Shows the start screen of the application.
     */
    public void showStartScreen() {
        primaryStage.setTitle("Start screen");
        primaryStage.setScene(startScreen);
        startScreenCtrl.refresh();
    }
    /**
     * Shows the start screen of the application.
     */
    public void showBackups(){
        primaryStage.setTitle("Backups");
        backupsCtrl.initialize(primaryStage, backups);
    }

    /**
     * Shows the add participant screen.
     *
     * @param event - current event
     */
    public void showAddParticipant(Event event) {
        primaryStage.setTitle("Add Participant");
        addParticipantCtrl.initialize(primaryStage, addParticipant, event);
        //addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyInput(e));
    }

    /**
     * Initialises the Invitation page
     *
     * @param event - current event
     */
    public void showInvitation(Event event) {
        primaryStage.setTitle("Invitation");
        invitationCtrl.initialize(primaryStage, invitation, event);
    }

    /**
     * Shows the open debts screen.
     *
     * @param event - current event
     */
    public void showOpenDebts(Event event) {
        primaryStage.setTitle("Open Debts");
        openDebtsCtrl.initialize(primaryStage, openDebts, event);
    }

    /**
     * Displays the Edit Expense scene
     * @param expense - the expense the user wants to edit
     * @param event - the event this expense belongs to
     */
    public void showEditExpense(Expense expense, Event event) {
        primaryStage.setTitle("Add/Edit Expense");
        addExpenseCtrl.initialize(primaryStage, addExpense, event, expense);
    }

    /**
     * Shows the admin login screen
     */
    public void showAdmin(){
        primaryStage.setTitle("Admin login");
        adminCtrl.initialize(primaryStage, adminLogin);
    }

    /**
     * Shows the page with all events for the admin
     */
    public void showEventsOverview(){
        primaryStage.setTitle("Events");
        primaryStage.setScene(eventsOverview);
        adminCtrl.initialize(primaryStage, eventsOverview);
    }
}
