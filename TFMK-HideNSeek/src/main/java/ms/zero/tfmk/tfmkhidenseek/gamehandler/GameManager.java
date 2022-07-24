package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

import static ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule.PlayerType;
import static ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule.getMinPlayers;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class GameManager {
    private static HashMap<Player, GameRule.PlayerType> playersMap = new HashMap<>();
    private static ArrayList<Player> playersList;
    private static Boolean gameStarted = false;
    private static Integer taskID = 0;

    public static Boolean join(Player p) {
        if (!gameStarted) {
            playersMap.put(p, PlayerType.RUNNER);
            broadcastToPlayers(String.format(translate("&a[+] &f%s &7&o(%d/∞)"), p.getName(), playersMap.size()));
            if (canGameStart()) {
                broadcastToPlayers(translate("&6[!] &c30&7초 후 게임이 &a시작됩니다."));
                taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        startGame();
                    }
                }, 20L * 30);

            }
            return true;
        } else {
            return false;
        }
    }

    public static Boolean quit(Player p) {
        if (!gameStarted) {
            playersMap.remove(p);
            broadcastToPlayers(String.format(translate("&c[-] &f%s &7&o(%d/∞)"), p.getName(), playersMap.size()));
            if (!canGameStart()) {
                broadcastToPlayers(translate("&6[!] &7최소인원이 &c부족&7하여 게임이 &c취소&7됩니다."));
                Bukkit.getScheduler().cancelTask(taskID);
                taskID = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public static Boolean canGameStart() {
        if (playersMap.size() >= GameRule.getMinPlayers()) {
            return true;
        } else {
            return false;
        }
    }

    public static void broadcastToPlayers(String msg) {
        for (Player p : playersMap.keySet()) {
            p.sendMessage(msg);
        }
    }

    public static void broadcastToTagger(String msg) {
        for (Player p : playersMap.keySet()) {
            if (playersMap.get(p).equals(PlayerType.TAGGER)) {
                p.sendMessage(msg);
            }
        }
    }

    public static void broadcastToRunner(String msg) {
        for (Player p : playersMap.keySet()) {
            if (playersMap.get(p).equals(PlayerType.RUNNER)) {
                p.sendMessage(msg);
            }
        }
    }

    public static void initTagger() {
        // Random assign tagger.
        playersList = new ArrayList<>(playersMap.keySet());
        for (int i = 0; i < getMinPlayers(); i++) {

        }
    }

    public static Integer getRandomTagger(int max) {
    }

    public static void startGame() {
        gameStarted = true;
        initTagger();
    }

    public static void interruptGame() {
        gameStarted = false;
    }
}
