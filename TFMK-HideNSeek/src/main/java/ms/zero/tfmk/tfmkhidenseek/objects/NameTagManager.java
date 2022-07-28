package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NameTagManager {
    public static void hideNameTag(List<Player> playerList) {
        ArrayList<String> playersNameList = new ArrayList<>();
        playerList.forEach(player -> {
            playersNameList.add(player.getName());
        });
        for (Player p : playerList) {
            WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
            wrapper.setName("hidenseek-team");
            wrapper.setDisplayName(WrappedChatComponent.fromText(""));
            wrapper.setNameTagVisibility("never");
            wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            wrapper.setCollisionRule("always");
            wrapper.sendPacket(p);

            wrapper = new WrapperPlayServerScoreboardTeam();
            wrapper.setName("hidenseek-team");
            wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
            wrapper.setPlayers(playersNameList.stream().filter(s -> !p.getName().equals(s)).collect(Collectors.toList()));
            wrapper.sendPacket(p);
        }
    }

    public static void showNameTag(List<Player> playerList) {
        ArrayList<String> playersNameList = new ArrayList<>();
        playerList.forEach(player -> {
            playersNameList.add(player.getName());
        });
        for (Player p : playerList) {
            WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
            wrapper.setName("hidenseek-team");
            wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
            wrapper.setPlayers(playersNameList.stream().filter(s -> !p.getName().equals(s)).collect(Collectors.toList()));
            wrapper.sendPacket(p);
        }
    }

    public static void hideNameTag(Player targetPlayer, String name) {
        WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
        wrapper.setName("hidenseek-team");
        wrapper.setDisplayName(WrappedChatComponent.fromText(""));
        wrapper.setNameTagVisibility("never");
        wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        wrapper.setCollisionRule("always");
        wrapper.sendPacket(targetPlayer);

        wrapper = new WrapperPlayServerScoreboardTeam();
        wrapper.setName("hidenseek-team");
        wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        wrapper.setPlayers(Arrays.asList(name));
        wrapper.sendPacket(targetPlayer);

    }

    public static void showNameTag(Player targetPlayer, String name) {
        WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
        wrapper.setName("hidenseek-team");
        wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        wrapper.setPlayers(Arrays.asList(name));
        wrapper.sendPacket(targetPlayer);
    }
}
