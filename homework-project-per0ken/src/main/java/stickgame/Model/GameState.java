package stickgame.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.util.Pair;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GameState {

    /**
     * This variable is used for initializing the game board
     * and iterate lists within it.
     */
    public static final int BOARD_SIZE = 4;

    /**
     * This variable is used to let the application decide
     * which player's turn is coming.
     */
    private PlayerColor currentPlayerColor;

    /**
     * This variable is used to store the lines' position and
     * their owners.
     */
    private final Map<Position, LineOwner> ownerMapping;

    /**
     * This variable is used to store red player's score.
     */
    private final List<Position> redSquares;
    /**
     * This variable is used to store blue player's score.
     */
    private final List<Position> blueSquares;

    /**
     * This is a getter method for the currentPlayerColor variable.
     *
     * @return the currentPlayerColor
     */
    public PlayerColor getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    /**
     * This is a getter method for the winner's score.
     *
     * @return the size of redSquares or bluSquares
     * depending on which is a bigger list
     */
    public int getWinnerScore() {
        return Math.max(redSquares.size(), blueSquares.size());
    }

    /**
     * This is a getter method for the red player's score.
     *
     * @return the redSquares list size
     */
    public int getRedSquares() {
        return redSquares.size();
    }

    /**
     * This is a getter method for the blue player's score.
     *
     * @return the blueSquares list size
     */
    public int getBlueSquares() {
        return blueSquares.size();
    }

    /**
     * This is a setter method for the red player's score.
     * @param color is the color of the player who will
     * put the next stick on the board.
     */
    public void setCurrentPlayerColor(final PlayerColor color) {
        this.currentPlayerColor = color;
    }

    /**
     * This is method to decide if the game is over.
     *
     * @return true if all the lines are painted.
     */
    public boolean areAllLinesColored() {
        return !ownerMapping.containsValue(LineOwner.NONE);
    }

    /**
     * This is getter method for the winner.
     *
     * @return the player's color as a string
     */
    public String getWinner() {
        if (redSquares.size() > blueSquares.size()) {
            return "Red";
        } else if (redSquares.size() < blueSquares.size()) {
            return "Blue";
        } else {
            return "Tie";
        }
    }

    /**
     * This is getter method for checking who painted
     * the line to blue or red.
     * @param col is the column of the line.
     * @param row is the row of the line.
     * @return the line's position.
     */
    public LineOwner getOwnerOfLineByPosition(final int col, final int row) {
        return this.getOwnerOfLineByPosition(new Position(col, row));
    }

    /**
     * This is a helper method for the getOwnerOfLineByPosition method.
     * @param position is the position of a line.
     * @return the key value of the line by its position
     */
    public LineOwner getOwnerOfLineByPosition(final Position position) {
        return this.ownerMapping.get(position);
    }

    /**
     * This method is to switch the players' turns.
     */
    public void endTurn() {
        this.switchPlayers();
    }

    /**
     * This method is called when the game scene is initialized, it stores
     * the lines in a map with no owner.
     */
    public GameState() {
        currentPlayerColor = PlayerColor.RED;
        ownerMapping = new HashMap<>();

        for (int row = 0; row < GameState.BOARD_SIZE; row++) {
            for (int col = 0; col < GameState.BOARD_SIZE; col++) {
                ownerMapping.put(new Position(
                        row * 2, col * 2 + 1), LineOwner.NONE);
                ownerMapping.put(new Position(
                        row * 2 + 1, col * 2), LineOwner.NONE);
            }
        }

        for (int row = 0; row < GameState.BOARD_SIZE; row++) {
            ownerMapping.put(new Position(
                    row * 2 + 1, GameState.BOARD_SIZE * 2), LineOwner.NONE);
        }

        for (int col = 0; col < GameState.BOARD_SIZE; col++) {
            ownerMapping.put(new Position(
                    GameState.BOARD_SIZE * 2, col * 2 + 1), LineOwner.NONE);
        }

        redSquares = new ArrayList<>();
        blueSquares = new ArrayList<>();
    }

    /**
     * This is a setter method to decide which player have
     * to put the next stick.
     */
    private void switchPlayers() {
        if (this.currentPlayerColor == PlayerColor.RED) {
            this.currentPlayerColor = PlayerColor.BLUE;
        } else {
            this.currentPlayerColor = PlayerColor.RED;
        }
    }

    /**
     * This method decides if a square is already filled because
     * the application counts every square after every click on
     * the lines. We need to avoid counting the same squares again.
     * @param position is the position of a square.
     * @return true or false
     */
    public boolean isSquareFilled(final Position position) {
        return redSquares.contains(position)
                || blueSquares.contains(position);
    }

    /**
     * This method adds a square to the blueSquares
     * of redSquares list depending on which player filled
     * the square.
     * @param ownerColor is the owner color.
     * @param position is the position of the square.
     */
    public void addFilledSquare(
            final PlayerColor ownerColor, final Position position) {
        if (ownerColor == PlayerColor.BLUE) {
            blueSquares.add(position);
        } else {
            redSquares.add(position);
        }
    }

    /**
     * This method is for counting the filled squares on the board.
     * @return the newFilledSquares list if no squares were added to
     * the list
     */
    public Set<Pair<Position, SquareOwner>> getNewFilledSquares() {
        Set<Pair<Position, SquareOwner>> newFilledSquares = new HashSet<>();

        for (int col = 0; col < GameState.BOARD_SIZE; col++) {
            for (int row = 0; row < GameState.BOARD_SIZE; row++) {

                var owner = getSquareOwner(col, row);

                if (owner == SquareOwner.NONE) {
                    continue;
                }

                if (!isSquareFilled(new Position(col, row))) {
                    newFilledSquares.add(new Pair<>(
                            new Position(col, row), owner));
                }
            }
        }

        return newFilledSquares;
    }

    /**
     * This method decides who owns a square. It gets a
     * list with 4 line elements, which form a square.
     * @param squareCol is the column of the square.
     * @param squareRow is the row of the square.
     * @return who own the square
     */
    private SquareOwner getSquareOwner(
            final int squareCol, final int squareRow) {
        List<LineOwner> lineOwners  = getLinesOfSquare(squareCol, squareRow);

        final int maxsize = 4;

        if (lineOwners.stream().anyMatch(owner -> owner == LineOwner.NONE)) {
            return SquareOwner.NONE;
        }

        if (lineOwners.size() == maxsize && lineOwners
                .stream()
                .distinct()
                .count() <= 1) {
            Logger.info("Lines are grouped");
            return lineOwners.get(0) == LineOwner.RED
                    ? SquareOwner.RED
                    : SquareOwner.BLUE;
        }

        return SquareOwner.NONE;
    }

    /**
     * This method set a line's owner to BLUE or RED depending
     * on its colour.
     * @param color is the line's color we want to check.
     * @param position is the position of the lines.
     */
    public void selectLineForColor(
            final PlayerColor color, final Position position) {
        this.ownerMapping.put(position, color == PlayerColor.BLUE
                ? LineOwner.BLUE : LineOwner.RED);
    }

    /**
     * This method decides gets a position of a line, it
     * forms a square by its neighbours.
     * @param squareCol is the column of the square.
     * @param squareRow is the row of the square.
     * @return a list of these lines
     */
    public List<LineOwner> getLinesOfSquare(
            final int squareCol, final int squareRow) {
        if (squareCol >= GameState.BOARD_SIZE
                || squareRow >= GameState.BOARD_SIZE) {
            throw new IllegalArgumentException();
        }

        LineOwner top = getOwnerOfLineByPosition(
                2 * squareCol + 1, 2 * squareRow);
        LineOwner right = getOwnerOfLineByPosition(
                2 * squareCol + 2, 2 * squareRow + 1);
        LineOwner bottom = getOwnerOfLineByPosition(
                2 * squareCol + 1, 2 * squareRow + 2);
        LineOwner left = getOwnerOfLineByPosition(
                2 * squareCol, 2 * squareRow + 1);

        assert top != null;
        assert right != null;
        assert bottom != null;
        assert left != null;
        return List.of(top, right, bottom, left);
    }

}
