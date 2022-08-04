package ms.zero.tfmk.tfmkhidenseek.game;

import ms.zero.tfmk.tfmkhidenseek.game.objects.*;
import ms.zero.tfmk.tfmkhidenseek.game.util.GameVariable;
import ms.zero.tfmk.tfmkhidenseek.hologram.ChangeableHologramSet;
import ms.zero.tfmk.tfmkhidenseek.hologram.HologramManager;
import ms.zero.tfmk.tfmkhidenseek.hologram.HologramRankingPreset;
import ms.zero.tfmk.tfmkhidenseek.global.Util;
import ms.zero.tfmk.tfmkhidenseek.npc.NPC;
import ms.zero.tfmk.tfmkhidenseek.npc.NPCManager;
import ms.zero.tfmk.tfmkhidenseek.nms.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import static ms.zero.tfmk.tfmkhidenseek.game.objects.GameRule.PlayerType;
import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.global.Util.translate;
import static ms.zero.tfmk.tfmkhidenseek.game.util.GameVariable.*;

public class GameManager {
    private static HashMap<Player, GamePlayer> gamePlayers = new HashMap<>();
    private static GameStatus gameStatus = GameStatus.NOT_PLAYING;
    private static Boolean assignedRole = false;
    private static Integer startCountDownTaskID = -1;
    private static Integer startTaskID = -1;

    private static Semaphore joinMutex = new Semaphore(1, true);

