package ms.zero.tfmk.tfmkmenu.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.UUID;

public class HideNSeekChecker {
    public static Boolean isPlayingHideNSeek(Player player) {
        Class tfmkHideNSeekPlugin = Bukkit.getPluginManager().getPlugin("Tfmkhidenseek").getClass();
        try {
            Method checkMethod = tfmkHideNSeekPlugin.getMethod("isPlayingHideNSeek", Player.class);
            return (Boolean) checkMethod.invoke(null, player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
