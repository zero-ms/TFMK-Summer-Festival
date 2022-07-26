package ms.zero.tfmk.tfmkhidenseek.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            Player p = (Player) sender;
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("toggle", "stop", "status"), new ArrayList<>());
        } else {
            return Collections.emptyList();
        }
    }
}
