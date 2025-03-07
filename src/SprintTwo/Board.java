package SprintTwo;

public class Board {
    private int[][] grid;
    private String turn = "Blue";
    private int boardSize;
    private int gameMode = 1; // 1 --> simple, 2 --> general

    public Board(int _boardSize) {
        boardSize = _boardSize;
        grid = new int[boardSize][boardSize];
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

    public int getBoardSize() { return boardSize; }

    public String getGameMode() {
        return gameMode == 1 ? "Simple" : "General";
    }

    public void setGameModeSimple() {
        gameMode = 1;
    }

    public void setGameModeGeneral() {
        gameMode = 2;
    }

    public void resetBoard(int boardSize) {
        this.boardSize = boardSize;
        turn = "Blue";
        grid = new int[boardSize][boardSize];
    }

    public void makeMove(int row, int column, boolean isS) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize
                && grid[row][column] == 0) {
            grid[row][column] = isS ? 1 : 2;
            turn = (turn == "Red") ? "Blue" : "Red";
        }
    }

}
