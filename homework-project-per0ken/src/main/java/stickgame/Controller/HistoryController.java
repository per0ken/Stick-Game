package stickgame.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.tinylog.Logger;

import stickgame.Database.JsonReader;
import stickgame.Database.GameResult;

import java.util.List;

public final class HistoryController {

    /**
     * Variable to store the FXML table object.
     */
    @FXML
    private TableView<GameResult> table;

    /**
     * Variable to store the FXML table's object winner column.
     */
    @FXML
    private TableColumn<GameResult, String> winner;

    /**
     * Variable to store the FXML table's object score column.
     */
    @FXML
    private TableColumn<GameResult, Integer> score;

    /**
     * This method reads the results from the JSON file
     * to a list and converts it to an ObservableList
     * which helps the application to insert it to
     * the FXML table.
     */
    @FXML
    public void initialize() {
        JsonReader reader = new JsonReader();
        List<GameResult> results = reader.readJsonFile();

        if (results != null) {
            ObservableList<GameResult> gameResults =  FXCollections
                    .observableArrayList(results);
            winner.setCellValueFactory(
                    new PropertyValueFactory<>("player"));
            score.setCellValueFactory(
                    new PropertyValueFactory<>("score"));
            table.setItems(gameResults);
            Logger.info("Game results loaded successfully.");
        } else {
            Logger.info("Game results could not load.");
        }
    }
}
