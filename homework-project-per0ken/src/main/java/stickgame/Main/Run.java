package stickgame.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.util.Objects;

/**
 * Represents the application.
 */
public class Run extends Application {

    /**
     * Loads the MenuScene and displays it on the monitor.
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final int width = 300;
        final int height = 300;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                .getResource("/GUI/MenuScene.fxml")));
        primaryStage.setTitle("StickGame");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();
        Logger.info("Game opened");
    }

    /**
     * Launches the application.
     * @param args Opens the MenuScene.
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
