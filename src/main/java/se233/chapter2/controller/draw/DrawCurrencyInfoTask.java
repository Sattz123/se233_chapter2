package se233.chapter2.controller.draw;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se233.chapter2.model.Currency;

import java.util.concurrent.Callable;

public class DrawCurrencyInfoTask implements Callable<VBox> {
    private Currency currency;

    public DrawCurrencyInfoTask(Currency currency) {
        this.currency = currency;
    }

    @Override
    public VBox call() {
        VBox currencyInfoPane = new VBox(10);
        currencyInfoPane.setAlignment(Pos.CENTER);
        Label exchangeString = new Label(String.format("%s: %.4f", currency.getShortCode(), currency.getCurrent().getRate()));
        exchangeString.setStyle("-fx-font-size: 20;");
        Label watchString = new Label();
        if (currency.getWatch()) {
            watchString.setText(String.format("(Watch @%.4f)", currency.getWatchRate()));
        }
        watchString.setStyle("-fx-font-size: 14;");
        currencyInfoPane.getChildren().addAll(exchangeString, watchString);
        return currencyInfoPane;
    }
}