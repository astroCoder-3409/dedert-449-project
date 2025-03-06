package SprintTwo;

public class Board {
    private int[][] grid;
    private char turn = 'X';
    private int boardSize;

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

    public char getTurn() {
        return turn;
    }

    public int getBoardSize() { return boardSize; }

    // todo not really a pure setter
    public void setBoardSize(int _boardSize) {
        boardSize = _boardSize;
        grid = new int[boardSize][boardSize];
    }
    public void makeMove(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize
                && grid[row][column] == 0) {
            grid[row][column] = (turn == 'R') ? 1 : 2;
            turn = (turn == 'R')? 'B' : 'R';
        }
    }

}
