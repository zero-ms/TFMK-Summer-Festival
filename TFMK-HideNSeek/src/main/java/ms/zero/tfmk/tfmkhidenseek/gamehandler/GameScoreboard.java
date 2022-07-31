package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.*;

public class GameScoreboard {
    private static ArrayList<GamePlayer> gamePlayers;

    public static void initApplyList(ArrayList<GamePlayer> gamePlayers) {
        GameScoreboard.gamePlayers = new ArrayList<>(gamePlayers);
    }
    public static void updateScoreBoard() {
        for (GamePlayer gamePlayer : gamePlayers) {
            ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            Objective dummyObjective = scoreboard.registerNewObjective(gamePlayer.getPlayer().getName(), "dummy", translate("&6TFMK-Hide&Seek"));
            dummyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score playerRole = dummyObjective.getScore(String.format(translate("&f역할: &b%s"), gamePlayer.getFinalPlayerType().name()));
            playerRole.setScore(5);
            Score remainingTaggers = dummyObjective.getScore(String.format(translate("&f남은 술래: &c%d&f명"), GameScore.getTaggerCount()));
            remainingTaggers.setScore(4);
            Score remainingRunners = dummyObjective.getScore(String.format(translate("&f남은 도망자: &a%d&f명"), GameScore.getRunnerCount()));
            remainingRunners.setScore(3);
            Score pickedUpKeyScore = dummyObjective.getScore(String.format(translate("&f남은 키 갯수: &e%d&f개"), GameRule.getNeedKey() - GameScore.getPickedUpKeyScore()));
            pickedUpKeyScore.setScore(2);
            Score remainingTime = dummyObjective.getScore(String.format(translate("&f남은 시간: &6%s"), getRemainingTime()));
            remainingTime.setScore(1);

            gamePlayer.getPlayer().setScoreboard(scoreboard);
        }
    }

    private static String getRemainingTime() {
        Long currentTime = System.currentTimeMillis();
        Long differentTime = GameScore.getEndTime() - currentTime;
        differentTime /= 1000;
        return formatingTime(differentTime.intValue());
    }

    private static String formatingTime(Integer unformattedTime) {
        int minutes = unformattedTime / 60;
        int seconds = unformattedTime - (60 * minutes);

        if (minutes > 0) {
            return String.format("%d분 %d초", minutes, seconds);
        } else {
            return String.format("%d초", seconds);
        }
    }
}
