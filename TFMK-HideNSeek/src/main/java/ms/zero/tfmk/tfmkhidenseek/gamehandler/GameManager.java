package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import ms.zero.tfmk.tfmkhidenseek.objects.GlowManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule.PlayerType;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class GameManager {
    private static HashMap<Player, GameRule.PlayerType> playersMap = new HashMap<>();
    private static ArrayList<Player> playersList = new ArrayList<>();
    private static Boolean gameStarted = false;
    private static Integer startCountDownTaskID = -1;
    private static Integer startTaskID = -1;
    private static Location[] barrierLocation = new Location[30];
    private static Location baseLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -94.5, 180, 5);
    private static Location startLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -104.5, 180, 5);

    static {
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
            broadcastToPlayers(
                    String.format(translate("&a[+] &f%s &7&o(현재 인원수: %d명)"), p.getName(), playersMap.size()));
            if (canGameStart() && startCountDownTaskID == -1 && startTaskID == -1) {
                broadcastToPlayers(translate("&a[!] &730초 후 게임이 &a시작&7됩니다."));
                startCountDownTaskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        startCountDown(10, 1);
                    }
                }, 20L * 20);
                startTaskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
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
            broadcastToPlayers(
                    String.format(translate("&c[-] &f%s &7&o(현재 인원수: %d명)"), p.getName(), playersMap.size()));
            p.sendMessage(translate("&c[-] &7게임에서 퇴장하셨습니다."));
            if (!canGameStart() && startCountDownTaskID != -1 && startTaskID != -1) {
                broadcastToPlayers(translate("&c[!] &7최소인원이 부족하여 게임이 &c취소&7됩니다."));
                Bukkit.getScheduler().cancelTask(startCountDownTaskID);
                Bukkit.getScheduler().cancelTask(startTaskID);
                startCountDownTaskID = -1;
                startTaskID = -1;
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
        if (getRunnerCount() == 0 || getTaggerCount() == 0) {
            return false;
        }
        return true;
    }

    public static Boolean isGameStarted() {
        return gameStarted;
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

    public static Boolean isRunner(Player p) {
        return playersList.contains(p) && playersMap.get(p) == PlayerType.RUNNER;
    }

    public static Integer getTaggerCount() {
        return getTagger().size();
    }

    public static Integer getRunnerCount() {
        return getRunner().size();
    }

    public static List<Player> getTagger() {
        return playersMap.keySet().stream()
                .filter(player -> playersMap.get(player).equals(PlayerType.TAGGER))
                .collect(Collectors.toList());
    }

    public static List<Player> getRunner() {
        return playersMap.keySet().stream()
                .filter(player -> playersMap.get(player).equals(PlayerType.RUNNER))
                .collect(Collectors.toList());
    }

    public static String getRole(Player p) {
        return playersMap.get(p).equals(PlayerType.RUNNER) ? "도망자" : "술래";
    }

    private static void broadcastToPlayers(String msg) {
        playersList.forEach(player -> player.sendMessage(msg));
    }

    private static void broadcastToPlayers(String title, String subTitle, int seconds) {
        playersList.forEach(player -> player.sendTitle(title, subTitle, 0, 20 * seconds, 0));
    }

    private static void broadcastToTagger(String msg) {
        getTagger().forEach(player -> player.sendMessage(msg));
    }

    private static void broadcastToRunner(String msg) {
        getRunner().forEach(player -> player.sendMessage(msg));
    }

    private static void broadcastToRunner(String title, String subTitle, int seconds) {
        getRunner().forEach(player -> player.sendTitle(title, subTitle, 0, 20 * seconds, 0));
    }

    private static void initTagger() {
        // Random assign tagger.
        Collections.shuffle(playersList);
        for (int i = 0; i < GameRule.getLeastTaggers(); i++) {
            Integer r = getRandomIndex();
            Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] random_index: %d"), r));
            Player p = playersList.get(r);
            playersMap.put(p, PlayerType.TAGGER);
            makeTagger(p);
        }
        GameScore.initPlayers(getTaggerCount(), getRunnerCount());
        broadcastToRunner(translate("&a휴, 살았다!"), translate("&f당신은 도망자입니다. 술래로부터 도망가세요!"), 7);
    }

    private static void makeTagger(Player p) {
        Util.removeItem(p, KEY_PIECE);

        p.getInventory().setHelmet(PUMPKIN_HELMET);
        p.getInventory().setItem(0, GOLDEN_HOE);
        p.getInventory().setHeldItemSlot(0);
        PotionEffect eInvisible = new PotionEffect(PotionEffectType.INVISIBILITY, 100000 * 20, 1, false, false);
        PotionEffect eSlow = new PotionEffect(PotionEffectType.SLOW, 10 * 20, 250, false, false);
        PotionEffect eJumpBoost = new PotionEffect(PotionEffectType.JUMP, 10 * 20, 250, false, false);
        p.addPotionEffects(new ArrayList<>(Arrays.asList(eSlow, eJumpBoost, eInvisible)));

        p.sendTitle(translate("&c이런!"), translate("&f당신은 술래입니다. 도망자를 잡으세요!"), 0, 20 * 7, 0);
        p.sendMessage(translate("&6[!] &7당신은 술래입니다!"));
    }

    private static Integer getRemainingRunner() {
        Integer count = 0;
        for (Player p : playersList) {
            if (playersMap.get(p).equals(PlayerType.RUNNER)) {
                count++;
            }
        }

        return count;
    }

    public static void catchTheRunner(Player p) {
        if (isRunner(p)) {
            playersMap.put(p, PlayerType.TAGGER);
            makeTagger(p);

            GameScore.decreaseRunner();
            for (int i = 0; i < GameScore.getPlayerPickedUpKey(p); i++) {
                KeyDropper.spawnKey();
            }
            GameScore.dropKey(p);

            if (isGameCanPlayable()) {
                broadcastToPlayers(String.format(translate("&c[!] &f%s&7님이 술래가 되었습니다."), p.getName()));
                broadcastToPlayers(String.format(translate("&c[!] &7도망자가 &e%d&7명 남았습니다."), getRemainingRunner()));
                broadcastToPlayers(translate("&c[!] &7도망자가 죽은 자리에 열쇠가 떨어집니다."));
            } else {
                endGame();
            }
        }
    }

    private static void initScore() {
        GameScore.initScores();
        for (Player p : playersList) {
            GameScore.addPlayer(p, 0);
        }
    }

    private static void finalizeGame() {
        // Teleport to lobby.
        // Steal tagger's item and clear potion effect.
        // Clear dropped items.

        clearTagger();
        clearRunner();
        installBarrier();

        KeyDropper.reset();

        playersMap.clear();
        playersList.clear();

        startCountDownTaskID = -1;
        startTaskID = -1;
    }

    private static void clearTagger() {
        getTagger().forEach(player -> {
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            Util.removeItem(player, GOLDEN_HOE);
            Util.removePotionEffects(player);
            player.teleport(baseLocation);
        });
    }

    private static void clearRunner() {
        getRunner().forEach(player -> {
            Util.removeItem(player, KEY_PIECE);
            player.teleport(baseLocation);
        });
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

    private static void dropKeyPiece() {
        KeyDropper.spawnKey();
        GameScore.setDroppedKey(GameScore.getDroppedKey() + 1);
    }

    public static void makePlayerGlowing(Player p) {
        GlowManager.add(p);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                GlowManager.remove(p);
            }
        }, 20L * 10);
    }

    public static void pickUpKey(Player p) {
        GameScore.pickUpKey(p);
        makePlayerGlowing(p);
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
                    if (count <= 3) {
                        broadcastToPlayers(translate("&a시작까지..."), String.format(translate("&c%d초"), count), 2);
                    } else {
                        broadcastToPlayers(translate("&a시작까지..."), String.format(translate("&7%d초"), count), 2);
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

    private static void ending() {
        if (isGameCanPlayable()) {
            // Runner WIN
            broadcastToPlayers(translate("&e게임 종료!"), translate("&e도망자들이 저택을 성공적으로 탈출했습니다!"), 10);
        } else {
            // Tagger WIN
            broadcastToPlayers(translate("&e게임 종료!"), translate("&c술래가 도망자들을 전부 고기로 만들어버렸습니다..."), 10);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                finalizeGame();
            }
        }, 20L * 10);
    }

    public static void startGame() {
        gameStarted = true;

        if (gameStarted) {
            initScore();
            removeBarrier();
            broadcastToPlayers(translate("&a게임 시작!"), translate("&f움직이세요!"), 7);
            broadcastToPlayers(translate("&c[!] &730초 후 &c술래&7가 정해집니다."));
            broadcastToPlayers(translate("&c[!] &7건물 곳곳으로 빠르게 도망치세요!"));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    taggerCountDown(10, 1);
                }
            }
        }, 20L * 20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    initTagger();
                }
            }
        }, 20L * 30);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (gameStarted) {
                    dropKeyPiece();
                }
            }
        }, 20L * 40, 20L * 10L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                endGame();
            }
        }, 20L * 220);
    }

    public static void endGame() {
        if (gameStarted) {
            gameStarted = false;
            Bukkit.getScheduler().cancelTasks(plugin);
            ending();
        }
    }

    public static void interruptGame() {
        if (gameStarted) {
            gameStarted = false;
            Bukkit.getScheduler().cancelTasks(plugin);
            broadcastToPlayers(translate("&c[NOTICE] &7관리자가 게임을 중단시켰습니다."));
            broadcastToPlayers(translate("&c[NOTICE] &7자세한 것은 관리자에게 문의하십시오."));
            finalizeGame();
        }
    }
}
