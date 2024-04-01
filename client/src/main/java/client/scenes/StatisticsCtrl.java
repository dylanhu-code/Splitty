package client.scenes;

import client.utils.StatisticsUtils;
import commons.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalExpenseLabel;
    private Scene statistics;
    private Stage primaryStage;
    private Event event;
    private StatisticsUtils utils = new StatisticsUtils();

    public void initalize(Stage primaryStage, Scene statistics, Event event)  {
        this.event = event;
        this.primaryStage = primaryStage;
        this.statistics = statistics;
        updateTotalExpense(utils.calculateTotalExpense(event.getExpenses()));
        updatePieChartData(utils.generatePieChartData(event.getExpenses()));

        primaryStage.setScene(statistics);
        primaryStage.show();
    }

    public void updatePieChartData(ObservableList<PieChart.Data> pieChartData) {
        pieChart.setData(pieChartData);
    }

    public void updateTotalExpense(double totalExpense) {
        totalExpenseLabel.setText("Total Expense: " + totalExpense);
    }
}
