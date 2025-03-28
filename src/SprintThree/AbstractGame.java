package SprintThree;

import java.util.ArrayList;

enum GameState {
    ACTIVE,
    RED_WON,
    BLUE_WON,
    DRAW,
    STOPPED,
}

public abstract class AbstractGame {
    protected int[][] grid;
    protected String turn = "Blue";
    protected int boardSize;
    protected int gameMode = 1; // 1 --> simple, 2 --> general
    protected int filledSquareCount;
    protected ArrayList<SOSRecord> SOSList;
    protected GameState gameState = GameState.ACTIVE;

    public ArrayList<SOSRecord> getSOSList() {
        return SOSList;
    }

    public int getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return grid[row][column];
        else
            return -1;
    }

    public String getTurn() {
        return turn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getBoardSize() { return boardSize; }

    public String getGameMode() {
        return gameMode == 1 ? "Simple" : "General";
    }

    public void incrementScore(String color) {
        return;
    }

    public boolean makeMove(int row, int column, boolean isS) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize
                && grid[row][column] == 0) {
            grid[row][column] = isS ? 1 : 2;
            filledSquareCount++;
            return true;
        } else {
            return false;
        }
    }

    public abstract boolean isGameOver(int row, int column);

    protected ArrayList<int[]> getFilledAdjacentSquares(int row, int column) {
        ArrayList<int[]> cells = new ArrayList<>();
        if (row > 0 && column > 0 && getCell(row - 1, column - 1) != 0) cells.add(new int[]{row - 1, column - 1}); //top left
        if (row > 0 && getCell(row - 1, column) != 0) cells.add(new int[]{row - 1, column}); //top middle
        if (row > 0 && column < boardSize - 1 && getCell(row - 1, column + 1) != 0) cells.add(new int[]{row - 1, column + 1}); //top right
        if (column < boardSize - 1 && getCell(row, column + 1) != 0) cells.add(new int[]{row, column + 1}); //right middle
        if (row < boardSize - 1 && column < boardSize - 1 && getCell(row + 1, column + 1) != 0) cells.add(new int[]{row + 1, column + 1}); //bottom right
        if (row < boardSize - 1 && getCell(row + 1, column) != 0) cells.add(new int[]{row + 1, column}); //bottom middle
        if (row < boardSize - 1 && column > 0 && getCell(row + 1, column-1) != 0) cells.add(new int[]{row + 1, column - 1}); //bottom left
        if (column > 0 && getCell(row, column - 1) != 0) cells.add(new int[]{row, column - 1}); //left middle
        //cells.forEach(cell -> System.out.println("Row - " + cell[0] + "  Column - " + cell[1]));
        return cells;
    }

    protected boolean findNewSOSs(int row, int column) {
        boolean foundSOSs = false;
        // if move is S
        if (getCell(row, column) == 1) {
            ArrayList<int[]> cells = getFilledAdjacentSquares(row, column);
            cells.removeIf(cell -> getCell(cell[0], cell[1]) == 1); // remove adjadcent 'S's
            // loop through adjacent 'O's
            for (int[] cell : cells) {
                int[] vector = {cell[0] - row, cell[1] - column};
                if(getCell(cell[0] + vector[0], cell[1] + vector[1]) == 1) {
                    SOSList.add(new SOSRecord(new int[]{cell[0] + vector[0], cell[1] + vector[1]}, new int[]{row, column}, turn));
                    incrementScore(turn);
                    foundSOSs = true;
                }
            }
        } else {
            ArrayList<int[]> cells = getFilledAdjacentSquares(row, column);
            cells.removeIf(cell -> getCell(cell[0], cell[1]) == 2); // remove adjadcent 'O's
            // loop through adjacent 'S's
            for (int[] cell : cells) {
                int[] vector = {cell[0] - row, cell[1] - column};
                if (getCell(row - vector[0], column - vector[1]) == 1) {
                    boolean isDuplicate = false;
                    for (SOSRecord sosRecord : SOSList) {
                        if (sosRecord.equals(new SOSRecord(new int[]{row - vector[0], column - vector[1]}, new int[]{cell[0], cell[1]}, turn))) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        SOSList.add(new SOSRecord(new int[]{row - vector[0], column - vector[1]}, new int[]{cell[0], cell[1]}, turn));
                        incrementScore(turn);
                        foundSOSs = true;
                    }
                }
            }
        }
        return foundSOSs;
    }
}
