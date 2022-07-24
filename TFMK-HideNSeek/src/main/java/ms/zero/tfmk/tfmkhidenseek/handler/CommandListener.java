package ms.zero.tfmk.tfmkhidenseek.handler;

import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("game")) {
            Player p = (Player) sender;
            if (args.length > 0) {
                if (args[1].equalsIgnoreCase("start")) {

                } else if (args[1].equalsIgnoreCase("stop")) {

                } else if (args[1].equalsIgnoreCase("status")) {

                }
            }
        }
        return false;
    }
}
