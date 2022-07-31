package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class GameScore {
    private static Integer droppedKeyScore = 0;
    private static Integer pickedUpKeyScore = 0;
    private static Integer taggerCount = 0;
    private static Integer runnerCount = 0;

    private static Long endTime = 0L;

    public static void initScores() {
        droppedKeyScore = 0;
        pickedUpKeyScore = 0;
        taggerCount = 0;
        runnerCount = 0;
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

    public static void setEndTime(Long timeMillis) {
        endTime = timeMillis;
    }

    public static Long getEndTime() {
        return endTime;
    }

    public static void runnerDroppedKey(Integer keyScore) {
        droppedKeyScore += keyScore;
        pickedUpKeyScore -= keyScore;
    }

    public static void runnerPickedUpKey() {
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
    public static ArrayList<GamePlayer> getKillRanking() {
        ArrayList<GamePlayer> gamePlayers = GameManager.getGamePlayerSet().stream().filter(gamePlayer -> gamePlayer.getFinalPlayerType() == GameRule.PlayerType.TAGGER).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < gamePlayers.size() - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < gamePlayers.size(); j++) {
                if (gamePlayers.get(j).getKillScore() > gamePlayers.get(minIndex).getKillScore()) {
                    minIndex = j;
                }
            }
            GamePlayer swapped = gamePlayers.get(i);
            gamePlayers.set(i, gamePlayers.get(minIndex));
            gamePlayers.set(minIndex, swapped);
        }

        return gamePlayers;
    }

    public static ArrayList<GamePlayer> getPickedUpKeyRanking() {
        ArrayList<GamePlayer> gamePlayers = GameManager.getGamePlayerSet().stream().filter(gamePlayer -> gamePlayer.getInitialPlayerType() == GameRule.PlayerType.RUNNER).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < gamePlayers.size() - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < gamePlayers.size(); j++) {
                if (gamePlayers.get(j).getPickedUpKeyScore() > gamePlayers.get(minIndex).getPickedUpKeyScore()) {
                    minIndex = j;
                }
            }
            GamePlayer swapped = gamePlayers.get(i);
            gamePlayers.set(i, gamePlayers.get(minIndex));
            gamePlayers.set(minIndex, swapped);
        }

        return gamePlayers;
    }
}
