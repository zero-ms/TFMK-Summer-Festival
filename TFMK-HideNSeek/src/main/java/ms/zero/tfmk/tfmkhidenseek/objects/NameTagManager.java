package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            scoreboardTeam.sendPacket(player);

            scoreboardTeam = new WrapperPlayServerScoreboardTeam();
            scoreboardTeam.setName("hidenseek-team");
            scoreboardTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
            scoreboardTeam.setPlayers(playerNames.stream().filter(s -> !player.getName().equals(s)).collect(Collectors.toList()));
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
            scoreboardTeam.setPlayers(playersNameList.stream().filter(s -> !player.getName().equals(s)).collect(Collectors.toList()));
            scoreboardTeam.sendPacket(player);
        }
    }

    public static void hideNameTag(Player targetPlayer, String name) {
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
        scoreboardTeam.setPlayers(Arrays.asList(name));
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
