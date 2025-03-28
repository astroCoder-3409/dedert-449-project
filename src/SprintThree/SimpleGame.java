package SprintThree;

import java.util.ArrayList;

public class SimpleGame extends AbstractGame {
    public SimpleGame(int _boardSize) {
        boardSize = _boardSize;
        grid = new int[boardSize][boardSize];
        SOSList = new ArrayList<>();
        gameMode = 1;
        filledSquareCount = 0;
        gameState = GameState.ACTIVE;
        turn = "Blue";
    }

    @Override
    public boolean isGameOver(int row, int column) {
        System.out.println(filledSquareCount);
        System.out.println(boardSize);
        boolean gameOver = !SOSList.isEmpty() || filledSquareCount == boardSize * boardSize;
        if (gameOver) {
            if (!SOSList.isEmpty()) {
                if (SOSList.getFirst().color == "Blue") {
                    gameState = GameState.BLUE_WON;
                } else {
                    gameState = GameState.RED_WON;
                }
            } else {
                gameState = GameState.DRAW;
            }
        }
        return gameOver;
    }

    @Override
    public boolean makeMove(int row, int column, boolean isS) {
        boolean success = super.makeMove(row, column, isS);
        if (success) {
            findNewSOSs(row, column);
            isGameOver(row, column);
            turn = (turn == "Red") ? "Blue" : "Red";
        }
        return success;
    }
}