    public static Boolean join(Player player) {
        try {
            joinMutex.acquire();
            if ((gameStatus == GameStatus.NOT_PLAYING || gameStatus == GameStatus.WAITING) && !isPlaying(player)) {
                GamePlayer gamePlayer = new GamePlayer(player, PlayerType.RUNNER);
                gamePlayers.put(player, gamePlayer);
                player.teleport(GameVariable.lobbyLocation);
                broadcastToPlayers(
                        String.format(translate("&a[+] &f%s &7&o(현재 인원수: %d명)"), player.getName(), gamePlayers.size()));
                if (canGameStart() && startCountDownTaskID == -1 && startTaskID == -1) {
                    broadcastToPlayers(translate("&a[!] &730초 후 게임이 &a시작&7됩니다."));
                    startCountDownTaskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> startCountDown(10, 1), 20L * 20);
                    startTaskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        try {
                            startGame();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            interruptGame();
                        }
                    }, 20L * 30);
                }
                joinMutex.release();
                return true;
            } else {
                joinMutex.release();
                return false;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            joinMutex.release();
            return false;
        }
    }

    public static Boolean quit(Player player, GameRule.Reason reason) {
        try {
            joinMutex.acquire();
            if ((gameStatus == GameStatus.NOT_PLAYING || gameStatus == GameStatus.WAITING) && isPlaying(player)) {
                gamePlayers.remove(player);
                player.teleport(GameVariable.baseLocation);
                broadcastToPlayers(
                        String.format(translate("&c[-] &f%s &7&o(현재 인원수: %d명)"), player.getName(), gamePlayers.size()));
                player.sendMessage(translate("&c[-] &7게임에서 퇴장하셨습니다."));
                if (!canGameStart() && startCountDownTaskID != -1 && startTaskID != -1) {
                    broadcastToPlayers(translate("&c[!] &7최소인원이 부족하여 게임이 &c취소&7됩니다."));
                    Bukkit.getScheduler().cancelTask(startCountDownTaskID);
                    Bukkit.getScheduler().cancelTask(startTaskID);
                    startCountDownTaskID = -1;
                    startTaskID = -1;
                }
                joinMutex.release();
                return true;
            } else {
                if (reason == GameRule.Reason.FORCE) {
                    player.teleport(GameVariable.baseLocation);
                    try {
                        clearGameItems(player);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    gamePlayers.remove(player);
                    joinMutex.release();
                    if (gameStatus == GameStatus.PLAYING) {
                        if (!canPlayable()) {
                            interruptGame();
                        }
                    }
                } else {
                    joinMutex.release();
                }
                return false;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            joinMutex.release();
            return false;
        }
    }

    public static ArrayList<GamePlayer> getGamePlayerSet() {
        return new ArrayList<>(gamePlayers.values());
    }

    private static Boolean canPlayable() {
        if (gamePlayers.size() < GameRule.getMinPlayers()) {
            return false;
        }
        if (assignedRole) {
            return getTagger().size() != 0 && getRunner().size() != 0;
        }
        return true;
    }

    private static GameRule.EndReason isGameEnded() {
        if (GameScore.getPickedUpKeyScore() >= GameRule.getNeedKey()) {
            return GameRule.EndReason.KEY_SUFFICIENT;
        }
        if (getRunnerCount() == 0) {
            return GameRule.EndReason.TAGGER_WIN;
        }
        if ((GameScore.getEndTime() - System.currentTimeMillis()) <= 1000) {
            return GameRule.EndReason.TIMEOUT;
        }
        if (getTaggerCount() == 0) {
            return GameRule.EndReason.TAGGER_INSUFFICIENT;
        }

        return GameRule.EndReason.CAN_PLAY;
    }

    public static GameStatus getGameStatus() {
        return gameStatus;
    }

    public static Boolean isPlaying(Player player) {
        return gamePlayers.containsKey(player);
    }

    private static Boolean canGameStart() {
        return gamePlayers.size() >= GameRule.getMinPlayers();
    }

    public static Boolean isTagger(Player player) {
        return gamePlayers.containsKey(player) && gamePlayers.get(player).getFinalPlayerType() == PlayerType.TAGGER;
    }

    public static Boolean isRunner(Player player) {
        return gamePlayers.containsKey(player) && gamePlayers.get(player).getFinalPlayerType() == PlayerType.RUNNER;
    }

    public static Integer getTaggerCount() {
        return getTagger().size();
    }

    public static Integer getRunnerCount() {
        return getRunner().size();
    }

    public static List<Player> getTagger() {
        return gamePlayers.keySet().stream()
                .filter(player -> gamePlayers.get(player).getFinalPlayerType() == PlayerType.TAGGER)
                .collect(Collectors.toList());
    }

    public static List<Player> getRunner() {
        return gamePlayers.keySet().stream()
                .filter(player -> gamePlayers.get(player).getFinalPlayerType() == PlayerType.RUNNER)
                .collect(Collectors.toList());
    }

    public static String getRole(Player player) {
        return gamePlayers.get(player).getFinalPlayerType() == PlayerType.RUNNER ? "도망자" : "술래";
    }

    private static void broadcastToPlayers(String message) {
        gamePlayers.forEach((player, gamePlayer) -> player.sendMessage(message));
    }

    private static void broadcastToPlayers(String title, String subTitle, int seconds) {
        gamePlayers.forEach((player, gamePlayer) -> player.sendTitle(title, subTitle, 0, 20 * seconds, 0));
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

    private static void initTagger() throws Exception {
        ArrayList<Player> shuffledPlayers = new ArrayList<>(gamePlayers.keySet());
        Collections.shuffle(shuffledPlayers);
        for (int i = 0; i < GameRule.getLeastTaggers(); i++) {
            Integer randomIndex = getRandomIndex(shuffledPlayers);
            Player tagger = shuffledPlayers.get(randomIndex);
            gamePlayers.get(tagger).setInitialPlayerType(PlayerType.TAGGER);
            gamePlayers.get(tagger).setFinalPlayerType(PlayerType.TAGGER);
            makeTagger(tagger);
        }
        GameScore.initPlayers(getTaggerCount(), getRunnerCount());
        broadcastToRunner(translate("&a휴, 살았다!"), translate("&f당신은 도망자입니다. 술래로부터 도망가세요!"), 7);
        assignedRole = true;
    }

    private static void makeTagger(Player player) throws Exception {
        Util.removeItem(player, KEY_PIECE);

        ItemStack hatBackupItem = player.getInventory().getItem(EquipmentSlot.HEAD);
        ItemStack toolBackupItem = player.getInventory().getItem(0);

        gamePlayers.get(player).setBackupItem(hatBackupItem, toolBackupItem);

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
        for (Player player : gamePlayers.keySet()) {
            if (gamePlayers.get(player).getFinalPlayerType() == PlayerType.RUNNER) {
                count++;
            }
        }

        return count;
    }

    public static void catchTheRunner(Player attacker, Player victim) throws Exception {
        if (isRunner(victim)) {
            gamePlayers.get(victim).setFinalPlayerType(PlayerType.TAGGER);
            makeTagger(victim);

            GameScore.decreaseRunner();
            for (int i = 0; i < gamePlayers.get(victim).getPickedUpKeyScore(); i++) {
                KeyDropper.dropKeyToRandomLocation();
            }
            GameScore.runnerDroppedKey(gamePlayers.get(victim).getPickedUpKeyScore());

            gamePlayers.get(attacker).addKillScore();

            GameRule.EndReason endReason = isGameEnded();
            if (endReason == GameRule.EndReason.CAN_PLAY) {
                broadcastToPlayers(String.format(translate("&c[!] &f%s&7님이 술래가 되었습니다."), victim.getName()));
                broadcastToPlayers(String.format(translate("&c[!] &7도망자가 &e%d&7명 남았습니다."), getRemainingRunner()));
                broadcastToPlayers(translate("&c[!] &7도망자가 죽은 자리에 열쇠가 떨어집니다."));
            } else if (endReason == GameRule.EndReason.TAGGER_WIN) {
                endGame();
            }
        }
    }

    private static void finalizeGame() {
        try {
            clearGameItems();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        NameTagManager.showEachPlayerNameTag(new ArrayList<>(gamePlayers.keySet()));
        gamePlayers.forEach(
                (player, gamePlayer) -> player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
        gamePlayers.forEach((player, gamePlayer) -> GlowManager.removeFromGlowingPool(player));

        installBarrier();
        gamePlayers.forEach((player, gamePlayer) -> player.teleport(GameVariable.baseLocation));

        KeyDropper.resetAllKeys();

        gamePlayers.clear();
        GameScore.initScores();
        startCountDownTaskID = -1;
        startTaskID = -1;
        gameStatus = GameStatus.NOT_PLAYING;
        assignedRole = false;
    }

    private static void clearTagger() throws Exception {
        getTagger().forEach(tagger -> {
            ItemStack hat = gamePlayers.get(tagger).getHatBackupItem();
            ItemStack tool = gamePlayers.get(tagger).getToolBackupItem();
            if (hat != null) {
                tagger.getInventory().setHelmet(hat);
            } else {
                tagger.getInventory().setHelmet(new ItemStack(Material.AIR));
            }

            Util.removeItem(tagger, GOLDEN_HOE);

            if (tool != null) {
                tagger.getInventory().setItem(0, tool);
            }

            Util.removePotionEffects(tagger);
        });
    }

    private static void clearRunner() throws Exception {
        getRunner().forEach(runner -> Util.removeItem(runner, KEY_PIECE));
    }

    private static void clearTagger(Player tagger) throws Exception {
        ItemStack hat = gamePlayers.get(tagger).getHatBackupItem();
        ItemStack tool = gamePlayers.get(tagger).getToolBackupItem();
        if (hat != null) {
            tagger.getInventory().setHelmet(hat);
        } else {
            tagger.getInventory().setHelmet(new ItemStack(Material.AIR));
        }

        Util.removeItem(tagger, GOLDEN_HOE);

        if (tool != null) {
            tagger.getInventory().setItem(0, tool);
        }
        Util.removePotionEffects(tagger);
    }

    private static void clearRunner(Player runner) throws Exception {
        Util.removeItem(runner, KEY_PIECE);
    }

    private static void installBarrier() {
        for (int i = 0; i < 30; i++) {
            GameVariable.barrierLocation[i].getBlock().setType(Material.BARRIER);
        }
    }

    private static void removeBarrier() {
        for (int i = 0; i < 30; i++) {
            GameVariable.barrierLocation[i].getBlock().setType(Material.AIR);
        }
    }

    private static void dropKeyPiece() {
        KeyDropper.dropKeyToRandomLocation();
        GameScore.setDroppedKeyScore(GameScore.getDroppedKeyScore() + 1);
    }

    public static void makePlayerGlowing(Player player) {
        GlowManager.addToGlowingPool(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> GlowManager.removeFromGlowingPool(player), 20L * 4);
    }

    public static void pickUpKey(Player player) {
        GameScore.runnerPickedUpKey();
        gamePlayers.get(player).addPickedUpKeyScore();
        makePlayerGlowing(player);

        if (isGameEnded() == GameRule.EndReason.KEY_SUFFICIENT) {
            endGame();
        }
    }

    private static Integer getRandomIndex(List<Player> shuffledPlayers) {
        int randomValue = (int) (Math.random() * gamePlayers.size());
        if (gamePlayers.get(shuffledPlayers.get(randomValue)).getFinalPlayerType() == PlayerType.TAGGER) {
            return getRandomIndex(shuffledPlayers);
        } else {
            return randomValue;
        }
    }

    private static void startCountDown(int startCount, int endCount) {
        for (int i = startCount; i >= endCount; i--) {
            int count = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (count <= 3) {
                    broadcastToPlayers(translate("&a시작까지..."), String.format(translate("&c%d초"), count), 2);
                } else {
                    broadcastToPlayers(translate("&a시작까지..."), String.format(translate("&7%d초"), count), 2);
                }

            }, 20L * (startCount - i));
        }
    }

    private static void taggerCountDown(int startCount, int endCount) { // using 10 seconds
        for (int i = startCount; i >= endCount; i--) {
            int count = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (count <= 3) {
                    broadcastToPlayers(translate("&c술래 배정까지..."), String.format(translate("&c%d초"), count), 2);
                } else {
                    broadcastToPlayers(translate("&c술래 배정까지..."), String.format(translate("&7%d초"), count), 2);
                }

            }, 20L * (startCount - i));
        }
    }

    private static void clearGameItems() throws Exception {
        clearTagger();
        clearRunner();
    }

    private static void clearGameItems(Player player) throws Exception {
        clearTagger(player);
        clearRunner(player);
    }

    private static void showRankingPart() throws Exception {
        ChangeableHologramSet hologramSet = HologramManager.createHologramSet(HologramRankingPreset.getKillRanking());
        hologramSet.setTotalReserved1(2);
        gamePlayers.forEach(((player, gamePlayer) -> hologramSet.showHolograms(player)));
    }

    private static void showNPCPart() throws Exception {
        gamePlayers.forEach(((player, gamePlayer) -> {
            NPC npc = NPCManager.createNewNPCForPlayer(player);
            npc.setNPCName(NPCManager.getRandomNPCName()).setLocation(statisticsNPCLocation).spawnNPC(player);

            HologramManager.createHologram(String.format(translate("&f처음 역할: &a%s"), gamePlayers.get(player).getInitialPlayerType().name()), new Location(world, 282.5, 86, -101.5), false).showHologram(player);
            HologramManager.createHologram(String.format(translate("&f최종 역할: &c%s"), gamePlayers.get(player).getFinalPlayerType().name()), new Location(world, 282.5, 86.35, -101.5),
                    false).showHologram(player);
            HologramManager.createHologram(String.format(translate("&f획득한 키: &3%d&f개"), gamePlayers.get(player).getPickedUpKeyScore()), new Location(world, 282.5, 86.7, -101.5),
                    false).showHologram(player);
            HologramManager.createHologram(String.format(translate("&f죽인 사람: &4%d&f명"), gamePlayers.get(player).getKillScore()), new Location(world, 282.5, 87.05, -101.5),
                    false).showHologram(player);
        }));

    }

    private static void showStatistics() throws Exception {
        clearGameItems();

        gamePlayers.forEach((player, gamePlayers) -> {
            PotionEffect eInvisible = new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 1, false, false);
            player.addPotionEffect(eInvisible);
        });
        gamePlayers.forEach((player, gamePlayer) -> player.teleport(GameVariable.resultLocation));

        showNPCPart();
        showRankingPart();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> broadcastToPlayers(translate("&c[!] &710초 뒤, 멘션 입구로 이동합니다.")), 20L * 10);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            gamePlayers.forEach((player, gamePlayer) -> {
                NPCManager.removeNPCFromPlayer(player);
                HologramManager.clearAllHologramsFromPlayer(player);
            });
            HologramManager.clearAllHologramObjects();
            finalizeGame();
        }, 20L * 20);
    }

    private static void ending() throws Exception {
        gameStatus = GameStatus.ENDING;

        switch (isGameEnded()) {
            case KEY_SUFFICIENT:
            case TIMEOUT:
                broadcastToPlayers(translate("&e게임 종료!"), translate("&e도망자들이 저택을 성공적으로 탈출했습니다!"), 10);
                break;
            case TAGGER_WIN:
                broadcastToPlayers(translate("&e게임 종료!"), translate("&c술래가 도망자들을 전부 고기로 만들어버렸습니다..."), 10);
                break;
            default:
                broadcastToPlayers(translate("&e뭐죠?"), translate("&c승패 판별에 실패했어요. 개발자에게 문의하세요."), 10);
                break;
        }

        GameScoreboard.updateScoreBoard();
        gamePlayers.forEach((player, gamePlayer) -> GlowManager.removeFromGlowingPool(player));
        HologramRankingPreset.resetRanking();
        HologramRankingPreset.updateRanking();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            try {
                showStatistics();
            } catch (Exception ex) {
                ex.printStackTrace();
                interruptGame();
            }
        }, 20L * 5);
    }

    public static void startGame() throws InterruptedException {
        joinMutex.acquire();
        gameStatus = GameStatus.PLAYING;
        joinMutex.release();

        GameScore.setEndTime(System.currentTimeMillis() + 220 * 1000);

        GameScoreboard.initApplyList(new ArrayList<>(gamePlayers.values()));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> GameScoreboard.updateScoreBoard(), 0, 5L);

        NameTagManager.hideEachPlayersNameTag(new ArrayList<>(gamePlayers.keySet()));


        removeBarrier();
        broadcastToPlayers(translate("&a게임 시작!"), translate("&f움직이세요!"), 7);
        broadcastToPlayers(translate("&c[!] &730초 후 &c술래&7가 정해집니다."));
        broadcastToPlayers(translate("&c[!] &7건물 곳곳으로 빠르게 도망치세요!"));


        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> taggerCountDown(10, 1), 20L * 20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            try {
                initTagger();
            } catch (Exception ex) {
                ex.printStackTrace();
                interruptGame();
            }
        }, 20L * 30);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, GameManager::dropKeyPiece, 20L * 40, 20L * 10L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, GameManager::endGame, 20L * 220);

    }

    public static void endGame() {
        Bukkit.getScheduler().cancelTasks(plugin);
        try {
            ending();
        } catch (Exception ex) {
            ex.printStackTrace();
            interruptGame();
        }
    }

    public static void interruptGame() {
        Bukkit.getScheduler().cancelTasks(plugin);
        broadcastToPlayers(translate("&c[NOTICE] &7게임이 중단되었습니다."));

        finalizeGame();
    }
}
