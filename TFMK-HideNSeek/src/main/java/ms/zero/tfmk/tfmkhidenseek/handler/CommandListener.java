package ms.zero.tfmk.tfmkhidenseek.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameScore;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.KeyDropper;
import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import ms.zero.tfmk.tfmkhidenseek.objects.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.pm;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("game")) {
            Player p = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("toggle")) {

                } else if (args[0].equalsIgnoreCase("stop")) {
                    GameManager.interruptGame();
                } else if (args[0].equalsIgnoreCase("status")) {

                } else if (args[0].equalsIgnoreCase("test")) {
                    p.sendMessage(
                            String.format(translate("&c[DEBUG] &7drop_key: %d pick_key: %d tagger: %d runner: %d"),
                                    GameScore.getDroppedKey(), GameScore.getPickedUpKey(), GameScore.getTagger(),
                                    GameScore.getRunner()));
                } else if (args[0].equalsIgnoreCase("ar")) {
                    StringBuilder b = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        b.append(args[i]).append(" ");
                    }
                    b.deleteCharAt(b.length()-1);
                    Util.spawnArmorStand(translate(b.toString()));
                } else if (args[0].equalsIgnoreCase("npc")) {
                    Util.spawnNPC();
                }
            }
            return true;
        } else if (label.equalsIgnoreCase("이스터에그")) {
            Player p = (Player) sender;
            p.sendMessage(translate("&e[개발자의 한마디] &7플러그인, 기타 외주 문의 DataCat#0001"));
            return true;
        }
        return false;
    }
}
