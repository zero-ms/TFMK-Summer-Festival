package ms.zero.tfmk.tfmkhotel.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignChecker {
    private Location baseLocation;
    private Location signLocation;

    public SignChecker(Location l) {
        this.baseLocation = l;
    }

    private Boolean findHotelSign() {
        for (int x = baseLocation.getBlockX() - 1; x <= baseLocation.getBlockX() + 1; x++) {
            for (int y = baseLocation.getBlockY() - 1; y <= baseLocation.getBlockY() + 1; y++) {
                for (int z = baseLocation.getBlockZ() - 1; z <= baseLocation.getBlockZ() + 1; z++) {
                    Location l = new Location(baseLocation.getWorld(), x, y, z);
                    Block b = l.getBlock();
                    if (b.getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                        signLocation = new Location(baseLocation.getWorld(), x, y, z);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Integer getRoomNumberFromSign() {
        if (findHotelSign()) {
            Block b = signLocation.getBlock();
            Sign sign = (Sign) b.getState();
            Integer roonNumber = Integer.parseInt(sign.getLine(1).replaceAll("\\D", ""));
            return roonNumber;
        } else {
            return -1;
        }
    }
}
