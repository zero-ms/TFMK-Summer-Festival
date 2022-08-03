package ms.zero.tfmk.tfmkhidenseek.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.UUID;

public class HotelChecker {
    public static Boolean isPlayerAlreadyAssigned(Player player) {
        Class tfmkHotelPlugin = Bukkit.getPluginManager().getPlugin("Tfmkhotel").getClass();
        try {
            Method checkMethod = tfmkHotelPlugin.getMethod("isPlayerAssignedRoom", UUID.class);
            return (Boolean) checkMethod.invoke(null, player.getUniqueId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
