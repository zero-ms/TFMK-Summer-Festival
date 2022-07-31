package ms.zero.tfmk.tfmktitle.handler;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmktitle.miscellaneous.Util.translate;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player commandSender = (Player) sender;
        commandSender.sendMessage("YO");
        WrapperPlayServerScoreboardTeam adminTeam = new WrapperPlayServerScoreboardTeam();
        adminTeam.setName("tfmk-admin");
        adminTeam.setDisplayName(WrappedChatComponent.fromText(""));
        adminTeam.setPrefix(WrappedChatComponent.fromText(translate("&c[DEV] &f")));
        adminTeam.setNameTagVisibility("always");
        adminTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        adminTeam.setCollisionRule("always");
        adminTeam.setColor(ChatColor.WHITE);
        adminTeam.sendPacket(commandSender);

        adminTeam = new WrapperPlayServerScoreboardTeam();
        adminTeam.setName("tfmk-admin");
        adminTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        adminTeam.setPlayers(Arrays.asList(commandSender.getName()));
        adminTeam.sendPacket(commandSender);
        return false;
    }
}
