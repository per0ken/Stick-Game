package stickgame.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class JsonReader {

    /**
     * This method is for reading the results from the JSON file.
     * @return the results as a list or null if there is no list
     */
    public List<GameResult> readJsonFile() {
        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(
                    new FileReader("stickgame-history.json"));
            Type type = new TypeToken<List<GameResult>>() {
            }.getType();

            return gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
