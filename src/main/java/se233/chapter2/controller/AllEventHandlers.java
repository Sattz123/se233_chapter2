package se233.chapter2.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import se233.chapter2.Launcher;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AllEventHandlers {
    public static void onRefresh() {
        try {
            Launcher.refreshPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void onAdd() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Currency");
        dialog.setContentText("Currency code:");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        Optional<String> code = dialog.showAndWait();
        if (code.isPresent()){
            String inputCode = code.get().trim().toUpperCase();

            List<Currency> currencyList = Launcher.getCurrencyList();
            Currency c = new Currency(inputCode);
            try {
                List<CurrencyEntity> cList = FetchData.fetchRange(c.getShortCode(), 30);
                if (cList == null || cList.isEmpty()) {
                    throw new Exception("Invalid currency code or no data returned.");
                }
                c.setHistorical(cList);
                c.setCurrent(cList.get(cList.size() - 1));
                currencyList.add(c);
                Launcher.setCurrencyList(currencyList);
                Launcher.refreshPane();
            } catch (Exception e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Invalid Currency Code");
                alert.setHeaderText(null);
                alert.setContentText("The currency code \"" + inputCode + "\" is invalid or could not be loaded. Please try again.");
                alert.showAndWait();
            }
        }
    }
    public static void onDelete(String code) {
        try {
            List<Currency> currencyList = Launcher.getCurrencyList();
            int index = -1;
            for(int i=0 ; i<currencyList.size() ; i++) {
                if (currencyList.get(i).getShortCode().equalsIgnoreCase(code) ) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                currencyList.remove(index);
                Launcher.setCurrencyList(currencyList);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void onWatch(String code) {
        try {
            List<Currency> currencyList = Launcher.getCurrencyList();
            int index = -1;
            for (int i = 0; i < currencyList.size(); i++) {
                if (currencyList.get(i).getShortCode().equalsIgnoreCase(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Watch");
                dialog.setContentText("Rate:");
                dialog.setHeaderText(null);
                dialog.setGraphic(null);
                Optional<String> retrievedRate = dialog.showAndWait();
                if (retrievedRate.isPresent() && !retrievedRate.get().isEmpty()) {
                    try {
                        double rate = Double.parseDouble(retrievedRate.get());
                        currencyList.get(index).setWatch(true);
                        currencyList.get(index).setWatchRate(rate);
                        Launcher.setCurrencyList(currencyList);
                        new WatchTask().call();
                        Launcher.refreshPane();

                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Input");
                        alert.setHeaderText("Invalid number format");
                        alert.setContentText("Please enter a valid number for the watch rate.");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void onUnwatch(String code) {
        try {
            List<Currency> currencyList = Launcher.getCurrencyList();
            for (Currency c : currencyList) {
                if (c.getShortCode().equalsIgnoreCase(code)) {
                    c.clearWatch();
                    break;
                }
            }
            Launcher.setCurrencyList(currencyList);
            Launcher.refreshPane();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
