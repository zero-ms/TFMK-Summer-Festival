package ms.zero.tfmk.tfmkmenu.command;

import ms.zero.tfmk.tfmkmenu.gui.GuiHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("warp") || label.equalsIgnoreCase("menu") || label.equalsIgnoreCase("워프") || label.equalsIgnoreCase("메뉴")) {
            GuiHandler.openWarpGui((Player) sender);
            return true;
        }
        return false;
    }
}
