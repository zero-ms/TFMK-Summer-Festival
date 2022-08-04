package ms.zero.tfmk.tfmkhidenseek.nms;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmkhidenseek.global.Util.translate;

public class NameTagManager {
    public static void hideEachPlayersNameTag(List<Player> players) {
        for (Player player : players) {
            WrapperPlayServerScoreboardTeam scoreboardTeam = new WrapperPlayServerScoreboardTeam();
            scoreboardTeam.setName("hidenseek-team");
            scoreboardTeam.setDisplayName(WrappedChatComponent.fromText(""));
            scoreboardTeam.setNameTagVisibility("never");
            scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            scoreboardTeam.setCollisionRule("always");
            scoreboardTeam.setColor(ChatColor.WHITE);
            scoreboardTeam.setPrefix(WrappedChatComponent.fromText(translate("&b[PLAYING] &f")));
            scoreboardTeam.sendPacket(player);

            scoreboardTeam = new WrapperPlayServerScoreboardTeam();
            scoreboardTeam.setName("hidenseek-team");
            scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
            scoreboardTeam.setPlayers(players.stream().map(Player::getName).collect(Collectors.toList()));
            scoreboardTeam.sendPacket(player);
        }
    }

    public static void showEachPlayerNameTag(List<Player> players) {
        for (Player player : players) {
            WrapperPlayServerScoreboardTeam scoreboardTeam = new WrapperPlayServerScoreboardTeam();
            scoreboardTeam.setName("hidenseek-team");
            scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
            scoreboardTeam.setPlayers(players.stream().map(Player::getName).collect(Collectors.toList()));
            scoreboardTeam.sendPacket(player);
        }
        updatePlayerPrefix();
    }

    private static void updatePlayerPrefix() throws NullPointerException {
        Class tfmkTitlePlugin = Bukkit.getPluginManager().getPlugin("Tfmktitle").getClass();
        try {
            Method updateMethod = tfmkTitlePlugin.getMethod("updatePlayersTeam", null);
            updateMethod.invoke(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void hideNameTag(Player targetPlayer, String targetEntityName) {
        WrapperPlayServerScoreboardTeam scoreboardTeam = new WrapperPlayServerScoreboardTeam();
        scoreboardTeam.setName("hidenseek-team");
        scoreboardTeam.setDisplayName(WrappedChatComponent.fromText(""));
        scoreboardTeam.setNameTagVisibility("never");
        scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        scoreboardTeam.setCollisionRule("never");
        scoreboardTeam.sendPacket(targetPlayer);

        scoreboardTeam = new WrapperPlayServerScoreboardTeam();
        scoreboardTeam.setName("hidenseek-team");
        scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        scoreboardTeam.setPlayers(Collections.singletonList(targetEntityName));
        scoreboardTeam.sendPacket(targetPlayer);

    }

    public static void showNameTag(Player targetPlayer, String name) {
        WrapperPlayServerScoreboardTeam scoreboardTeam = new WrapperPlayServerScoreboardTeam();
        scoreboardTeam.setName("hidenseek-team");
        scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        scoreboardTeam.setPlayers(Collections.singletonList(name));
        scoreboardTeam.sendPacket(targetPlayer);
    }
}
