package ms.zero.tfmk.tfmkgeneral.event;

import ms.zero.tfmk.tfmkgeneral.global.PortalLocation;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkgeneral.global.GlobalVariable.*;

public class PortalChecker {
    public static void startPortalChecker() {
        World world = Bukkit.getWorld("world");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Chunk portalChunk = new Location(world, -745, 91, 762).getChunk();
            for (Entity entity : portalChunk.getEntities()) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (PortalLocation.isInside(player.getLocation())) {
                        player.teleport(new Location(Bukkit.getWorld("world"), 477.5, 61, 245.5, 90, 0));
                    }
                }
            }
        }, 0, 10L);
    }
}
