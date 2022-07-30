package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.*;

public class GameScoreboard {
    private Player player;
    private Scoreboard scoreboard;

    public GameScoreboard(Player player) {
        this.player = player;
    }

    public void createNewScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        Objective dummyObjective = scoreboard.registerNewObjective(player.getName(), "dummy", "TFMK-Hide&Seek");
        dummyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score playerName = dummyObjective.getScore(String.format(translate("&e이름: %s"), player.getName()));
        Score playerRole = dummyObjective.getScore(String.format(translate("&b역할: %s"), GameManager.getRole(player)));
        Score remainingTaggers = dummyObjective.getScore(String.format(translate("&c남은 술래: %d"), GameScore.getTaggerCount()));
        Score remainingRunners = dummyObjective.getScore(String.format(translate("&b남은 도망자: %d"), GameScore.getRunnerCount()));
    }

    public void applyScoreboard() {

    }

    public void updateScoreboard() {

    }
}
