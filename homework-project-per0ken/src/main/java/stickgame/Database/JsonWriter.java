package stickgame.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.tinylog.Logger;


/**
 * This class stands for writing a JSON file with the results of the game.
 */
public class JsonWriter {

    /**
     * Stores the previous game results.
     */
    private List<GameResult> results = new ArrayList<>();

    /**
     * This function gets an Integer and String
     * as a pair, reads the last json database and stores
     * it in a list, finally it adds the new pair to the list.
     * @param result is an Integer and String pair.
     * @return with all the results as a list.
     */
    public static List<GameResult> addResult(
            final GameResult result) throws FileNotFoundException {

        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(
                new FileReader("stickgame-history.json"));
        Logger.info("Game history have been read.");


        List<GameResult> results = gson.fromJson(
                reader, new TypeToken<List<GameResult>>() {

                } .getType()
        );

        if (results == null) {
            results = new ArrayList<>();
        }

        results.add(result);
        Logger.info("New result added.");


        return results;
    }

    /**
     * This function stands for converting the data to JSON format.
     * @return the results in JSON format.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Logger.info("Result converted to JSON format.");
        return gson.toJson(results);
    }

    /**
     * The function creates a new list and
     * gives it to the JsonWriter's List and write a file with
     * all the results.
     * @param gameResult is result of the game as a String and Integer
     * pair object.
     */
    public static void writeFile(
            final GameResult gameResult) throws IOException {
        JsonWriter data = new JsonWriter();
        data.results = addResult(gameResult);

        File file = new File("stickgame-history.json");
        FileWriter writer = new FileWriter(file);

        writer.write(data.toJson());
        Logger.info("Result is saved in the JSON file.");
        writer.close();
    }
}
