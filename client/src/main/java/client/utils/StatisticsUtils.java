package client.utils;

import commons.Expense;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatisticsUtils {

    public StatisticsUtils() {
    }

    public double calculateTotalExpense(List<Expense> expenses) {
        double totalExpense = 0;
        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }
        return totalExpense;
    }

    public ObservableList<PieChart.Data> generatePieChartData(List<Expense> expenses) {
        Map<Tag, Double> tagExpenses = new HashMap<>();
        double totalExpense = 0;

        // Calculate total expense and tag expenses
        for (Expense e : expenses) {
            totalExpense += e.getAmount();
            Set<Tag> tags = e.getTags();
            double amount = e.getAmount();
            for (Tag t : tags) {
                tagExpenses.put(t, tagExpenses.getOrDefault(t, 0.0) + amount);
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add pie chart data with absolute and relative values
        for (Map.Entry<Tag, Double> entry : tagExpenses.entrySet()) {
            double absoluteValue = entry.getValue();
            double relativeValue = (absoluteValue / totalExpense) * 100; // Calculate relative value as percentage
            String label = entry.getKey().getName() + "\n" + String.format("%.2f", absoluteValue) + " (" + String.format("%.2f", relativeValue) + "%)";
            pieChartData.add(new PieChart.Data(label, absoluteValue));
        }

        return pieChartData;
    }
}
