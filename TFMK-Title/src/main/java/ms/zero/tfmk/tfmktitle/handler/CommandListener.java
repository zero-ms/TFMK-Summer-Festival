package ms.zero.tfmk.tfmktitle.handler;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ms.zero.tfmk.tfmktitle.objects.TeamManager;
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
        if (label.equalsIgnoreCase("titleupdate")) {
            TeamManager.updatePlayersTeam();
            return true;
        } else {
            return false;
        }
    }
}
