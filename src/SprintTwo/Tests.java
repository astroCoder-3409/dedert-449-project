package SprintTwo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.SwingUtilities;

class GUITests {

    private GUI gui;
    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        // Create a new Board with the default size and start the GUI on the EDT.
        board = new Board(GUI.DEFAULT_BOARD_SIZE);
        SwingUtilities.invokeAndWait(() -> gui = new GUI(board));
    }

    @AfterEach
    void tearDown() {
        // Dispose the GUI after each test.
        SwingUtilities.invokeLater(() -> gui.dispose());
    }

    // AC 1.1 <Update game board size on input>
    @Test
    void testUpdateBoardSizeOnValidInput() throws Exception {
        // Set board size input to a valid value (e.g., "15")
        Field boardSizeInputField = GUI.class.getDeclaredField("boardSizeInput");
        boardSizeInputField.setAccessible(true);
        JTextField boardSizeInput = (JTextField) boardSizeInputField.get(gui);
        SwingUtilities.invokeAndWait(() -> boardSizeInput.setText("15"));

        // Simulate clicking the "Start Game!" button.
        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // Verify that the Board's size is updated to 15.
        assertEquals(15, board.getBoardSize(), "Board size should be updated to 15 for valid input.");
        // Also, the board size input field should still display "15".
        assertEquals("15", boardSizeInput.getText(), "Board size input field should display '15'.");
    }

    // AC 1.2 <update square size to keep board width similar>
    @Test
    void testSquareSizeAdjustmentKeepsBoardWidthConstant() throws Exception {
        // Use a valid board size (e.g., "15")
        int validSize = 15;
        Field boardSizeInputField = GUI.class.getDeclaredField("boardSizeInput");
        boardSizeInputField.setAccessible(true);
        JTextField boardSizeInput = (JTextField) boardSizeInputField.get(gui);
        SwingUtilities.invokeAndWait(() -> boardSizeInput.setText(String.valueOf(validSize)));

        // Click "Start Game!" to update the board.
        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // Retrieve the private field 'adjustedBoardDimensions'
        Field adjustedBoardDimensionsField = GUI.class.getDeclaredField("adjustedBoardDimensions");
        adjustedBoardDimensionsField.setAccessible(true);
        int adjustedBoardDimensions = adjustedBoardDimensionsField.getInt(gui);

        // The board's overall width should remain near the constant BOARD_DIMENSIONS.
        int boardWidthConstant = GUI.BOARD_DIMENSIONS;
        int tolerance = 10; // allow a small deviation due to integer division
        assertTrue(Math.abs(adjustedBoardDimensions - boardWidthConstant) <= tolerance,
                "Adjusted board dimensions (" + adjustedBoardDimensions +
                        ") should be close to board width constant (" + boardWidthConstant + ").");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "2", "26"})
    void testInvalidBoardSizeInput(String invalidInput) throws Exception {
        // Access the private boardSizeInput field via reflection.
        Field boardSizeInputField = GUI.class.getDeclaredField("boardSizeInput");
        boardSizeInputField.setAccessible(true);
        JTextField boardSizeInput = (JTextField) boardSizeInputField.get(gui);

        // Set the board size input text to an invalid value on the EDT.
        JTextField finalBoardSizeInput = boardSizeInput;
        SwingUtilities.invokeAndWait(() -> finalBoardSizeInput.setText(invalidInput));

        // Schedule a background thread to close any error dialog that may appear.
        new Thread(() -> {
            try {
                Thread.sleep(100); // Wait briefly to let the dialog appear.
                SwingUtilities.invokeLater(() -> {
                    for (Window window : Window.getWindows()) {
                        if (window instanceof JDialog && window.isShowing()) {
                            ((JDialog) window).dispose();
                        }
                    }
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Simulate clicking the "Start Game!" button by accessing it via reflection.
        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // After the button click, the board should have been reset to the default size.
        assertEquals(10, board.getBoardSize(), "Board size should reset to default (10) for invalid input.");

        // Also, the boardSizeInput text should have been reset to the default value.
        boardSizeInput = (JTextField) boardSizeInputField.get(gui);
        assertEquals("10", boardSizeInput.getText(), "Board size input field should display default board size (10) after invalid input.");
    }


    // AC 2.1 <Update game mode game start> - Simple mode
    @Test
    void testUpdateGameModeOnGameStart_Simple() throws Exception {
        // Ensure that the Simple game radio button is selected.
        Field simpleGameButtonField = GUI.class.getDeclaredField("simpleGameButton");
        simpleGameButtonField.setAccessible(true);
        JRadioButton simpleGameButton = (JRadioButton) simpleGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(() -> simpleGameButton.setSelected(true));

        // Click "Start Game!" to update the game mode.
        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // Verify that the game mode is set to Simple.
        assertEquals("Simple", board.getGameMode(), "Game mode should be 'Simple' when Simple is selected.");
    }

    // AC 2.1 <Update game mode game start> - General mode
    @Test
    void testUpdateGameModeOnGameStart_General() throws Exception {
        // Select the General game radio button.
        Field generalGameButtonField = GUI.class.getDeclaredField("generalGameButton");
        generalGameButtonField.setAccessible(true);
        JRadioButton generalGameButton = (JRadioButton) generalGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(() -> generalGameButton.setSelected(true));

        // Click "Start Game!" to update the game mode.
        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // Verify that the game mode is set to General.
        assertEquals("General", board.getGameMode(), "Game mode should be 'General' when General is selected.");
    }

    // AC 2.2 <Only select one game mode>
    @Test
    void testOnlyOneGameModeSelected() throws Exception {
        // Access both game mode radio buttons.
        Field simpleGameButtonField = GUI.class.getDeclaredField("simpleGameButton");
        simpleGameButtonField.setAccessible(true);
        JRadioButton simpleGameButton = (JRadioButton) simpleGameButtonField.get(gui);

        Field generalGameButtonField = GUI.class.getDeclaredField("generalGameButton");
        generalGameButtonField.setAccessible(true);
        JRadioButton generalGameButton = (JRadioButton) generalGameButtonField.get(gui);

        // Simulate selecting the General game button.
        SwingUtilities.invokeAndWait(() -> generalGameButton.doClick());
        assertTrue(generalGameButton.isSelected(), "General game button should be selected.");
        assertFalse(simpleGameButton.isSelected(), "Simple game button should be deselected when General is selected.");

        // Now select the Simple game button.
        SwingUtilities.invokeAndWait(() -> simpleGameButton.doClick());
        assertTrue(simpleGameButton.isSelected(), "Simple game button should be selected.");
        assertFalse(generalGameButton.isSelected(), "General game button should be deselected when Simple is selected.");
    }

    // AC 3.1 <New game clicked, blue’s turn>
    @Test
    void testNewGameClearsBoardAndSetsBlueTurn() throws Exception {
        // Prepopulate the board by making a move at (0,0).
        board.makeMove(0, 0, true);  // Blue move; cell becomes 1 and turn becomes Red.
        assertNotEquals(0, board.getCell(0, 0), "Cell (0,0) should be non-zero before new game.");
        assertEquals("Red", board.getTurn(), "Turn should be Red after the move.");

        // Simulate a new game: set a valid board size and click "Start Game!".
        Field boardSizeInputField = GUI.class.getDeclaredField("boardSizeInput");
        boardSizeInputField.setAccessible(true);
        JTextField boardSizeInput = (JTextField) boardSizeInputField.get(gui);
        SwingUtilities.invokeAndWait(() -> boardSizeInput.setText("10"));

        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // Verify that the board is cleared and turn is reset to Blue.
        assertEquals("Blue", board.getTurn(), "Turn should reset to Blue after new game.");
        for (int row = 0; row < board.getBoardSize(); row++) {
            for (int col = 0; col < board.getBoardSize(); col++) {
                assertEquals(0, board.getCell(row, col), "Board should be cleared on new game.");
            }
        }
    }

    // AC 3.2 <New game clicked, size and gamemode correct>
    @Test
    void testNewGameCorrectSizeAndGameMode() throws Exception {
        // Set board size input and game mode (General in this example).
        Field boardSizeInputField = GUI.class.getDeclaredField("boardSizeInput");
        boardSizeInputField.setAccessible(true);
        JTextField boardSizeInput = (JTextField) boardSizeInputField.get(gui);
        SwingUtilities.invokeAndWait(() -> boardSizeInput.setText("12"));

        Field generalGameButtonField = GUI.class.getDeclaredField("generalGameButton");
        generalGameButtonField.setAccessible(true);
        JRadioButton generalGameButton = (JRadioButton) generalGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(() -> generalGameButton.setSelected(true));

        // Click "Start Game!".
        Field startGameButtonField = GUI.class.getDeclaredField("startGameButton");
        startGameButtonField.setAccessible(true);
        JButton startGameButton = (JButton) startGameButtonField.get(gui);
        SwingUtilities.invokeAndWait(startGameButton::doClick);

        // Verify board size and game mode.
        assertEquals(12, board.getBoardSize(), "Board size should be updated to 12.");
        assertEquals("General", board.getGameMode(), "Game mode should be set to General.");
    }

    // AC 4.1 <Simple move selection>
    @Test
    void testMoveSelectionAllowsSAndOSelection() throws Exception {
        // Access Blue player's move selection radio buttons.
        Field bluePlayerSButtonField = GUI.class.getDeclaredField("bluePlayerSButton");
        bluePlayerSButtonField.setAccessible(true);
        JRadioButton bluePlayerSButton = (JRadioButton) bluePlayerSButtonField.get(gui);

        Field bluePlayerOButtonField = GUI.class.getDeclaredField("bluePlayerOButton");
        bluePlayerOButtonField.setAccessible(true);
        JRadioButton bluePlayerOButton = (JRadioButton) bluePlayerOButtonField.get(gui);

        // Simulate selecting 'O' and check that 'S' becomes deselected.
        SwingUtilities.invokeAndWait(() -> bluePlayerOButton.doClick());
        assertTrue(bluePlayerOButton.isSelected(), "Blue player's 'O' button should be selected.");
        assertFalse(bluePlayerSButton.isSelected(), "Blue player's 'S' button should be deselected.");

        // Now select 'S' and verify.
        SwingUtilities.invokeAndWait(() -> bluePlayerSButton.doClick());
        assertTrue(bluePlayerSButton.isSelected(), "Blue player's 'S' button should be selected.");
        assertFalse(bluePlayerOButton.isSelected(), "Blue player's 'O' button should be deselected.");
    }

    // AC 4.2 <Simple Move Placement vacant square>
    @Test
    void testMovePlacementVacantSquare() throws Exception {
        // Ensure that Blue's move selection is set to 'S' (default).
        Field bluePlayerSButtonField = GUI.class.getDeclaredField("bluePlayerSButton");
        bluePlayerSButtonField.setAccessible(true);
        JRadioButton bluePlayerSButton = (JRadioButton) bluePlayerSButtonField.get(gui);
        SwingUtilities.invokeAndWait(() -> bluePlayerSButton.setSelected(true));

        // Get the GameBoardCanvas component.
        Field gameBoardCanvasField = GUI.class.getDeclaredField("gameBoardCanvas");
        gameBoardCanvasField.setAccessible(true);
        JPanel gameBoardCanvas = (JPanel) gameBoardCanvasField.get(gui);

        // Simulate a mouse click on a vacant square.
        // For a default board of size 10, cellSize = 400/10 = 40, so clicking at (20,20) maps to cell (0,0).
        MouseEvent clickEvent = new MouseEvent(gameBoardCanvas, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, 20, 20, 1, false);
        SwingUtilities.invokeAndWait(() -> gameBoardCanvas.dispatchEvent(clickEvent));

        // Verify that cell (0,0) now contains an 'S' (represented by 1).
        assertEquals(1, board.getCell(0, 0), "Vacant square should now contain an 'S' (1) after move.");
        // And the turn should switch to Red.
        assertEquals("Red", board.getTurn(), "Turn should switch to Red after Blue's move.");
    }

    // AC 4.3 <Simple Move Placement occupied square>
    @Test
    void testMovePlacementOccupiedSquare() throws Exception {
        // Ensure Blue's move selection is set to 'S'.
        Field bluePlayerSButtonField = GUI.class.getDeclaredField("bluePlayerSButton");
        bluePlayerSButtonField.setAccessible(true);
        JRadioButton bluePlayerSButton = (JRadioButton) bluePlayerSButtonField.get(gui);
        SwingUtilities.invokeAndWait(() -> bluePlayerSButton.setSelected(true));

        // Get the GameBoardCanvas component.
        Field gameBoardCanvasField = GUI.class.getDeclaredField("gameBoardCanvas");
        gameBoardCanvasField.setAccessible(true);
        JPanel gameBoardCanvas = (JPanel) gameBoardCanvasField.get(gui);

        // First click on a vacant square (cell (0,0)) as Blue.
        MouseEvent clickEvent1 = new MouseEvent(gameBoardCanvas, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, 20, 20, 1, false);
        SwingUtilities.invokeAndWait(() -> gameBoardCanvas.dispatchEvent(clickEvent1));
        int cellValueAfterFirstMove = board.getCell(0, 0);
        assertEquals(1, cellValueAfterFirstMove, "First move: cell (0,0) should contain 1.");
        // Turn should now be Red.
        assertEquals("Red", board.getTurn(), "After Blue's move, turn should be Red.");

        // Now, simulate a second click on the same occupied square (cell (0,0)) as Red.
        MouseEvent clickEvent2 = new MouseEvent(gameBoardCanvas, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, 20, 20, 1, false);
        SwingUtilities.invokeAndWait(() -> gameBoardCanvas.dispatchEvent(clickEvent2));

        // Since the square is occupied, the value should remain unchanged.
        assertEquals(cellValueAfterFirstMove, board.getCell(0, 0),
                "Occupied square should remain unchanged after an invalid move.");
        // And the turn should remain Red because the move was not accepted.
        assertEquals("Red", board.getTurn(), "Turn should remain Red after attempting move on an occupied square.");
    }
}

