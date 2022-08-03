package ms.zero.tfmk.tfmkfishing.fishing;

import ms.zero.tfmk.tfmkfishing.fishing.objects.ContestPlayer;
import ms.zero.tfmk.tfmkfishing.fishing.objects.ContestTimer;
import ms.zero.tfmk.tfmkfishing.fishing.util.FishInfo;
import ms.zero.tfmk.tfmkfishing.fishing.objects.FishType;
import ms.zero.tfmk.tfmkfishing.fishing.util.FishUtil;
import ms.zero.tfmk.tfmkfishing.fishing.util.RandomSelector;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static ms.zero.tfmk.tfmkfishing.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkfishing.global.Util.*;

public class ContestManager {
    private static HashMap<Player, ContestPlayer> playerToContestPlayer = new HashMap<>();
    private static LinkedHashMap<Player, Integer> scoreRanking = new LinkedHashMap<>();
    private static Long endTimeMillis;

    private static Boolean contestStarted = false;
    private static Integer actionBarTaskID = -1;

    public static Boolean isContestStarted() {
        return contestStarted;
    }

    public static void quit(Player player) {
        playerToContestPlayer.remove(player);
        takeFishingRod(player);
    }

    public static Boolean joinAllQueuedPlayers() {
        Integer count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (FishUtil.isInside(player.getLocation())) {
                ContestPlayer contestPlayer = new ContestPlayer(player, player.getInventory().getItem(0));
                playerToContestPlayer.put(player, contestPlayer);
                count += 1;
            }
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void giveFishingRod(Player player) {
        player.getInventory().setItem(0, FishInfo.getFishingRod());
    }

    public static void takeFishingRod(Player player) {
        ItemStack backedUpItem = playerToContestPlayer.get(player).getBackedUpItem();
        if (backedUpItem != null) {
            player.getInventory().setItem(0, backedUpItem);
        } else {
            player.getInventory().setItem(0, new ItemStack(Material.AIR));
        }
    }

    public static void startContest() {
        if (joinAllQueuedPlayers()) {
            Bukkit.broadcastMessage(translate("&b[!] &7낚시대회가 시작됩니다!"));


            contestStarted = true;
            endTimeMillis = System.currentTimeMillis() + (240 * 1000);

            playerToContestPlayer.keySet().forEach(ContestManager::giveFishingRod);

            actionBarTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    sendActionTimerBar();
                }
            }, 0, 20L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    broadCastToPlayers(translate("&b[!] &7낚시대회가 끝나기까지 1분 남았습니다."));
                }
            }, 20L * 180);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    broadCastToPlayers(translate("&b[!] &7낚시대회가 끝나기까지 30초 남았습니다."));
                }
            }, 20L * 210);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    broadCastToPlayers(translate("&b[!] &7낚시대회가 끝나기까지 10초 남았습니다."));
                }
            }, 20L * 230);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    endContest();
                }
            }, 20L * 240);
        } else {
            Bukkit.broadcastMessage(translate("&b[!] &7인원이 부족하여, 낚시대회가 취소되었습니다."));
        }
    }

    public static void endContest() {
        broadCastToPlayers(translate("&b[!] &7낚시대회가 종료되었습니다!"));
        //showRankingToChat();
        playerToContestPlayer.keySet().forEach(ContestManager::showItself);
        playerToContestPlayer.keySet().forEach(ContestManager::takeFishingRod);
        Bukkit.getScheduler().cancelTask(actionBarTaskID);
        ContestTimer.clearQueue();
        actionBarTaskID = -1;
        playerToContestPlayer.clear();
        contestStarted = false;
    }

    public static void broadCastToPlayers(String message) {
        playerToContestPlayer.forEach(((player, contestPlayer) -> player.sendMessage(message)));
    }

    public static void sendActionTimerBar() {
        for (Player player : playerToContestPlayer.keySet()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(String.format(translate("&f남은시간: %s"), getRemainingTime())));
        }
    }

    public static void catchTheFish(Player player) {
        ContestPlayer contestPlayer = playerToContestPlayer.get(player);
        FishType fishType = RandomSelector.getRandomFish();
        if (fishType == FishType.RED_SEABREAM) {
            broadCastToPlayers(String.format(translate("%s&7님이 &c참돔&7을 낚으셨습니다!"), player.getName()));
        }
        contestPlayer.caughtFish(fishType);
        contestPlayer.getPlayer().getInventory().addItem(FishInfo.getItemFromFishType(fishType));
    }

    private static String getRemainingTime() {
        int diff = (int) (endTimeMillis - System.currentTimeMillis());
        diff /= 1000;

        int minutes = diff / 60;
        int seconds = diff - (minutes * 60);

        if (minutes > 0) {
            return String.format(translate("&6%d&f분 &6%d&f초"), minutes, seconds);
        } else {
            return String.format(translate("&6%d&f초"), seconds);
        }
    }

    private static void showRankingToChat() {
        broadCastToPlayers(translate("&7===== (&b낚시대회 결과&7) ====="));
    }

    private static void showItself(Player player) {
        player.sendMessage(String.format(translate("&b%s&7님의 점수: &e%d&7점"), player.getName(), playerToContestPlayer.get(player).getScore()));
    }

    public static Boolean isContestPlayer(Player player) {
        return playerToContestPlayer.containsKey(player);
    }
}
