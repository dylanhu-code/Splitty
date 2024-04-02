package client.utils;

import commons.Expense;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
            Tag tag = entry.getKey();
            String label = tag.getName() + "\n" + String.format("%.2f", absoluteValue)
                    + " (" + String.format("%.2f", relativeValue) + "%)";
            PieChart.Data data = new PieChart.Data(label, absoluteValue);
            pieChartData.add(data);
        }

        return pieChartData;
    }

    /**
     * it sets the colors of the pie chart to correspond to the color attribute
     * of the expense Tag
     * @param pieChart - the pie chart filled with the expenses by tag
     * @param expenses - the list of expenses
     */
    public void setSliceColors(PieChart pieChart, List<Expense> expenses) {
        ObservableList<PieChart.Data> pieChartData = pieChart.getData();

        for (PieChart.Data slice : pieChartData) {
            String sliceLabel = slice.getName().split("\n")[0]; // Extracting the tag name from the label
            for (Expense expense : expenses) {
                if (expense.getTag().getName().equals(sliceLabel)) {
                    String color = expense.getTag().getColor();
                    slice.getNode().setStyle("-fx-pie-color: " + color + ";");
                    break;
                }
            }
        }
    }

    /**
     * it creates a legend for the pie chart
     * @param legendBox - the box which is then populated
     * @param expenses - list of expenses
     */
    public void createLegend(VBox legendBox, List<Expense> expenses) {
        legendBox.getChildren().clear();
        Map<Tag, Rectangle> tagRectangles = new HashMap<>();

        legendBox.setStyle("-fx-padding: 10;");

        for (Expense expense : expenses) {
            Tag tag = expense.getTag();
            if (!tagRectangles.containsKey(tag)) {
                double absoluteValue = 0;
                double totalExpense = calculateTotalExpense(expenses);
                for (Expense e : expenses) {
                    if (e.getTag().equals(tag)) {
                        absoluteValue += e.getAmount();
                    }
                }
                double relativeValue = (absoluteValue / totalExpense) * 100;

                Rectangle rect = new Rectangle(10, 10, Color.web(tag.getColor()));
                Label nameLabel = new Label(tag.getName());
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                Label valuesLabel = new Label("Absolute: " + String.format("%.2f", absoluteValue) + "\n" +
                        "Relative: " + String.format("%.2f", relativeValue) + "%");
                HBox entry = new HBox(10, rect, nameLabel, valuesLabel);
                legendBox.getChildren().add(entry);
                tagRectangles.put(tag, rect);

                // Add spacing between legend entries
                legendBox.getChildren().add(new Label("\n"));
            }
        }
    }


}
