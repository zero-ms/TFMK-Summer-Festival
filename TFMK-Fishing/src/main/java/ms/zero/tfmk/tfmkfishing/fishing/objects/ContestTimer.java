package ms.zero.tfmk.tfmkfishing.fishing.objects;

import ms.zero.tfmk.tfmkfishing.fishing.ContestManager;
import ms.zero.tfmk.tfmkfishing.fishing.util.FishUtil;
import ms.zero.tfmk.tfmkfishing.reflection.HotelChecker;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkfishing.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkfishing.global.Util.translate;

import java.time.LocalTime;
import java.util.HashMap;

public class ContestTimer {

    private static HashMap<Player, Boolean> alreadyQueued = new HashMap<>();

    public static void clearQueue() {
        alreadyQueued.clear();
    }

    public static void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (!isHourEven()) {
                    switch (LocalTime.now().getMinute()) {
                        case 0:
                            Bukkit.broadcastMessage(translate("&b[!] &7낚시 대회 시작까지 1시간 남았습니다."));
                            break;
                        case 30:
                            Bukkit.broadcastMessage(translate("&b[!] &7낚시 대회 시작까지 30분 남았습니다."));
                            break;
                        case 50:
                            Bukkit.broadcastMessage(translate("&b[!] &7낚시 대회 시작까지 10분 남았습니다."));
                            break;
                        case 57:
                            Bukkit.broadcastMessage(translate("&b[!] &7낚시 대회 시작까지 3분 남았습니다."));
                            break;
                        case 59:
                            Bukkit.broadcastMessage(translate("&b[!] &7낚시 대회 시작까지 1분 남았습니다."));
                            break;

                    }
                }
                if (isHourEven()) {
                    if (LocalTime.now().getMinute() == 0) {
                        if (!ContestManager.isContestStarted()) {
                            ContestManager.startContest();
                        }
                    }
                }
            }
        }, 0, 20L * 30);
    }

    public static void locationClipper() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Chunk contestStartChunkPoint = new Location(world, 270, 58, 436).getChunk();
                for (Entity entity : contestStartChunkPoint.getEntities()) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        Location location = player.getLocation();
                        if (!ContestManager.isContestStarted()) {
                            if (FishUtil.isInside(location)) {
                                if (alreadyQueued.containsKey(player)) {
                                    if (!alreadyQueued.get(player)) {
                                        alreadyQueued.put(player, true);
                                        if (HotelChecker.isPlayerAlreadyAssigned(player)) {
                                            player.sendTitle(translate("&b&l입장"), translate("&b낚시대회 &7대기열에 입장하셨습니다."), 0, 20 * 3, 20);
                                        } else {
                                            player.sendMessage(translate("&6[TFMK] &7호텔 체크인을 먼저 해주세요."));
                                        }
                                    }
                                } else {
                                    alreadyQueued.put(player, true);
                                    if (HotelChecker.isPlayerAlreadyAssigned(player)) {
                                        player.sendTitle(translate("&b&l입장"), translate("&b낚시대회 &7대기열에 입장하셨습니다."), 0, 20 * 3, 20);
                                    } else {
                                        player.sendMessage(translate("&6[TFMK] &7호텔 체크인을 먼저 해주세요."));
                                    }
                                }
                            } else {
                                if (alreadyQueued.containsKey(player)) {
                                    if (alreadyQueued.get(player)) {
                                        alreadyQueued.put(player, false);
                                        if (HotelChecker.isPlayerAlreadyAssigned(player)) {
                                            player.sendTitle(translate("&c&l퇴장"), translate("&b낚시대회 &7대기열에서 퇴장하셨습니다."), 0, 20 * 3, 20);
                                        } else {
                                            player.sendMessage(translate("&6[TFMK] &7호텔 체크인을 먼저 해주세요."));
                                        }
                                    }
                                } else {
                                    alreadyQueued.put(player, false);
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 5L);

    }

    public static Boolean isHourEven() {
        LocalTime time = LocalTime.now();
        int hours = time.getHour();
        if (hours % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
