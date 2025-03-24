package SprintThree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUI extends JFrame {
    public static final int BOARD_DIMENSIONS = 400;
    public static final int X_OFFSET = 130;
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
    private ButtonGroup bluePlayerSOSButtonGroup;
    private ButtonGroup redPlayerSOSButtonGroup;
    private JRadioButton bluePlayerSButton;
    private JRadioButton redPlayerSButton;
    private JRadioButton bluePlayerOButton;
    private JRadioButton redPlayerOButton;
    private JLabel redPlayerLabel;
    private JLabel bluePlayerLabel;
    private JLabel boardSizeInputLabel;
    private JLabel gameModeLabel;
    private JLabel turnLabel;
    private ButtonGroup gameModeButtonGroup;
    private JRadioButton simpleGameButton;
    private JRadioButton generalGameButton;
    private JButton startGameButton;
    private Board board;

    private void updateBoardSize(int size) {
        if (size >= 3 && size <= MAX_BOARD_SIZE)
        {
            board.resetBoard(size);
            cellSize = BOARD_DIMENSIONS / board.getBoardSize();
            adjustedBoardDimensions = cellSize * board.getBoardSize();
            cellPadding = cellSize / 6;
            symbolSize = cellSize - cellPadding * 2;
        }
        else
        {
            throw new NumberFormatException("Size out of bounds!");
        }
    }

    public GUI(Board board) {
        this.board = board;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 600));
        pack();
        setTitle("SOS Game");
        setVisible(true);
    }

    public Board getBoard(){
        return board;
    }

    private void setContentPane(){
        boardSizeInput = new JTextField(Integer.toString(DEFAULT_BOARD_SIZE), 2);
        boardSizeInput.setSize(25, 20);
        boardSizeInput.setLocation(X_OFFSET + (int) (BOARD_DIMENSIONS * 0.4), 40);
        boardSizeInputLabel = new JLabel("Board Size: ");
        boardSizeInputLabel.setSize(150, 20);
        boardSizeInputLabel.setLocation(X_OFFSET + (int) (BOARD_DIMENSIONS * 0.4), 20);

        gameModeLabel = new JLabel("Select a game mode: ");
        gameModeLabel.setSize(150, 20);
        gameModeLabel.setLocation(X_OFFSET, 20);

        simpleGameButton = new JRadioButton("Simple Game", true);
        simpleGameButton.setSize(150, 20);
        simpleGameButton.setLocation(X_OFFSET, 45);

        generalGameButton = new JRadioButton("General Game", true);
        generalGameButton.setSize(150, 20);
        generalGameButton.setLocation( X_OFFSET, 70);

        gameModeButtonGroup = new ButtonGroup();
        gameModeButtonGroup.add(simpleGameButton);
        gameModeButtonGroup.add(generalGameButton);

        startGameButton = new JButton("Start Game!");
        startGameButton.setSize(120, 20);
        startGameButton.setLocation(X_OFFSET + (int) (BOARD_DIMENSIONS * 0.65), 20);
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (simpleGameButton.isSelected()) {
                    board.setGameModeSimple();
                } else {
                    board.setGameModeGeneral();
                }
                try {
                    updateBoardSize(Integer.parseInt(boardSizeInput.getText()));
                } catch (NumberFormatException ex) {
                    updateBoardSize(DEFAULT_BOARD_SIZE);
                    boardSizeInput.setText(Integer.toString(DEFAULT_BOARD_SIZE));
                    JOptionPane.showMessageDialog(gameBoardCanvas, "invalid board size!");
                }
                repaint();
            }
        });
        redPlayerLabel = new JLabel("Red Player: ");
        redPlayerLabel.setSize(150, 20);
        redPlayerLabel.setLocation(BOARD_DIMENSIONS + X_OFFSET + 50, Y_OFFSET);

        redPlayerSButton = new JRadioButton("S", true);
        redPlayerSButton.setSize(50, 20);
        redPlayerSButton.setLocation(BOARD_DIMENSIONS + X_OFFSET + 50, Y_OFFSET + 25);

        redPlayerOButton = new JRadioButton("O");
        redPlayerOButton.setSize(50, 20);
        redPlayerOButton.setLocation(BOARD_DIMENSIONS + X_OFFSET + 50, Y_OFFSET + 50);

        bluePlayerLabel = new JLabel("Blue Player: ");
        bluePlayerLabel.setSize(150, 20);
        bluePlayerLabel.setLocation(20, Y_OFFSET);

        bluePlayerSButton = new JRadioButton("S", true);
        bluePlayerSButton.setSize(50, 20);
        bluePlayerSButton.setLocation(20, Y_OFFSET + 25);

        bluePlayerOButton = new JRadioButton("O");
        bluePlayerOButton.setSize(50, 20);
        bluePlayerOButton.setLocation(20, Y_OFFSET + 50);

        turnLabel = new JLabel("Current Turn: " + board.getTurn());
        turnLabel.setSize(200, 20);
        turnLabel.setLocation(X_OFFSET, Y_OFFSET + BOARD_DIMENSIONS + 10);

        redPlayerSOSButtonGroup = new ButtonGroup();
        redPlayerSOSButtonGroup.add(redPlayerSButton);
        redPlayerSOSButtonGroup.add(redPlayerOButton);

        bluePlayerSOSButtonGroup = new ButtonGroup();
        bluePlayerSOSButtonGroup.add(bluePlayerSButton);
        bluePlayerSOSButtonGroup.add(bluePlayerOButton);

        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setSize(new Dimension(BOARD_DIMENSIONS+GRID_WIDTH, BOARD_DIMENSIONS+GRID_WIDTH));
        gameBoardCanvas.setLocation(X_OFFSET, Y_OFFSET);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(gameBoardCanvas);
        contentPane.add(boardSizeInput);
        contentPane.add(boardSizeInputLabel);
        contentPane.add(redPlayerLabel);
        contentPane.add(redPlayerSButton);
        contentPane.add(redPlayerOButton);
        contentPane.add(bluePlayerLabel);
        contentPane.add(bluePlayerSButton);
        contentPane.add(bluePlayerOButton);
        contentPane.add(gameModeLabel);
        contentPane.add(simpleGameButton);
        contentPane.add(generalGameButton);
        contentPane.add(startGameButton);
        contentPane.add(turnLabel);
    }

    class GameBoardCanvas extends JPanel {

        GameBoardCanvas(){
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int rowSelected = e.getY() / cellSize;
                    int colSelected = e.getX() / cellSize;
                    boolean isS = board.getTurn() == "Blue" ? bluePlayerSButton.isSelected() : redPlayerSButton.isSelected();
                    board.makeMove(rowSelected, colSelected, isS);
                    repaint();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGridLines(g);
            drawBoard(g);
        }

        private void drawGridLines(Graphics g){
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
            turnLabel.setForeground(board.getTurn() == "Blue" ? Color.BLUE : Color.RED);
            turnLabel.setText("Current Turn: " + board.getTurn());
            for (SOSRecord SOS : board.getSOSList()) {
                int x1 = SOS.coordinateStart[1] * cellSize + cellSize/2  + GRID_WIDTH_HALF;
                int y1 = SOS.coordinateStart[0] * cellSize + cellSize/2  + GRID_WIDTH_HALF;
                int x2 = SOS.coordinateEnd[1] * cellSize + cellSize/2  + GRID_WIDTH_HALF;
                int y2 = SOS.coordinateEnd[0] * cellSize + cellSize/2  + GRID_WIDTH_HALF;
                g2d.setColor(SOS.color == "Blue" ? Color.BLUE : Color.RED);
                g2d.drawLine(x1, y1, x2, y2);
            }
            for (int row = 0; row < board.getBoardSize(); row++) {
                for (int col = 0; col < board.getBoardSize(); col++) {
                    int x1 = col * cellSize + cellPadding + GRID_WIDTH_HALF;
                    int y1 = row * cellSize + cellPadding + GRID_WIDTH_HALF;
                    if (board.getCell(row,col) == 1) {
                        g2d.setColor(Color.BLACK);
                        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (cellSize / 1.2)));
                        g2d.drawString("S", x1 + (int) (cellSize * 0.09), y1 + (int) (cellSize * 0.64));
                    } else if (board.getCell(row,col) == 2) {
                        g2d.setColor(Color.BLACK);
                        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (cellSize / 1.2)));
                        g2d.drawString("O", x1 + (int) (cellSize * 0.05), y1 + (int) (cellSize * 0.64));
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
