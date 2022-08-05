package ms.zero.tfmk.tfmkmenu.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class GuiCooltime {
    private static HashMap<Player, Long> cooltimeMap = new HashMap<>();

    public static void put(Player player, Long endTimeMillis) {
        cooltimeMap.put(player, endTimeMillis);
    }

    public static Boolean isCoolTime(Player player) {
        if (cooltimeMap.containsKey(player)) {
            return System.currentTimeMillis() <= cooltimeMap.get(player);
        } else {
            return false;
        }
    }

    public static String getRemainingCoolTime(Player player) {
        return String.format("%.1f", (cooltimeMap.get(player) - System.currentTimeMillis()) / 1000.0);
    }
}
