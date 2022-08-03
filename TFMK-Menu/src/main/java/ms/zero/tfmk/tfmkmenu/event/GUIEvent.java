package ms.zero.tfmk.tfmkmenu.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIEvent implements Listener {
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player clicker = (Player) inventoryClickEvent.getWhoClicked();
        Inventory clickedInventory = inventoryClickEvent.getView().getTopInventory();
        
    }
}
