package gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DifficultyWindow extends Stage {
    private String difficulty;

    public DifficultyWindow(String title) {
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);


        setTitle(title);
        GridPane pane = new GridPane();
        pane.setPrefSize(200,50);
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void initContent(GridPane pane){
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        Button buttonEasy = new Button("Easy");
        buttonEasy.setOnAction(e -> easyAction());
        Button buttonMedium = new Button("Medium");
        buttonMedium.setOnAction(e -> mediumAction());
        Button buttonHard = new Button("Hard");
        buttonHard.setOnAction(e -> hardAction());

        pane.add(buttonEasy, 1,0);
        pane.add(buttonMedium, 2,0);
        pane.add(buttonHard, 3,0);
    }

    private void hardAction() {
        difficulty = "hard";
        hide();
    }

    private void mediumAction() {
        difficulty = "medium";
        hide();
    }

    private void easyAction() {
        difficulty = "easy";
        hide();
    }
}
