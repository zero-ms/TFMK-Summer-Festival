package ms.zero.tfmk.tfmkhidenseek.command;

import ms.zero.tfmk.tfmkhidenseek.game.GameManager;
import ms.zero.tfmk.tfmkhidenseek.game.objects.GameRule;
import ms.zero.tfmk.tfmkhidenseek.game.objects.GameScore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkhidenseek.global.Util.translate;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player commandSender = (Player) sender;
        if (label.equalsIgnoreCase("game")) {
            if (args.length > 0) {
                if (commandSender.isOp()) {
                    if (args[0].equalsIgnoreCase("stop")) {
                        GameManager.interruptGame();
                    } else if (args[0].equalsIgnoreCase("status")) {
                        commandSender.sendMessage(
                                String.format(translate("&c[DEBUG] &7drop_key: %d pick_key: %d tagger: %d runner: %d"),
                                        GameScore.getDroppedKeyScore(), GameScore.getPickedUpKeyScore(),
                                        GameScore.getTaggerCount(),
                                        GameScore.getRunnerCount()));
                        commandSender.sendMessage(
                                String.format(translate("&c[DEBUG] &7least_player: %d least_tagger: %d need_key: %d"),
                                        GameRule.getMinPlayers(), GameRule.getLeastTaggers(), GameRule.getNeedKey()));
                    }
                } else {
                    commandSender.sendMessage(translate("&c[ERROR] &7권한이 없습니다."));
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
