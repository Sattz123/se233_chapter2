package se233.chapter2.controller.draw;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class DrawGraphTask implements Callable<VBox> {
    Currency currency;
    public DrawGraphTask(Currency currency) {
        this.currency = currency;
    }
    @Override
    public VBox call() throws Exception {
        VBox graphPane = new VBox(10);
        graphPane.setPadding(new Insets(0, 25, 5, 25));
        CategoryAxis xAxis = new CategoryAxis();
//        xAxis.setAutoRanging(false); // Stop it from skipping categories
//        xAxis.setTickLabelRotation(90); // Make labels vertical
//        xAxis.setTickLabelGap(5);       // Add gap
//        xAxis.setTickLabelFont(new Font("Arial", 8)); // Smaller font
//
//// Tell xAxis to use all the actual dates
//        List<String> categories = currency.getHistorical().stream()
//                .map(CurrencyEntity::getTimestamp)
//                .collect(Collectors.toList());
//        xAxis.setCategories(FXCollections.observableArrayList(categories));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        LineChart<String, Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setLegendVisible(false);
        if (this.currency != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;
            for (CurrencyEntity c : currency.getHistorical()) {
                series.getData().add(new XYChart.Data<>(c.getTimestamp(),c.getRate()));
                if(c.getRate() > maxY) maxY = c.getRate();
                if(c.getRate() < minY) minY = c.getRate();
            }
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(minY-(maxY-minY)/2);
            yAxis.setUpperBound(maxY+(maxY-minY)/2);
            yAxis.setTickUnit((maxY-minY)/2);
            lineChart.getData().add(series);
        }
//        ScrollPane scrollPane = new ScrollPane(lineChart);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setFitToWidth(false);
//        graphPane.getChildren().add(scrollPane);
        graphPane.getChildren().add(lineChart);
        return graphPane;
    }
}
