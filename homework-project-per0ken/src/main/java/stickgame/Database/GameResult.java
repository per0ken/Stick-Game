package stickgame.Database;

public final class GameResult {

    /**
     * This variable will is the score,
     * the second element of the GameResult
     * object that will be used to store
     * the game result in a JSON file.
     */
    private final int score;

    /**
     * This variable will is the player,
     * the first element of the GameResult
     * object that will be used to store
     * the game result in a JSON file.
     */
    private final String player;

    /**
     * This is a setter method for the GameResult object.
     *
     * @param newscore will be the score element.
     * @param newplayer will be the player element.
     */
    public GameResult(final int newscore, final String newplayer) {
        this.player = newplayer;
        this.score = newscore;
    }

    /**
     * Getter method for the score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter method for the player.
     *
     * @return the player
     */
    public String getPlayer() {
        return player;
    }
}
