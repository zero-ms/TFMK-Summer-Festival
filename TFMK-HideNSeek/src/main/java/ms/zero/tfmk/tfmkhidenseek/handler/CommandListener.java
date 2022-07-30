package ms.zero.tfmk.tfmkhidenseek.handler;

import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameScore;
import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import ms.zero.tfmk.tfmkhidenseek.objects.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player commandSender = (Player) sender;
        if (label.equalsIgnoreCase("game")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("toggle")) {

                } else if (args[0].equalsIgnoreCase("stop")) {
                    GameManager.interruptGame();
                } else if (args[0].equalsIgnoreCase("status")) {
                    commandSender.sendMessage(
                            String.format(translate("&c[DEBUG] &7drop_key: %d pick_key: %d tagger: %d runner: %d"),
                                    GameScore.getDroppedKeyScore(), GameScore.getPickedUpKeyScore(), GameScore.getTaggerCount(),
                                    GameScore.getRunnerCount()));
                } else if (args[0].equalsIgnoreCase("ar")) {
                    StringBuilder argsBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        argsBuilder.append(args[i]).append(" ");
                    }
                    argsBuilder.deleteCharAt(argsBuilder.length()-1);
                    Util.spawnArmorStand(translate(argsBuilder.toString()));
                } else if (args[0].equalsIgnoreCase("ho")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        HologramManager.createHologram(player, player.getLocation(), player.getName());
                    }
                }
            }
            return true;
        } else if (label.equalsIgnoreCase("이스터에그")) {
            commandSender.sendMessage(translate("&e[개발자의 한마디] &7플러그인, 기타 외주 문의 DataCat#0001"));
            return true;
        }
        return false;
    }
}
