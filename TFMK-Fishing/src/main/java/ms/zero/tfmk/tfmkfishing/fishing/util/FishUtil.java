package ms.zero.tfmk.tfmkfishing.fishing.util;

import org.bukkit.Location;

public class FishUtil {

    private static Location startLocation;
    public static Boolean isInside(Location location) {
        Integer x = location.getBlockX();
        Integer y = location.getBlockY();
        Integer z = location.getBlockZ();

        if (z >= 434 && z <= 438) {
            if (x >= 268 && x <= 272) {
                if (y == 58) {
                    return true;
                }
            }
        }

        return false;
    }


    public static Location getStartLocation() {
        return startLocation;
    }
}
