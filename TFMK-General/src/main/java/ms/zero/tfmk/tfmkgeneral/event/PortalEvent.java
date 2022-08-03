package ms.zero.tfmk.tfmkgeneral.event;

import ms.zero.tfmk.tfmkgeneral.global.PortalLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalEvent implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        Location location = playerMoveEvent.getFrom();
        if (PortalLocation.isInside(location)) {
            player.teleport(new Location(Bukkit.getWorld("world"), 477.5, 61, 245.5, 90, 0));
        }
    }
}
