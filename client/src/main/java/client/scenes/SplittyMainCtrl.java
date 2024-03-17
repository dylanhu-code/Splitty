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

    public void initialize(Stage primaryStage, Pair<OverviewCtrl, Parent> overview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<AddParticipantCtrl, Parent> addParticipant
                           ,Pair<AddExpenseCtrl, Parent> addExpense
                            ) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        showStartScreen();
        primaryStage.show();
    }

    /**
     * used to show the overview of a certain event.
     */
    public void showOverview(){
        primaryStage.setTitle("Event overview");
        primaryStage.setScene(overview);
//        overviewCtrl.showOverview();
        // overviewCtrl.refresh(); TODO should also be implemented such that it shows specific event
    }

    public void showStartScreen(){
        primaryStage.setTitle("Start screen");
        primaryStage.setScene(startScreen);
    }

    public void showAddParticipant(){
        primaryStage.setTitle("Add Participant");
        primaryStage.setScene(addParticipant);
    }

    public void showAddExpense(){
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpense);
    }
}
