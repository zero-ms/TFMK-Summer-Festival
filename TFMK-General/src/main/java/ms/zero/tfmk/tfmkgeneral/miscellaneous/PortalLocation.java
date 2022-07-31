package ms.zero.tfmk.tfmkgeneral.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class PortalLocation {
    private static ArrayList<Location> portalBaseBlockLocation = new ArrayList<>();

    static {
        portalBaseBlockLocation.add(new Location(Bukkit.getWorld("world"), -744, 91, 762));
        portalBaseBlockLocation.add(new Location(Bukkit.getWorld("world"), -745, 91, 762));
        portalBaseBlockLocation.add(new Location(Bukkit.getWorld("world"), -746, 91, 762));
    }

    public static Boolean isInside(Location location) {
        for (Location registerdLocation : portalBaseBlockLocation) {
            if (registerdLocation.getBlockX() == location.getBlockX() && registerdLocation.getBlockY() == location.getBlockY() &&registerdLocation.getBlockZ() == location.getBlockZ()) {
                return true;
            }
        }
        return false;
    }
}
