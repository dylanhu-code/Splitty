package client.utils;

import commons.Expense;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsUtils {

    /**
     * Constructor
     */
    public StatisticsUtils() {
    }

    /**
     * Calculate the total expense amount
     * @param expenses - the list of expneses
     * @return - double represnting total event expense amount
     */
    public double calculateTotalExpense(List<Expense> expenses) {
        double totalExpense = 0;
        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }
        return totalExpense;
    }

    /**
     * generates the data that goes in the pie chart, both relative and absolute values
     * @param expenses - list of expense of hte event
     * @return - pie chart data
     */

    public ObservableList<PieChart.Data> generatePieChartData(List<Expense> expenses) {
        Map<Tag, Double> tagExpenses = new HashMap<>();
        double totalExpense = 0;

        for (Expense e : expenses) {
            totalExpense += e.getAmount();
            Tag tag = e.getTag();
            double amount = e.getAmount();
            tagExpenses.put(tag, tagExpenses.getOrDefault(tag, 0.0) + amount);

        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<Tag, Double> entry : tagExpenses.entrySet()) {
            double absoluteValue = entry.getValue();
            double relativeValue = (absoluteValue / totalExpense) * 100;
            String label = entry.getKey().getName() + "\n" + String.format("%.2f", absoluteValue)
                    + " (" + String.format("%.2f", relativeValue) + "%)";
            pieChartData.add(new PieChart.Data(label, absoluteValue));
        }

        return pieChartData;
    }
}
