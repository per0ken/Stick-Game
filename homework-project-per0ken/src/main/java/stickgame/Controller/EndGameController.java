package stickgame.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is for displaying the winner on a new scene.
 */
public final class EndGameController implements Initializable {

    /**
     * Variable to store the winner of the last game.
     */
    private final String winner;
    /**
     * Variable to control the EndGameScene's
     * label object.
     */
    @FXML
    private Label endLabel;

    /**
     * Setter function for the winner variable.
     * @param newWinner is the new winner we want to set.
     */
    public EndGameController(final String newWinner) {
        this.winner = newWinner;
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        endLabel.setText("Winner is: " + this.winner);
    }
}
