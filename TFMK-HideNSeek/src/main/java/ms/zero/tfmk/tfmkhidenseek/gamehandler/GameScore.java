package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class GameScore {
    private static HashMap<Player, Integer> keyScoreToPlayer = new HashMap<>();
    private static Integer droppedKeyScore = 0;
    private static Integer pickedUpKeyScore = 0;
    private static Integer taggerCount = 0;
    private static Integer runnerCount = 0;

    public static void initScores() {
        keyScoreToPlayer.clear();
        droppedKeyScore = 0;
        pickedUpKeyScore = 0;
        taggerCount = 0;
        runnerCount = 0;
    }

    public static void addGamePlayer(Player player, Integer keyScore) {
        keyScoreToPlayer.put(player, keyScore);
    }

    public static Integer getPickedUpKeyByPlayer(Player player) {
        return keyScoreToPlayer.getOrDefault(player, 0);
    }

    public static void setDroppedKeyScore(Integer droppedKey) {
        GameScore.droppedKeyScore = droppedKey;
    }

    public static Integer getDroppedKeyScore() {
        return droppedKeyScore;
    }

    public static void setPickedUpKeyScore(Integer pickedUpKey) {
        GameScore.pickedUpKeyScore = pickedUpKey;
    }

    public static Integer getPickedUpKeyScore() {
        return pickedUpKeyScore;
    }

    public static void runnerDroppedKey(Player runner) {
        droppedKeyScore += keyScoreToPlayer.get(runner);
        pickedUpKeyScore -= keyScoreToPlayer.get(runner);
        keyScoreToPlayer.put(runner, 0);
    }

    public static void runnerPickedUpKey(Player runner) {
        keyScoreToPlayer.put(runner, keyScoreToPlayer.get(runner) + 1);
        droppedKeyScore -= 1;
        pickedUpKeyScore += 1;
    }

    public static void initPlayers(Integer taggers, Integer runners) {
        taggerCount = taggers;
        runnerCount = runners;
    }

    public static void decreaseRunner() {
        taggerCount += 1;
        runnerCount -= 1;
    }

    public static Integer getTaggerCount() {
        return taggerCount;
    }

    public static Integer getRunnerCount() {
        return runnerCount;
    }
}
