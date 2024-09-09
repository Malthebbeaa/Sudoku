package gui;

import Solver.SudokuGenerator;
import Solver.SudokuSolver;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Random;

public class GUI extends Application {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private TextField[][] textFields = new TextField[GRID_SIZE][GRID_SIZE];

    private SudokuGenerator sudokuGenerator = new SudokuGenerator();
    private String difficulty = "";
    private int[][] board = sudokuGenerator.generatePuzzle("medium");
    private int[][] solvedBoard = sudokuGenerator.getSolvedBoard();

    @Override
    public void start(Stage stage) throws Exception {
        GridPane sudokuGrid = new GridPane();
        sudokuGrid.setPadding(new Insets(10));
        sudokuGrid.setHgap(10);
        sudokuGrid.setVgap(10);

        // Create subgrids
        for (int row = 0; row < SUBGRID_SIZE; row++) {
            for (int column = 0; column < SUBGRID_SIZE; column++) {
                GridPane subGrid = createSubgrid(row, column);
                sudokuGrid.add(subGrid, column, row); // rækkefølgen på column og row er omvendt
            }
        }

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetSudoku());
        Button checkButton = new Button("Check");
        checkButton.setOnAction(e -> checkAction());
        Button solveButton = new Button("Solve");
        solveButton.setOnAction(e -> solveSudoku());
        Button hintButton = new Button("Hint");
        hintButton.setOnAction(e -> getHintAction());

        sudokuGrid.add(resetButton, 0, 4);
        sudokuGrid.add(checkButton, 1, 4);
        sudokuGrid.add(solveButton, 2, 4);
        sudokuGrid.add(hintButton, 0,5);

        StackPane root = new StackPane(sudokuGrid);
        Scene scene = new Scene(root, 450, 450);
        stage.setScene(scene);
        stage.setTitle("Sudoku");
        stage.show();
    }

    private void getHintAction(){
        Random random = new Random();
        TextField currentField;
        boolean notFound = false;
        int attempts = 0;
        while (!notFound && attempts < 100){
            int randomRow = random.nextInt(GRID_SIZE);
            int randomColumn = random.nextInt(GRID_SIZE);
            currentField = textFields[randomRow][randomColumn];
            if (currentField.getText().isEmpty()){
                currentField.setText(String.valueOf(solvedBoard[randomRow][randomColumn]));
                currentField.setStyle("-fx-background-color: lightgreen; -fx-alignment: center;");
                currentField.setEditable(false);
                notFound = true;
            }
            attempts++;
        }
    }

    private void checkAction() {
        //mangler stadig noget der fjerner rød skrift efter tallet er fjernet
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                TextField currentField = textFields[row][column];
                if (currentField.isEditable() && !currentField.getText().isEmpty()) {
                    int userInput = Integer.parseInt(currentField.getText());
                    if (userInput == solvedBoard[row][column]) {
                        currentField.setStyle("-fx-background-color: lightgreen; -fx-alignment: center;");
                    } else if (currentField.getText().isEmpty()) {
                        currentField.setStyle("-fx-background-color: white");
                    } else {
                        currentField.setStyle("-fx-background-color: red; -fx-alignment: center;");
                    }
                }
            }
        }
    }

    private void resetSudoku() {
        DifficultyWindow difficultyWindow = new DifficultyWindow("Difficulty");
        difficultyWindow.showAndWait();

        if (difficultyWindow.getDifficulty() != null) {
            difficulty = difficultyWindow.getDifficulty();
            board = sudokuGenerator.generatePuzzle(difficulty);
            solvedBoard = sudokuGenerator.getSolvedBoard();
            updateGrid();
        }
    }

    private GridPane createSubgrid(int subGridRow, int subGridColumn) {
        GridPane subGrid = new GridPane();
        subGrid.setPadding(new Insets(5));
        subGrid.setHgap(5);
        subGrid.setVgap(5);
        subGrid.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        for (int row = 0; row < SUBGRID_SIZE; row++) {
            for (int column = 0; column < SUBGRID_SIZE; column++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-alignment: center;");

                int globalRow = subGridRow * SUBGRID_SIZE + row;
                int globalCol = subGridColumn * SUBGRID_SIZE + column;


                int value = board[globalRow][globalCol];
                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setEditable(false);
                    cell.setStyle("-fx-background-color: lightgray; -fx-alignment: center;");
                }

                textFields[globalRow][globalCol] = cell;
                subGrid.add(cell, column, row);
            }
        }

        return subGrid;
    }

    private void solveSudoku() {
        if (SudokuSolver.solveBoard(board)) {
            updateGrid();
        } else {
            System.out.println("Unsolvable...");
        }
    }

    private void updateGrid() {
        clearGrid();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                TextField cell = textFields[row][column];
                cell.setStyle("-fx-alignment: center;");
                if (board[row][column] != 0) {
                    cell.setEditable(false);
                    cell.setStyle("-fx-background-color: lightgray; -fx-alignment: center;");
                    cell.setText(String.valueOf(board[row][column]));
                }
            }
        }
    }


    private void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                textFields[row][column].clear();
                textFields[row][column].setEditable(true);
                textFields[row][column].setStyle("-fx-background-color: white");
            }
        }
    }
}
