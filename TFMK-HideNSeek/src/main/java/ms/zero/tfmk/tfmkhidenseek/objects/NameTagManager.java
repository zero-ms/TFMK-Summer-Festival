package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class NameTagManager {
    public static void hideNameTag(List<Player> players) {
        ArrayList<String> playerNames = new ArrayList<>();
        players.forEach(player -> {
            playerNames.add(player.getName());
        });
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
            scoreboardTeam.setPlayers(playerNames);
            scoreboardTeam.sendPacket(player);
        }
    }

    public static void showNameTag(List<Player> players) {
        ArrayList<String> playersNameList = new ArrayList<>();
        players.forEach(player -> {
            playersNameList.add(player.getName());
        });
        for (Player player : players) {
            WrapperPlayServerScoreboardTeam scoreboardTeam = new WrapperPlayServerScoreboardTeam();
            scoreboardTeam.setName("hidenseek-team");
            scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
            scoreboardTeam.setPlayers(playersNameList);
            scoreboardTeam.sendPacket(player);
        }
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
        scoreboardTeam.setCollisionRule("always");
        scoreboardTeam.sendPacket(targetPlayer);

        scoreboardTeam = new WrapperPlayServerScoreboardTeam();
        scoreboardTeam.setName("hidenseek-team");
        scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        scoreboardTeam.setPlayers(Arrays.asList(targetEntityName));
        scoreboardTeam.sendPacket(targetPlayer);

    }

    public static void showNameTag(Player targetPlayer, String name) {
        WrapperPlayServerScoreboardTeam scoreboardTeam = new WrapperPlayServerScoreboardTeam();
        scoreboardTeam.setName("hidenseek-team");
        scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        scoreboardTeam.setPlayers(Arrays.asList(name));
        scoreboardTeam.sendPacket(targetPlayer);
    }
}
