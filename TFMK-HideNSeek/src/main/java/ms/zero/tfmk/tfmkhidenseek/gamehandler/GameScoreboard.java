package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.*;

public class GameScoreboard {
    private Player player;
    private Scoreboard scoreboard;

    public GameScoreboard(Player p) {
        this.player = p;
    }

    public void createNewScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective(player.getName(), "dummy", "TFMK-Hide&Seek");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score playerName = obj.getScore(String.format(translate("&e이름: %s"), player.getName()));
        Score playerRole = obj.getScore(String.format(translate("&b역할: %s"), GameManager.getRole(player)));
        Score remainingTaggers = obj.getScore(String.format(translate("&c남은 술래: %d"), GameScore.getTagger()));
        Score remainingRunners = obj.getScore(String.format(translate("&b남은 도망자: %d"), GameScore.getRunner()));
    }

    public void applyScoreboard() {

    }

    public void updateScoreboard() {

    }
}
