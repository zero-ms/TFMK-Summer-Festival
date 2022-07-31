package ms.zero.tfmk.tfmktitle.objects;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmktitle.miscellaneous.Util.translate;

public class TeamManager {
    public static void updatePlayersTeam() {
        WrapperPlayServerScoreboardTeam adminTeam = new WrapperPlayServerScoreboardTeam();
        adminTeam.setName("tfmk-admin");
        adminTeam.setDisplayName(WrappedChatComponent.fromText(""));
        adminTeam.setPrefix(WrappedChatComponent.fromText(translate("&c[ADMIN] &f")));
        adminTeam.setNameTagVisibility("always");
        adminTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        adminTeam.setCollisionRule("always");
        adminTeam.setColor(ChatColor.WHITE);
        adminTeam.broadcastPacket();

        adminTeam = new WrapperPlayServerScoreboardTeam();
        adminTeam.setName("tfmk-admin");
        adminTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        adminTeam.setPlayers(getOPPlayerNames());
        adminTeam.broadcastPacket();

        WrapperPlayServerScoreboardTeam userTeam = new WrapperPlayServerScoreboardTeam();
        userTeam.setName("tfmk-user");
        userTeam.setDisplayName(WrappedChatComponent.fromText(""));
        userTeam.setPrefix(WrappedChatComponent.fromText(translate("&7[USER] &f")));
        userTeam.setNameTagVisibility("always");
        userTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        userTeam.setCollisionRule("always");
        userTeam.setColor(ChatColor.WHITE);
        userTeam.broadcastPacket();

        userTeam = new WrapperPlayServerScoreboardTeam();
        userTeam.setName("tfmk-user");
        userTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        userTeam.setPlayers(getUserPlayerNames());
        userTeam.broadcastPacket();
    }

    private static List<String> getOPPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers().stream().filter(player -> player.isOp()).collect(Collectors.toList())) {
            playerNames.add(player.getName());
        }

        return playerNames;
    }

    private static List<String> getUserPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers().stream().filter(player -> !player.isOp()).collect(Collectors.toList())) {
            playerNames.add(player.getName());
        }

        return playerNames;
    }
}
