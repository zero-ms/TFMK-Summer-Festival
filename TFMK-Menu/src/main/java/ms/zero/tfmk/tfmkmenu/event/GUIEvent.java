package ms.zero.tfmk.tfmkmenu.event;

import ms.zero.tfmk.tfmkmenu.gui.GuiHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIEvent implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player clicker = (Player) inventoryClickEvent.getWhoClicked();
        String inventoryName = ChatColor.stripColor(inventoryClickEvent.getView().getTitle());
        if (inventoryName.equalsIgnoreCase("워프 메뉴")) {
            inventoryClickEvent.setCancelled(true);
            GuiHandler.teleportFromClickedItem(clicker, inventoryClickEvent.getSlot());
        }
    }
}
