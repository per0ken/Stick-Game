package stickgame.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * This class is for navigating to the game scene from the menu scene.
 */
public class MenuController {

    /**
     * This variable is to control the button object
     * of the MenuScene.fxml file.
     */
    @FXML
    private Button playButton;

    /**
     * This variable is to control the navigation bar
     * of the MenuScene.fxml file.
     */
    @FXML
    private MenuItem historyButton;

    @FXML
    private void initialize() {

        playButton.setOnAction(event -> {
                    newgame();
                }
        );
        playButton.setAlignment(Pos.CENTER);
        historyButton.setOnAction(event -> openhistory());
    }

    @FXML
    private void newgame() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/GUI/StickGame.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("StickGame");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            Logger.info("Game scene loaded");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openhistory() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/GUI/StickGameHistory.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("StickGame");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            Logger.info("Game scene loaded");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
