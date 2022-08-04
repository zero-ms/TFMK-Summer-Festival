package ms.zero.tfmk.tfmkfishing.command;

import ms.zero.tfmk.tfmkfishing.fishing.ContestManager;
import ms.zero.tfmk.tfmkfishing.fishing.objects.FishType;
import ms.zero.tfmk.tfmkfishing.fishing.util.FishInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkfishing.global.Util.translate;

public class FishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player commandSender = (Player) sender;
        if (label.equalsIgnoreCase("fishing")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("start")) {
                    if (commandSender.isOp()) {
                        ContestManager.startContest();
                        commandSender.sendMessage(translate("&c[DEBUG] &7낚시대회를 실행했습니다."));
                        return true;
                    } else {
                        commandSender.sendMessage(translate("&c[ERROR] &7권한이 없습니다."));
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (commandSender.isOp()) {
                        commandSender.getInventory().addItem(FishInfo.getItemFromFishType(FishType.RED_SEABREAM),
                                FishInfo.getItemFromFishType(FishType.MANDARIN_FISH),
                                FishInfo.getItemFromFishType(FishType.SALMON),
                                FishInfo.getItemFromFishType(FishType.SEAWEED),
                                FishInfo.getItemFromFishType(FishType.BLOWFISH),
                                FishInfo.getItemFromFishType(FishType.SEASHELL));
                    } else {
                        commandSender.sendMessage(translate("&c[DEBUG] &7권한이 없습니다."));
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }
}
