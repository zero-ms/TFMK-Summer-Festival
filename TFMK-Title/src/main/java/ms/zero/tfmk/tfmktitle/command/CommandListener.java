package ms.zero.tfmk.tfmktitle.command;

import ms.zero.tfmk.tfmktitle.objects.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
