package ms.zero.tfmk.tfmkhotel.event;

import ms.zero.tfmk.tfmkhotel.hotel.HotelManger;
import ms.zero.tfmk.tfmkhotel.hotel.SignChecker;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkhotel.global.GlobalVariable.eventLockList;
import static ms.zero.tfmk.tfmkhotel.global.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkhotel.global.Util.translate;

public class LocationChecker {
    public static void startLocationChecker() {
        World world = Bukkit.getWorld("world");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        Location location = player.getLocation();
                        Block block = location.getBlock();

                        if (block.getType().equals(Material.DARK_OAK_DOOR)) {
                            SignChecker checker = new SignChecker(location);
                            Integer roomNumber = checker.getRoomNumberFromSign();
                            if (roomNumber > 1000) {
                                if (!HotelManger.isOwner(player.getUniqueId(), roomNumber)) {
                                    Directional d = (Directional) block.getBlockData();
                                    player.setVelocity(d.getFacing().getOppositeFace().getDirection().multiply(1));
                                    player.sendMessage(translate("&a[TFMK] &7당신의 방이 아닙니다!"));
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 1L);
    }
}
