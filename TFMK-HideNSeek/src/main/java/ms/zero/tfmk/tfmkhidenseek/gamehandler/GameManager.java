package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule.PlayerType;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class GameManager {
    private static HashMap<Player, GameRule.PlayerType> playersMap = new HashMap<>();
    private static ArrayList<Player> playersList = new ArrayList<>();
    private static Boolean gameStarted = false;
    private static Integer taskID = -1;
    private static ItemStack pumpkinHelmet;
    private static ItemStack goldHoe;
    private static Location[] barrierLocation = new Location[30];
    private static Location baseLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -94.5, 180, 5);
    private static Location startLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -104.5, 180, 5);

    static {
        pumpkinHelmet = new ItemStack(Material.CARVED_PUMPKIN);
        goldHoe = new ItemStack(Material.GOLDEN_HOE);

        ItemMeta goldHoeMeta = goldHoe.getItemMeta();
        goldHoeMeta.setDisplayName(translate("&6술래의 낫"));
        ArrayList<String> lores = new ArrayList<>();
        lores.add(translate("&8술래들이 사용하는 낫이다."));
        goldHoeMeta.setLore(lores);
        goldHoe.setItemMeta(goldHoeMeta);

        World world = Bukkit.getWorld("world");
        for (int i = 0; i < 5; i++) {
            barrierLocation[i] = new Location(world, 275, 78, -103 + (i * -1));
        }
        for (int i = 5; i < 10; i++) {
            barrierLocation[i] = new Location(world, 275, 77, -103 + ((i - 5) * -1));
        }
        for (int i = 10; i < 15; i++) {
            barrierLocation[i] = new Location(world, 291, 78, -103 + ((i - 10) * -1));
        }
        for (int i = 15; i < 20; i++) {
            barrierLocation[i] = new Location(world, 291, 77, -103 + ((i - 15) * -1));
        }
        for (int i = 20; i < 23; i++) {
            barrierLocation[i] = new Location(world, 282 + (i - 20), 78, -108);
        }
        for (int i = 23; i < 26; i++) {
            barrierLocation[i] = new Location(world, 282 + (i - 23), 77, -108);
        }
        barrierLocation[26] = new Location(world, 285, 79, -109);
        barrierLocation[27] = new Location(world, 285, 78, -108);
        barrierLocation[28] = new Location(world, 281, 79, -109);
        barrierLocation[29] = new Location(world, 281, 78, -108);
    }


    public static Boolean join(Player p) {
        if (!gameStarted && !alreadyJoined(p)) {
            playersMap.put(p, PlayerType.RUNNER);
            playersList.add(p);
            p.teleport(startLocation);
            broadcastToPlayers(String.format(translate("&a[+] &f%s &7&o(현재 인원수: %d명)"), p.getName(), playersMap.size()));
            if (canGameStart() && taskID == -1) {
                broadcastToPlayers(translate("&a[!] &730초 후 게임이 &a시작&7됩니다."));
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

    public static Boolean quit(Player p, GameRule.Reason r) {
        if (!gameStarted && alreadyJoined(p)) {
            playersMap.remove(p);
            playersList.remove(p);
            p.teleport(baseLocation);
            broadcastToPlayers(String.format(translate("&c[-] &f%s &7&o(현재 인원수: %d명)"), p.getName(), playersMap.size()));
            p.sendMessage(translate("&c[-] &7게임에서 퇴장하셨습니다."));
            if (!canGameStart() && taskID != -1) {
                broadcastToPlayers(translate("&c[!] &7최소인원이 부족하여 게임이 &c취소&7됩니다."));
                Bukkit.getScheduler().cancelTask(taskID);
                taskID = -1;
            }
            return true;
        } else {
            if (r == GameRule.Reason.NPC) {
                return false;
            } else if (r == GameRule.Reason.FORCE) {
                // Check game can still playable.
            }
            return false;
        }
    }

    private static Boolean isGameCanPlayable() {
        // Check tagger's number or etc...
        return false;
    }

    private static Boolean alreadyJoined(Player p) {
        return playersList.contains(p);
    }

    private static Boolean canGameStart() {
        if (playersList.size() >= GameRule.getMinPlayers()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isTagger(Player p) {
        return playersList.contains(p) && playersMap.get(p) == PlayerType.TAGGER;
    }

    private static void broadcastToPlayers(String msg) {
        for (Player p : playersList) {
            p.sendMessage(msg);
        }
    }

    private static void broadcastToPlayers(String title, String subTitle, int seconds) {
        for (Player p : playersList) {
            p.sendTitle(title, subTitle, 0, 20 * seconds, 0);
        }
    }

    private static void broadcastToTagger(String msg) {
        for (Player p : playersList) {
            if (playersMap.get(p).equals(PlayerType.TAGGER)) {
                p.sendMessage(msg);
            }
        }
    }

    private static void broadcastToRunner(String msg) {
        for (Player p : playersList) {
            if (playersMap.get(p).equals(PlayerType.RUNNER)) {
                p.sendMessage(msg);
            }
        }
    }

    private static void broadcastToRunner(String title, String subTitle, int seconds) {
        for (Player p : playersList) {
            if (playersMap.get(p).equals(PlayerType.RUNNER)) {
                p.sendTitle(title, subTitle, 0, 20 * seconds, 0);
            }
        }
    }

    private static void initTagger() {
        // Random assign tagger.
        for (int i = 0; i < GameRule.getLeastTaggers(); i++) {
            Integer r = getRandomIndex();
            playersMap.put(playersList.get(r), PlayerType.TAGGER);

            Player p = playersList.get(r);
            makeTagger(p);
        }

        broadcastToRunner("&a휴, 살았다!", "&f당신은 도망자입니다. 술래로부터 도망가세요!", 7);
    }

    private static void makeTagger(Player p) {
        p.getInventory().setHelmet(pumpkinHelmet);
        p.getInventory().setItem(0, goldHoe);
        p.getInventory().setHeldItemSlot(0);
        PotionEffect eInvisible = new PotionEffect(PotionEffectType.INVISIBILITY, 100000 * 20, 1, false, false);
        PotionEffect eSlow = new PotionEffect(PotionEffectType.SLOW, 10 * 20, 250, false, false);
        PotionEffect eJumpBoost = new PotionEffect(PotionEffectType.JUMP, 10 * 20, 250, false, false);
        p.addPotionEffects(new ArrayList<PotionEffect>(Arrays.asList(eSlow, eJumpBoost, eInvisible)));

        p.sendTitle(translate("&c이런!"), translate("&f당신은 술래입니다. 도망자를 잡으세요!"), 0, 20 * 7, 0);
        p.sendMessage(translate("&6[!] &7당신은 술래입니다!"));
    }

    private static void finalizeGame() {
        // Teleport to lobby.
        // Steal tagger's item and clear potion effect.
        // Clear dropped items.
        clearTagger();
        clearRunner();
        installBarrier();

        playersMap.clear();
        playersList.clear();
    }

    private static void clearTagger() {
        for (Player p : playersList) {
            if (playersMap.get(p) == PlayerType.TAGGER) {
                p.getInventory().setHelmet(new ItemStack(Material.AIR));
                p.getInventory().remove(goldHoe);

                Util.removePotionEffects(p);

                p.teleport(baseLocation);
            }
        }
    }

    private static void clearRunner() {
        for (Player p : playersList) {
            if (playersMap.get(p) == PlayerType.RUNNER) {
                p.teleport(baseLocation);
            }
        }
    }

    private static void installBarrier() {
        for (int i = 0; i < 30; i++) {
            barrierLocation[i].getBlock().setType(Material.BARRIER);
        }
    }

    private static void removeBarrier() {
        for (int i = 0; i < 30; i++) {
            barrierLocation[i].getBlock().setType(Material.AIR);
        }
    }

    private static Integer getRandomIndex() {
        int r = (int) (Math.random() * playersList.size());
        if (playersMap.get(playersList.get(r)).equals(PlayerType.TAGGER)) {
            return getRandomIndex();
        } else {
            return r;
        }
    }

    private static void startCountDown(int startNumber, int endNumber) {
        for (int i = startNumber; i >= endNumber; i--) {
            int count = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (gameStarted) {
                        if (count <= 3) {
                            broadcastToPlayers(translate("&a시작까지..."), String.format(translate("&c%d초"), count), 2);
                        } else {
                            broadcastToPlayers(translate("&a시작까지..."), String.format(translate("&7%d초"), count), 2);
                        }
                    }
                }
            }, 20L * (startNumber - i));
        }
    }

    private static void taggerCountDown(int startNumber, int endNumber) { // using 10 seconds
        for (int i = startNumber; i >= endNumber; i--) {
            int count = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (gameStarted) {
                        if (count <= 3) {
                            broadcastToPlayers(translate("&c술래 배정까지..."), String.format(translate("&c%d초"), count), 2);
                        } else {
                            broadcastToPlayers(translate("&c술래 배정까지..."), String.format(translate("&7%d초"), count), 2);
                        }
                    }
                }
            }, 20L * (startNumber - i));
        }
    }

    public static void startGame() {
        gameStarted = true;

        startCountDown(10, 1); // using 10 seconds
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    removeBarrier();
                    broadcastToPlayers(translate("&a게임 시작!"), translate("&f움직이세요!"), 7);
                    broadcastToPlayers(translate("&c[!] &730초 후 &c술래&7가 정해집니다."));
                    broadcastToPlayers(translate("&c[!] &7건물 곳곳으로 빠르게 도망치세요!"));
                }
            }
        }, 20L * 10);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    taggerCountDown(10, 1);
                }
            }
        }, 20L * 30);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    initTagger();
                }
            }
        }, 20L * 40);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    broadcastToPlayers("game end!");
                    finalizeGame();
                    gameStarted = false;
                }
            }
        }, 20L * 60);
    }

    public static void interruptGame() {
        gameStarted = false;
    }
}
