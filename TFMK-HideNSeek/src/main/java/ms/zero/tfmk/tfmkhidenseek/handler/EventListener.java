package ms.zero.tfmk.tfmkhidenseek.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerNPCRightClickEvent(NPCRightClickEvent npcRightClickEvent) {
        String npcName = ChatColor.stripColor(npcRightClickEvent.getNPC().getName());
        Player clicker = npcRightClickEvent.getClicker();
        if (npcName.equalsIgnoreCase("참가하기")) {
            if (!GameManager.join(clicker)) {
                clicker.sendMessage(translate("&6[TFMK] &7참가할 수 없습니다."));
            }
        } else if (npcName.equalsIgnoreCase("퇴장하기")) {
            if (!GameManager.quit(clicker, GameRule.Reason.NPC)) {
                clicker.sendMessage(translate("&6[TFMK] &7퇴장할 수 없습니다."));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent playerQuitEvent) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player clicker = (Player) inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            if (GameManager.isTagger(clicker)) {
                inventoryClickEvent.setCancelled(true);
            }
        }
        if (inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().equals(GOLDEN_HOE)) {
            if (GameManager.isTagger(clicker)) {
                inventoryClickEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent playerDropItemEvent) {
        ItemStack droppedItem = playerDropItemEvent.getItemDrop().getItemStack();
        if (droppedItem.equals(GOLDEN_HOE)) {
            if (GameManager.isTagger(playerDropItemEvent.getPlayer())) {
                playerDropItemEvent.setCancelled(true);
            }
        }

        if (droppedItem.hasItemMeta()) {
            if (droppedItem.getItemMeta().getDisplayName().equals(KEY_PIECE.getItemMeta().getDisplayName())) {
                if (GameManager.isRunner(playerDropItemEvent.getPlayer())) {
                    playerDropItemEvent.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager() instanceof Player && entityDamageByEntityEvent.getEntity() instanceof Player) {
            Player victim = (Player) entityDamageByEntityEvent.getEntity();
            Player attacker = (Player) entityDamageByEntityEvent.getDamager();
            if (GameManager.isTagger(attacker) && GameManager.isRunner(victim)) {
                if (GameManager.isGameStarted()) {
                    GameManager.catchTheRunner(attacker, victim);
                } else {
                    entityDamageByEntityEvent.setCancelled(true);
                }
            } else {
                entityDamageByEntityEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            entityDamageEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent entityPickupItemEvent) {
        ItemStack pickedUpItem = entityPickupItemEvent.getItem().getItemStack();
        if (pickedUpItem.hasItemMeta()) {
            if (pickedUpItem.getItemMeta().getDisplayName().equals(KEY_PIECE.getItemMeta().getDisplayName())) {
                Player eventEntity = (Player) entityPickupItemEvent.getEntity();
                if (GameManager.isRunner(eventEntity)) {
                    if (GameManager.isGameStarted()) {
                        GameManager.pickUpKey((Player) entityPickupItemEvent.getEntity());
                    } else {
                        entityPickupItemEvent.setCancelled(true);
                    }
                } else {
                    entityPickupItemEvent.setCancelled(true);
                }
            }
        }
    }
}
