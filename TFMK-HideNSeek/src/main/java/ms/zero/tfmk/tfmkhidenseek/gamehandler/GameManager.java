package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import ms.zero.tfmk.tfmkhidenseek.objects.GlowManager;
import ms.zero.tfmk.tfmkhidenseek.objects.NPCManager;
import ms.zero.tfmk.tfmkhidenseek.objects.NameTagManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule.PlayerType;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class GameManager {
    private static HashMap<Player, GameRule.PlayerType> playerTypeByPlayer = new HashMap<>();
    private static ArrayList<Player> gamePlayers = new ArrayList<>();
    private static Boolean gameStarted = false;
    private static Integer startCountDownTaskID = -1;
    private static Integer startTaskID = -1;
    private static Location[] barrierLocation = new Location[30];
    private static Location baseLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -94.5, 180, 5);
    private static Location lobbyLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -104.5, 180, 5);

    private static Location resultLocation = new Location(Bukkit.getWorld("world"), 283.5, 84, -106.5);

    static {
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

    public static Boolean join(Player player) {
        if (!gameStarted && !alreadyJoined(player)) {
            playerTypeByPlayer.put(player, PlayerType.RUNNER);
            gamePlayers.add(player);
            player.teleport(lobbyLocation);
            broadcastToPlayers(
                    String.format(translate("&a[+] &f%s &7&o(현재 인원수: %d명)"), player.getName(), playerTypeByPlayer.size()));
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

    public static Boolean quit(Player player, GameRule.Reason reason) {
        if (!gameStarted && alreadyJoined(player)) {
            playerTypeByPlayer.remove(player);
            gamePlayers.remove(player);
            player.teleport(baseLocation);
            broadcastToPlayers(
                    String.format(translate("&c[-] &f%s &7&o(현재 인원수: %d명)"), player.getName(), playerTypeByPlayer.size()));
            player.sendMessage(translate("&c[-] &7게임에서 퇴장하셨습니다."));
            if (!canGameStart() && startCountDownTaskID != -1 && startTaskID != -1) {
                broadcastToPlayers(translate("&c[!] &7최소인원이 부족하여 게임이 &c취소&7됩니다."));
                Bukkit.getScheduler().cancelTask(startCountDownTaskID);
                Bukkit.getScheduler().cancelTask(startTaskID);
                startCountDownTaskID = -1;
                startTaskID = -1;
            }
            return true;
        } else {
            if (reason == GameRule.Reason.NPC) {
                return false;
            } else if (reason == GameRule.Reason.FORCE) {
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

    private static Boolean alreadyJoined(Player player) {
        return gamePlayers.contains(player);
    }

    private static Boolean canGameStart() {
        if (gamePlayers.size() >= GameRule.getMinPlayers()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isTagger(Player player) {
        return gamePlayers.contains(player) && playerTypeByPlayer.get(player) == PlayerType.TAGGER;
    }

    public static Boolean isRunner(Player player) {
        return gamePlayers.contains(player) && playerTypeByPlayer.get(player) == PlayerType.RUNNER;
    }

    public static Integer getTaggerCount() {
        return getTagger().size();
    }

    public static Integer getRunnerCount() {
        return getRunner().size();
    }

    public static List<Player> getTagger() {
        return playerTypeByPlayer.keySet().stream()
                .filter(player -> playerTypeByPlayer.get(player).equals(PlayerType.TAGGER))
                .collect(Collectors.toList());
    }

    public static List<Player> getRunner() {
        return playerTypeByPlayer.keySet().stream()
                .filter(player -> playerTypeByPlayer.get(player).equals(PlayerType.RUNNER))
                .collect(Collectors.toList());
    }

    public static String getRole(Player player) {
        return playerTypeByPlayer.get(player).equals(PlayerType.RUNNER) ? "도망자" : "술래";
    }

    private static void broadcastToPlayers(String message) {
        gamePlayers.forEach(player -> player.sendMessage(message));
    }

    private static void broadcastToPlayers(String title, String subTitle, int seconds) {
        gamePlayers.forEach(player -> player.sendTitle(title, subTitle, 0, 20 * seconds, 0));
    }

    private static void broadcastToTagger(String message) {
        getTagger().forEach(player -> player.sendMessage(message));
    }

    private static void broadcastToRunner(String message) {
        getRunner().forEach(player -> player.sendMessage(message));
    }

    private static void broadcastToRunner(String title, String subTitle, int seconds) {
        getRunner().forEach(player -> player.sendTitle(title, subTitle, 0, 20 * seconds, 0));
    }

    private static void initTagger() {
        Collections.shuffle(gamePlayers);
        for (int i = 0; i < GameRule.getLeastTaggers(); i++) {
            Integer r = getRandomIndex();
            Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] random_index: %d"), r));
            Player p = gamePlayers.get(r);
            playerTypeByPlayer.put(p, PlayerType.TAGGER);
            makeTagger(p);
        }
        GameScore.initPlayers(getTaggerCount(), getRunnerCount());
        broadcastToRunner(translate("&a휴, 살았다!"), translate("&f당신은 도망자입니다. 술래로부터 도망가세요!"), 7);
    }

    private static void makeTagger(Player player) {
        Util.removeItem(player, KEY_PIECE);

        player.getInventory().setHelmet(PUMPKIN_HELMET);
        player.getInventory().setItem(0, GOLDEN_HOE);
        player.getInventory().setHeldItemSlot(0);
        PotionEffect eInvisible = new PotionEffect(PotionEffectType.INVISIBILITY, 100000 * 20, 1, false, false);
        PotionEffect eSlow = new PotionEffect(PotionEffectType.SLOW, 10 * 20, 250, false, false);
        PotionEffect eJumpBoost = new PotionEffect(PotionEffectType.JUMP, 10 * 20, 250, false, false);
        player.addPotionEffects(new ArrayList<>(Arrays.asList(eSlow, eJumpBoost, eInvisible)));

        player.sendTitle(translate("&c이런!"), translate("&f당신은 술래입니다. 도망자를 잡으세요!"), 0, 20 * 7, 0);
        player.sendMessage(translate("&6[!] &7당신은 술래입니다!"));
    }

    private static Integer getRemainingRunner() {
        Integer count = 0;
        for (Player player : gamePlayers) {
            if (playerTypeByPlayer.get(player).equals(PlayerType.RUNNER)) {
                count++;
            }
        }

        return count;
    }

    public static void catchTheRunner(Player player) {
        if (isRunner(player)) {
            playerTypeByPlayer.put(player, PlayerType.TAGGER);
            makeTagger(player);

            GameScore.decreaseRunner();
            for (int i = 0; i < GameScore.getPickedUpKeyByPlayer(player); i++) {
                KeyDropper.spawnKey();
            }
            GameScore.runnerDroppedKey(player);

            if (isGameCanPlayable()) {
                broadcastToPlayers(String.format(translate("&c[!] &f%s&7님이 술래가 되었습니다."), player.getName()));
                broadcastToPlayers(String.format(translate("&c[!] &7도망자가 &e%d&7명 남았습니다."), getRemainingRunner()));
                broadcastToPlayers(translate("&c[!] &7도망자가 죽은 자리에 열쇠가 떨어집니다."));
            } else {
                endGame();
            }
        }
    }

    private static void initScore() {
        GameScore.initScores();
        for (Player player : gamePlayers) {
            GameScore.addGamePlayer(player, 0);
        }
    }

    private static void finalizeGame() {
        NameTagManager.showNameTag(gamePlayers);

        installBarrier();
        gamePlayers.forEach(player -> player.teleport(baseLocation));

        KeyDropper.reset();

        playerTypeByPlayer.clear();
        gamePlayers.clear();

        startCountDownTaskID = -1;
        startTaskID = -1;
    }

    private static void clearTagger() {
        getTagger().forEach(tagger -> {
            tagger.getInventory().setHelmet(new ItemStack(Material.AIR));
            Util.removeItem(tagger, GOLDEN_HOE);
            Util.removePotionEffects(tagger);
        });
    }

    private static void clearRunner() {
        getRunner().forEach(runner -> {
            Util.removeItem(runner, KEY_PIECE);
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
        GameScore.setDroppedKeyScore(GameScore.getDroppedKeyScore() + 1);
    }

    public static void makePlayerGlowing(Player player) {
        GlowManager.add(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                GlowManager.remove(player);
            }
        }, 20L * 10);
    }

    public static void pickUpKey(Player player) {
        GameScore.runnerPickedUpKey(player);
        makePlayerGlowing(player);
    }

    private static Integer getRandomIndex() {
        int randomValue = (int) (Math.random() * gamePlayers.size());
        if (playerTypeByPlayer.get(gamePlayers.get(randomValue)).equals(PlayerType.TAGGER)) {
            return getRandomIndex();
        } else {
            return randomValue;
        }
    }

    private static void startCountDown(int startCount, int endCount) {
        for (int i = startCount; i >= endCount; i--) {
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
            }, 20L * (startCount - i));
        }
    }

    private static void taggerCountDown(int startCount, int endCount) { // using 10 seconds
        for (int i = startCount; i >= endCount; i--) {
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
            }, 20L * (startCount - i));
        }
    }

    private static void showStatistics() {
        clearTagger();
        clearRunner();
        gamePlayers.forEach(player -> player.teleport(resultLocation));
        NPCManager.showNPC(gamePlayers);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                NPCManager.removeNPC();
            }
        }, 20L * 20);
        //hologram managers
    }

    private static void showRankingHologram() {

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
                showStatistics();
            }
        }, 20L * 5);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                finalizeGame();
            }
        }, 20L * 25);
    }

    public static void startGame() {
        gameStarted = true;

        NameTagManager.hideNameTag(gamePlayers);

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
