package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import se233.chapter2.Launcher;
import se233.chapter2.controller.AllEventHandlers;
import se233.chapter2.controller.FetchData;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TopPane extends FlowPane {
    private Button refresh;
    private Button add;

    public Label getUpdate() {
        return update;
    }

    public void setUpdate(Label update) {
        this.update = update;
    }

    public Button getAdd() {
        return add;
    }

    public void setAdd(Button add) {
        this.add = add;
    }

    public Button getRefresh() {
        return refresh;
    }

    public void setRefresh(Button refresh) {
        this.refresh = refresh;
    }

    private Label update;

    private ComboBox<String> baseSelector;

    public TopPane() {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setPrefSize(640, 20);
        add = new Button("Add");
        refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onRefresh();
            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onAdd();
            }
        });
        update = new Label();
        baseSelector = new ComboBox<>();
        baseSelector.getItems().addAll("AUD", "CAD", "CHF", "CNY", "EUR", "GBP", "HKD",
                "IDR", "INR", "JPY", "MYR", "NZD", "PHP", "RUB",
                "SAR", "SGD", "THB", "TWD", "USD", "VND");
        baseSelector.setValue(Launcher.getBaseCurrency());
        baseSelector.setOnAction(e -> {
            String newBase = baseSelector.getValue();
            Launcher.setBaseCurrency(newBase);

            List<Currency> updatedList = new ArrayList<>();

            for (Currency c : Launcher.getCurrencyList()) {
                // Skip if the currency matches the new base
                if (c.getShortCode().equalsIgnoreCase(newBase)) continue;

                List<CurrencyEntity> hist = FetchData.fetchRange(c.getShortCode(), 30);

                // Only proceed if valid data was fetched
                if (hist != null && !hist.isEmpty()) {
                    c.setHistorical(hist);
                    c.setCurrent(hist.get(hist.size() - 1));
                    updatedList.add(c);
                }
            }

            // If the list becomes empty after removing the base, add a fallback
            if (updatedList.isEmpty()) {
                String fallback = !newBase.equals("THB") ? "THB" : "USD";
                Currency fallbackCurrency = new Currency(fallback);
                List<CurrencyEntity> fallbackHist = FetchData.fetchRange(fallback, 30);
                if (fallbackHist != null && !fallbackHist.isEmpty()) {
                    fallbackCurrency.setHistorical(fallbackHist);
                    fallbackCurrency.setCurrent(fallbackHist.get(fallbackHist.size() - 1));
                    updatedList.add(fallbackCurrency);
                }
            }

            Launcher.setCurrencyList(updatedList);

            try {
                Launcher.refreshPane();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        refreshPane();
        this.getChildren().addAll(refresh, add, baseSelector, update);
    }
    public void refreshPane() {
        update.setText(String.format("Last update: %s", LocalDateTime.now().toString()));
    }
}
