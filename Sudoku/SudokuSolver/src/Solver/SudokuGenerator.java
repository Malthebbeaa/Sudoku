package Solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SudokuGenerator {
    private static final int GRID_SIZE = 9;
    SudokuSolver sudokuSolver = new SudokuSolver();
    private static final int max = 8;
    private static final int min = 0;

    private static final int digitMax = 9;
    private static final int digitMin = 1;

    private static final int easyMin = 36;
    private static final int easyMax = 40;

    private static final int mediumMin = 40;
    private static final int mediumMax = 52;

    private static final int hardMin = 52;
    private static final int hardMax = 72;
    Random random = new Random();

    private int[][] solvedBoard = new int[9][9];

    public int[][] generatePuzzle(String difficulty){
        int[][] sudokuGrid = new int[GRID_SIZE][GRID_SIZE];

        //sørger for at vi fastholder de rigtige boards og ikke ændrer efter vi har lagt det ind
        if (generateCompleteGrid(sudokuGrid)){
            //tager en kopi og sætter ind i boardWithoutRemoved
            solvedBoard = copyGrid(sudokuGrid);
            //returnerer samtidig også det endelige af en kopi
            return removeCells(copyGrid(sudokuGrid), difficulty);
        }

        return null;
    }

    private boolean generateCompleteGrid(int[][] sudokuGrid){
        return generateSudokuGrid(sudokuGrid, 0,0);
    }

    private boolean generateSudokuGrid(int[][] sudokuGrid, int row, int column){
        //sidste række og dermed er sudokuen fyldt
        if (row == GRID_SIZE){
            solvedBoard = sudokuGrid;
            return true;
        }
        //sidste kolonne og dermed går vi videre til næste række
        if (column == GRID_SIZE){
            return generateSudokuGrid(sudokuGrid, row + 1, 0);
        }
        //hvis den er udfyldt rykker vi til næste kolonne
        if (sudokuGrid[row][column] != 0){
            return generateSudokuGrid(sudokuGrid, row, column + 1);
        }

        //shuffler tallene
        ArrayList<Integer> numbers = generateShuffledNumbersForSubGrid(row, column);


        for (int num : numbers){
            //tjekker om tallet er valid
            if (SudokuSolver.isValidPlacement(sudokuGrid, num, row, column)){
                sudokuGrid[row][column] = num; //så kan vi sætte tallet ind
                //recursive backtracking : prøver at fylde næste celle med forrige tal plottet ind
                if (generateSudokuGrid(sudokuGrid, row, column + 1)){
                    return true;
                }
                //hvis ikke den kan laves, sættes det til 0 igen
                sudokuGrid[row][column] = 0;
            }
        }
        return false;
    }

    private ArrayList<Integer> generateShuffledNumbersForSubGrid(int row, int column) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= GRID_SIZE; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return numbers;
    }

    private int[][] removeCells(int[][] grid, String difficulty){
        int min = 0;
        int max = 0;

        //sætter bounds for min og max
        if (difficulty.equals("easy")){
            min = easyMin; max = easyMax;
        } else if (difficulty.equals("medium")){
            min = mediumMin; max = mediumMax;
        } else {
            min = hardMin; max = hardMax;
        }

        int nrOfCellsToRemove = random.nextInt((max - min) + 1) + min;

        for (int i = 0; i < nrOfCellsToRemove; i++) {
            int row = random.nextInt(GRID_SIZE);
            int column = random.nextInt(GRID_SIZE);
            //sørger for at den samme celle ik fjernes to gange
            //kører indtil det er en ny celle
            while (grid[row][column] == 0){
                row = random.nextInt(GRID_SIZE);
                column = random.nextInt(GRID_SIZE);
            }
            //fjerner tal
            grid[row][column] = 0;
        }

        return grid;
    }

    public int[][] copyGrid(int[][] source){
        int[][] destination = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++){
            //kopiere fra source og ligger dem over i vores destination
            System.arraycopy(source[row],0, destination[row], 0, GRID_SIZE);
        }

        return destination;
    }

    public int[][] getSolvedBoard() {
        return solvedBoard;
    }
}
