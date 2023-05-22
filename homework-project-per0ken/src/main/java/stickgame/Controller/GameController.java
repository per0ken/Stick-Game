package stickgame.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.tinylog.Logger;
import stickgame.Database.JsonWriter;
import stickgame.Database.GameResult;
import stickgame.Model.LineOwner;
import stickgame.Model.GameState;
import stickgame.Model.Position;
import stickgame.Model.PlayerColor;
import stickgame.Model.SquareOwner;

import java.io.IOException;

/**
 * The class that controls the game, draws
 * the 4x4 board with lines, paints the borders.
 */
public final class GameController {

    /**
     * Variable to store the current GameState.
     */
    private GameState gameState;

    /**
     * Variable to earn the GridPane from the StickGame.fxml file.
     */
    @FXML
    private GridPane gridPane;

    /**
     * Variable to set the lines' width.
     */
    private static final int LINE_WIDTH = 100;
    /**
     * Variable to set the lines' thickness.
     */
    private static final int LINE_THICKNESS = 6;

    /**
     * Getter method for the gridPane.
     *
     * @return the gridPane
     */
    @FXML
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Getter method for the GameState.
     *
     * @return @return the gameState.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Draws the borders of the 4x4 board.
     */
    public void initialize() {
        gameState = new GameState();

        gridPane.setGridLinesVisible(false);
        final int gridHeight = 200;
        final int gridWidth = 200;
        gridPane.setPrefSize(gridHeight, gridWidth);


        for (int row = 0; row < GameState.BOARD_SIZE; row++) {
            for (int col = 0; col < GameState.BOARD_SIZE; col++) {
                createVerticalLine(row, col);
                createHorizontalLine(row, col);
            }
        }

        for (int row = 0; row < GameState.BOARD_SIZE; row++) {
            int col = GameState.BOARD_SIZE;
            createHorizontalLine(row, col);
        }

        for (int col = 0; col < GameState.BOARD_SIZE; col++) {
            int row = GameState.BOARD_SIZE;
            createVerticalLine(row, col);
        }
        Logger.info("Grid created of lines");
    }

    /**
     * Handles primary mouse click, paints the clicked
     * line to red or blue based on which player's is coming.
     * @param event gives the mouseclick event
     * to the function to find its position.
     */
    public void handleLineClick(final MouseEvent event) {
        Position eventPosition =
                getPositionOfGridChild((Node) event.getTarget());
        Logger.info("Clicked on {}", eventPosition);

        Line line = (Line) event.getTarget();

        if (event.getButton() != MouseButton.PRIMARY) {
            Logger.debug("Ignoring event from button {}",
                    event.getButton().name());
            return;
        }

        if (gameState.getOwnerOfLineByPosition(
                    eventPosition) != LineOwner.NONE) {
            Logger.info("Line already painted");
            return;
        }

        gameState.selectLineForColor(
                gameState.getCurrentPlayerColor(), eventPosition);

        if (gameState.getCurrentPlayerColor() == PlayerColor.RED) {
            line.setStroke(Color.RED);
            Logger.info("Line painted to red");
        } else {
            line.setStroke(Color.BLUE);
            Logger.info("Line painted to blue");
        }

        updateWithNewSquares();

        if (gameState.areAllLinesColored()) {
            Logger.debug("All lines are painted");
            var result = new GameResult(
                    gameState.getWinnerScore(), gameState.getWinner());

            try {
                JsonWriter.writeFile(result);
            } catch (IOException e) {
                Logger.error("Error while writing game result "
                        + "{} to file. Details: {}", result, e.getMessage());
            }

            endGame(result);
        } else {
            gameState.endTurn();
        }
    }

    /**
     * Creates a horizontal line on the GridPane,
     * it also paints it to red or blue if it is black.
     * @param row is the row on the gridPane.
     * @param col is the column on the gridPane.
     */
    public void createHorizontalLine(final int row, final int col) {
        Line line = new Line(0, 0, LINE_WIDTH, 0);
        line.setStrokeWidth(LINE_THICKNESS);
        line.setOnMouseClicked(this::handleLineClick);
        gridPane.add(line, row * 2 + 1, col * 2);
    }

    /**
     * Creates a vertical line on the GridPane,
     * it also paints it to red or blue if it is black.
     * @param row is the row on the gridPane.
     * @param col is the column on the gridPane.
     */
    public void createVerticalLine(final int row, final int col) {
        Line line = new Line(0, 0, 0, LINE_WIDTH);
        line.setStrokeWidth(LINE_THICKNESS);
        line.setOnMouseClicked(this::handleLineClick);
        gridPane.add(line, row * 2, col * 2 + 1);
    }

    private Position getPositionOfGridChild(final Node node) {
        return new Position(GridPane.getColumnIndex(node),
                GridPane.getRowIndex(node));
    }

    private void endGame(final GameResult gameResult) {
        Stage stage = new Stage();
        stage.setTitle("StickGame");
        stage.show();

        load(stage, new EndGameController(gameResult.getPlayer()));

    }

    private void load(final Stage stage, final Initializable controller) {
        try {
            var fxmlResource = getClass().getResource("/GUI/EndGameScene.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlResource);
            loader.setController(controller);

            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.error("Failed to load FXML.");
        }
    }

    /**
     * Counts how many red or blue squares can be
     * found in the grid and increases the player scores.
     */
    public void updateWithNewSquares() {
        var newFilledSquares = gameState.getNewFilledSquares();

        for (var square : newFilledSquares) {
            var owner = square.getValue();
            var position = square.getKey();
            gameState.addFilledSquare(owner == SquareOwner.RED
                    ? PlayerColor.RED : PlayerColor.BLUE, position);
            Logger.info("Added square to {}", square);

            Position positionInGrid = new Position(
                    2 * position.column() + 1, 2 * position.row() + 1);

            switch (gameState.getCurrentPlayerColor()) {
                case BLUE -> createRectangle(Color.BLUE, positionInGrid);
                case RED -> createRectangle(Color.RED, positionInGrid);
                default -> { }
            }
        }
    }

    /**
     * Creates a rectangle and sets is position
     * to the player's square and fill it with the player's color.
     * @param color is the color we want to fill the rectangle with
     * @param position is the position on the grid where we
     * want to put the rectangle.
     */
    public void createRectangle(final Color color, final Position position) {
        final int rectWidth = 107;
        final int rectHeight = 116;
        final int gridSpan = 2;
        Rectangle rect = new Rectangle();

        rect.setFill(color);
        Logger.info("Square is filled {}", color);

        if (gameState.getCurrentPlayerColor() == PlayerColor.RED) {
            gameState.setCurrentPlayerColor(PlayerColor.BLUE);
        } else {
            gameState.setCurrentPlayerColor(PlayerColor.RED);
        }

        rect.setWidth(rectWidth);
        rect.setHeight(rectHeight);
        rect.toFront();
        GridPane.setRowSpan(rect, gridSpan);
        GridPane.setColumnSpan(rect, gridSpan);
        GridPane.setRowIndex(rect, position.row());
        GridPane.setColumnIndex(rect, position.column());
        gridPane.getChildren().add(rect);
    }
}
