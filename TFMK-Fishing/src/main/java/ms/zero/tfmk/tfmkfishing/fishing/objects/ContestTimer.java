package ms.zero.tfmk.tfmkfishing.fishing.objects;

import ms.zero.tfmk.tfmkfishing.fishing.ContestManager;
import ms.zero.tfmk.tfmkfishing.fishing.util.FishInfo;
import ms.zero.tfmk.tfmkfishing.fishing.util.FishUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location location = player.getLocation();
                    if (!ContestManager.isContestStarted()) {
                        if (FishUtil.isInside(location)) {
                            if (alreadyQueued.containsKey(player)) {
                                if (!alreadyQueued.get(player)) {
                                    alreadyQueued.put(player, true);
                                    player.sendTitle(translate("&b[입장]"), translate("&b낚시대회 &7대기열에 입장하셨습니다."), 0, 20 * 3, 20);
                                }
                            } else {
                                alreadyQueued.put(player, true);
                                player.sendTitle(translate("&b[입장]"), translate("&b낚시대회 &7대기열에 입장하셨습니다."), 0, 20 * 3, 20);
                            }
                        } else {
                            if (alreadyQueued.containsKey(player)) {
                                if (alreadyQueued.get(player)) {
                                    alreadyQueued.put(player, false);
                                    player.sendTitle(translate("&c[퇴장]"), translate("&b낚시대회 &7대기열에서 퇴장하셨습니다."), 0, 20 * 3, 20);
                                }
                            } else {
                                alreadyQueued.put(player, false);
                            }
                        }
                    }
                }
            }
        }, 0, 20L);

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
