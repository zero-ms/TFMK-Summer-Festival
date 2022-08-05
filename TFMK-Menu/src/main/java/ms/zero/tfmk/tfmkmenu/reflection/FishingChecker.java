package ms.zero.tfmk.tfmkmenu.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class FishingChecker {
    public static Boolean isPlayingFishingContest(Player player) {
        Class tfmkFishingPlugin = Bukkit.getPluginManager().getPlugin("Tfmkfishing").getClass();
        try {
            Method checkMethod = tfmkFishingPlugin.getMethod("isJoinedFishingContest", Player.class);
            return (Boolean) checkMethod.invoke(null, player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
