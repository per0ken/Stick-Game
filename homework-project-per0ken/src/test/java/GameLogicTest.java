import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stickgame.Model.*;

import java.util.List;
import java.util.Set;

public class GameLogicTest {
    private GameState gameState;

    @BeforeEach
    public void init() {
        gameState = new GameState();
    }

    @Test
    public void testGetCurrentPlayerColor() {
        PlayerColor currentPlayerColor = gameState.getCurrentPlayerColor();
        Assertions.assertEquals(PlayerColor.RED, currentPlayerColor);
    }

    @Test
    public void testGetWinnerScore() {
        int winnerScore = gameState.getWinnerScore();
        Assertions.assertEquals(0, winnerScore);
    }

    @Test
    public void testGetRedSquares() {
        int redSquares = gameState.getRedSquares();
        Assertions.assertEquals(0, redSquares);
    }

    @Test
    public void testGetBlueSquares() {
        int blueSquares = gameState.getBlueSquares();
        Assertions.assertEquals(0, blueSquares);
    }

    @Test
    public void testAreAllLinesColored() {
        boolean allLinesColored = gameState.areAllLinesColored();
        Assertions.assertFalse(allLinesColored);
    }

    @Test
    public void testGetWinner() {
        String winner = gameState.getWinner();
        Assertions.assertEquals("Tie", winner);
    }

    @Test
    public void testGetOwnerOfLineByPosition() {
        LineOwner lineOwner = gameState.getOwnerOfLineByPosition(1, 1);
        Assertions.assertNull(lineOwner);
    }

    @Test
    public void testEndTurn() {
        gameState.endTurn();
        PlayerColor currentPlayerColor = gameState.getCurrentPlayerColor();
        Assertions.assertEquals(PlayerColor.BLUE, currentPlayerColor);
    }

    @Test
    public void testIsSquareFilled() {
        boolean squareFilled = gameState.isSquareFilled(new Position(0, 0));
        Assertions.assertFalse(squareFilled);
    }

    @Test
    public void testAddFilledSquare() {
        gameState.addFilledSquare(PlayerColor.RED, new Position(0, 0));
        int redSquares = gameState.getRedSquares();
        Assertions.assertEquals(1, redSquares);
    }

    @Test
    public void testGetNewFilledSquares() {
        Set<Pair<Position, SquareOwner>> newFilledSquares = gameState.getNewFilledSquares();
        Assertions.assertNotNull(newFilledSquares);
        Assertions.assertEquals(0, newFilledSquares.size());
    }

    @Test
    public void testGetLinesOfSquare() {
        List<LineOwner> linesOfSquare = gameState.getLinesOfSquare(0, 0);
        Assertions.assertNotNull(linesOfSquare);
        Assertions.assertEquals(4, linesOfSquare.size());
    }

}
