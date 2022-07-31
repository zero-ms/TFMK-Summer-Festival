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
        Bukkit.broadcastMessage(String.valueOf(gamePlayers.size()));
    }
    public static void showScoreBoard() {
        for (GamePlayer gamePlayer : gamePlayers) {
            ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            Objective dummyObjective = scoreboard.registerNewObjective(gamePlayer.getPlayer().getName(), "dummy", translate("&6TFMK-Hide&Seek"));
            dummyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score playerRole = dummyObjective.getScore(String.format(translate("&f역할: &b%s"), gamePlayer.getFinalPlayerType().name()));
            playerRole.setScore(5);
            Score remainingTaggers = dummyObjective.getScore(String.format(translate("&f남은 술래: &c%d"), GameScore.getTaggerCount()));
            remainingTaggers.setScore(4);
            Score remainingRunners = dummyObjective.getScore(String.format(translate("&f남은 도망자: &a%d"), GameScore.getRunnerCount()));
            remainingRunners.setScore(3);
            Score pickedUpKeyScore = dummyObjective.getScore(String.format(translate("&f획득한 키 갯수: &e%d"), GameScore.getPickedUpKeyScore()));
            pickedUpKeyScore.setScore(2);
            Score remainingTime = dummyObjective.getScore(String.format(translate("&f남은 시간: &6%s"), getRemainingTime()));
            remainingTime.setScore(1);

            gamePlayer.getPlayer().setScoreboard(scoreboard);
        }
    }

    private static String getRemainingTime() {
        Long currentTime = System.currentTimeMillis();
        Long diff = GameScore.getEndTime() - currentTime;
        diff /= 1000;
        return diff.toString();
    }
}
