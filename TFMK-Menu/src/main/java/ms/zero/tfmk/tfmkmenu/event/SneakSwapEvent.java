package ms.zero.tfmk.tfmkmenu.event;

import ms.zero.tfmk.tfmkmenu.gui.GuiHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SneakSwapEvent implements Listener {
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent swapHandItemsEvent) {
        Player player = swapHandItemsEvent.getPlayer();
        if (player.isSneaking()) {
            swapHandItemsEvent.setCancelled(true);
            GuiHandler.openWarpGui(player);
        }
    }
}
