package client.scenes;

import client.utils.StatisticsUtils;
import commons.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

import static client.scenes.SplittyMainCtrl.currentLocale;

public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalExpenseLabel;
    private Scene statistics;
    private Stage primaryStage;
    private Event event;
    private StatisticsUtils utils = new StatisticsUtils();

    /**
     * Initializes the statistics page
     * @param primaryStage - primary stage
     * @param statistics - statistics scene
     * @param event - specific event
     */

    public void initialize(Stage primaryStage, Scene statistics, Event event)  {
        this.event = event;
        this.primaryStage = primaryStage;
        this.statistics = statistics;
        updateTotalExpense(utils.calculateTotalExpense(event.getExpenses()));
        updatePieChartData(utils.generatePieChartData(event.getExpenses()));

        primaryStage.setScene(statistics);
        primaryStage.show();
    }

    /**
     * Update the pie chart data
     * @param pieChartData - new pie chart data
     */
    public void updatePieChartData(ObservableList<PieChart.Data> pieChartData) {
        pieChart.setData(pieChartData);
    }

    /**
     * updates total expense amount
     * @param totalExpense - new expense amount
     */
    public void updateTotalExpense(double totalExpense) {
        totalExpenseLabel.setText("Total Expense Amount: " + totalExpense);
    }
}
