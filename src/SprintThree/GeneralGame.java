package SprintThree;

import java.util.ArrayList;

public class GeneralGame extends AbstractGame {
    private int redScore;
    private int blueScore;

    public GeneralGame(int _boardSize) {
        boardSize = _boardSize;
        grid = new int[boardSize][boardSize];
        SOSList = new ArrayList<>();
        gameMode = 2;
        filledSquareCount = 0;
        gameState = GameState.ACTIVE;
        redScore = 0;
        blueScore = 0;
    }

    @Override
    public boolean isGameOver(int row, int column) {
        boolean gameOver = filledSquareCount == boardSize * boardSize;
        if (gameOver) {
            int blueCount = 0;
            int redCount = 0;
            for (SOSRecord sosRecord : SOSList) {
                if (sosRecord.color == "Blue") {
                    blueCount++;
                } else {
                    redCount++;
                }
            }
            if (blueCount > redCount) {
                gameState = GameState.BLUE_WON;
            } else if (blueCount == redCount) {
                gameState = GameState.DRAW;
            } else {
                gameState = GameState.RED_WON;
            }
        }
        return gameOver;
    }

    @Override
    public void incrementScore(String color) {
        if(color == "Blue") {
            blueScore++;
        } else {
            redScore++;
        }
    }

    @Override
    public boolean makeMove(int row, int column, boolean isS) {
        boolean success = super.makeMove(row, column, isS);
        if (success) {
            boolean areThereNewSOSs = findNewSOSs(row, column);
            if (areThereNewSOSs) {
                isGameOver(row, column);
            } else {
                isGameOver(row, column);
                turn = (turn == "Red") ? "Blue" : "Red";
            }
        }
        return success;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getRedScore() {
        return redScore;
    }
}
