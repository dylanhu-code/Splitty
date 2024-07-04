package client.scenes;

import client.utils.StatisticsUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.ResourceBundle;


public class StatisticsCtrl {
    @FXML
    public PieChart pieChart;
    @FXML
    public Label totalExpenseText;

    @FXML
    public Label totalExpenseLabel;
    @FXML
    public Button backButtonStat;
    @FXML
    public Label eventTitle;
    @FXML
    public VBox legend;

    public Event event;
    private SplittyMainCtrl mainCtrl;
    private  StatisticsUtils utils;
    private ResourceBundle bundle;

    /**
     * Constructor -
     * @param mainCtrl - Splitty Main controller
     * @param utils - the server utils
     */
    @Inject
    public StatisticsCtrl(SplittyMainCtrl mainCtrl, StatisticsUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    /**
     * Initializes the statistics page
     *
     * @param event The specified event.
     */
    public void initialize(Event event)  {
        this.event = event;
        eventTitle.setText(event.getTitle());
        pieChart.getData().clear();
        var pieData = utils.generatePieChartData(event.getExpenses());
        updateTotalExpense(utils.calculateTotalExpense(event.getExpenses()));
        updatePieChartData(pieData);
        utils.setSliceColors(pieChart, event.getExpenses());
        utils.createLegend(legend, event.getExpenses());

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
     * updates the language on the fields
     */
    private void updateUI() {
        backButtonStat.setText(bundle.getString("backButtonStat"));
        totalExpenseText.setText(bundle.getString("totalExpenseText"));
    }

    /**
     * Update the pie chart data
     *
     * @param pieChartData - new pie chart data
     */
    public void updatePieChartData(ObservableList<PieChart.Data> pieChartData) {
        pieChart.setData(pieChartData);
    }

    /**
     * updates total expense amount
     *
     * @param totalExpense - new expense amount
     */
    public void updateTotalExpense(double totalExpense) {
        totalExpenseLabel.setText(String.format("%.2f", totalExpense));
    }

    /**
     * Goes back to the overview page
     */
    public void goBack() {
        mainCtrl.showOverview(event, "-1");

    }


    /**
     * Handles actions when common keys are pressed
     *
     * @param e key event taking place
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            goBack();
        }
    }
}
