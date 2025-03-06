package SprintTwo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
    public static final int BOARD_DIMENSIONS = 400;
    public static final int X_OFFSET = 100;
    public static final int Y_OFFSET = 100;
    public static final int DEFAULT_BOARD_SIZE = 10;
    public static final int SYMBOL_STROKE_WIDTH = 4;
    public static final int GRID_WIDTH = 4;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    private final int MAX_BOARD_SIZE = 25;

    private int cellSize = BOARD_DIMENSIONS / DEFAULT_BOARD_SIZE;
    private int adjustedBoardDimensions = cellSize * DEFAULT_BOARD_SIZE;
    private int cellPadding = cellSize / 6;
    private int symbolSize = cellSize - cellPadding * 2;

    private GameBoardCanvas gameBoardCanvas;
    private JTextField boardSizeInput;
    private JLabel boardSizeInputLabel;
    private Board board;

    private int updateBoardSize(int size) {
        if (size >= 3 && size <= MAX_BOARD_SIZE)
        {
            board.setBoardSize(size);
            cellSize = BOARD_DIMENSIONS / board.getBoardSize();
            adjustedBoardDimensions = cellSize * board.getBoardSize();
            cellPadding = cellSize / 6;
            symbolSize = cellSize - cellPadding * 2;
            return 0;
        }
        else
        {
            return -1;
        }
    }

    public GUI(Board board) {
        this.board = board;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        pack();
        setTitle("SOS Game");
        setVisible(true);
    }

    public Board getBoard(){
        return board;
    }

    private void setContentPane(){
        boardSizeInput = new JTextField(Integer.toString(DEFAULT_BOARD_SIZE), 2);
        boardSizeInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    updateBoardSize(Integer.parseInt(boardSizeInput.getText()));
                    System.out.println("RJD try");
                } catch (NumberFormatException ex) {
                    System.out.println("RJD catch");
                    updateBoardSize(DEFAULT_BOARD_SIZE);
                    boardSizeInput.setText(Integer.toString(DEFAULT_BOARD_SIZE));
                    //todo display error
                }
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        boardSizeInput.setSize(25, 20);
        boardSizeInput.setLocation(BOARD_DIMENSIONS + X_OFFSET + 150, Y_OFFSET);
        boardSizeInputLabel = new JLabel("Board Size: ");
        boardSizeInputLabel.setSize(150, 20);
        boardSizeInputLabel.setLocation(BOARD_DIMENSIONS + X_OFFSET + 50, Y_OFFSET);
        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setSize(new Dimension(BOARD_DIMENSIONS+GRID_WIDTH, BOARD_DIMENSIONS+GRID_WIDTH));
        gameBoardCanvas.setLocation(X_OFFSET, Y_OFFSET);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(gameBoardCanvas);
        contentPane.add(boardSizeInput);
        contentPane.add(boardSizeInputLabel);
    }

    class GameBoardCanvas extends JPanel {

        GameBoardCanvas(){
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int rowSelected = e.getY() / cellSize;
                    int colSelected = e.getX() / cellSize;
                    board.makeMove(rowSelected, colSelected);
                    repaint();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            drawGridLines(g);
            drawBoard(g);
        }

        private void drawGridLines(Graphics g){
            System.out.println("RJD drawing grid");
            g.setColor(Color.LIGHT_GRAY);
            for (int row = 0; row <= board.getBoardSize(); row++) {
                g.fillRect(0, cellSize * row,
                        adjustedBoardDimensions+GRID_WIDTH_HALF, GRID_WIDTH);
            }

            for (int col = 0; col <= board.getBoardSize(); col++) {
                g.fillRect(cellSize * col,0,
                        GRID_WIDTH, adjustedBoardDimensions+GRID_WIDTH_HALF);
            }
        }

        private void drawBoard(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < board.getBoardSize(); row++) {
                for (int col = 0; col < board.getBoardSize(); col++) {
                    int x1 = col * cellSize + cellPadding + GRID_WIDTH_HALF;
                    int y1 = row * cellSize + cellPadding + GRID_WIDTH_HALF;
                    if (board.getCell(row,col) == 1) {
                        g2d.setColor(Color.RED);
                        int x2 = (col + 1) * cellSize - cellPadding;
                        int y2 = (row + 1) * cellSize - cellPadding;
                        g2d.drawLine(x1, y1, x2, y2);
                        g2d.drawLine(x2, y1, x1, y2);
                    } else if (board.getCell(row,col) == 2) {
                        g2d.setColor(Color.BLUE);
                        g2d.drawOval(x1, y1, symbolSize, symbolSize);
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI(new Board(DEFAULT_BOARD_SIZE));
            }
        });
    }


}
