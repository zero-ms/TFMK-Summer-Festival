package ms.zero.tfmk.tfmkhidenseek.handler;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameScore;
import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import ms.zero.tfmk.tfmkhidenseek.hologramhandler.HologramManager;
import ms.zero.tfmk.tfmkhidenseek.npchandler.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.world;
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
                    commandSender.sendMessage(
                            String.format(translate("&c[DEBUG] &7least_player: %d least_tagger: %d need_key: %d"),
                                    GameRule.getMinPlayers(), GameRule.getLeastTaggers(), GameRule.getNeedKey()));
                } else if (args[0].equalsIgnoreCase("ar")) {
                    StringBuilder argsBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        argsBuilder.append(args[i]).append(" ");
                    }
                    argsBuilder.deleteCharAt(argsBuilder.length()-1);
                    Util.spawnArmorStand(translate(argsBuilder.toString()));
                } else if (args[0].equalsIgnoreCase("test")) {
                    HashMap<Integer, String> ef = new HashMap<>();
                    ef.put(0, "test1");
                    ef.put(2, "test3");
                    ef.put(1, "test2");
                    Object[] mapkey = ef.keySet().toArray();
                    Arrays.sort(mapkey);

                    for (Integer key : ef.keySet()) {
                        System.out.println(ef.get(key));
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
