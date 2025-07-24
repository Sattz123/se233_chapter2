package se233.chapter2.controller.draw;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.concurrent.Callable;

public class DrawTopAreaTask implements Callable<HBox> {
    private final Button watch;
    private final Button unwatch;
    private final Button delete;

    public DrawTopAreaTask(Button watch, Button unwatch, Button delete) {
        this.watch = watch;
        this.unwatch = unwatch;
        this.delete = delete;
    }

    @Override
    public HBox call() {
        HBox topArea = new HBox(10);
        topArea.setPadding(new Insets(5));
        topArea.getChildren().addAll(watch, unwatch, delete);
        topArea.setAlignment(Pos.CENTER_RIGHT);
        return topArea;
    }
}
