package ms.zero.tfmk.tfmkhidenseek.event;

import ms.zero.tfmk.tfmkhidenseek.game.GameManager;
import ms.zero.tfmk.tfmkhidenseek.game.objects.GameRule;
import ms.zero.tfmk.tfmkhidenseek.game.objects.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static ms.zero.tfmk.tfmkhidenseek.game.util.GameVariable.*;

public class GameEvent implements Listener {

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent playerQuitEvent) {
        Player offlinePlayer = playerQuitEvent.getPlayer();
        if (GameManager.isPlaying(offlinePlayer)) {
            if (GameManager.getGameStatus() == GameStatus.PLAYING || GameManager.getGameStatus() == GameStatus.ENDING) {
                GameManager.quit(offlinePlayer, GameRule.Reason.FORCE);
            } else {
                GameManager.quit(offlinePlayer, GameRule.Reason.NPC);
            }

        }
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
        if (entityDamageByEntityEvent.getDamager() instanceof Player
                && entityDamageByEntityEvent.getEntity() instanceof Player) {
            Player victim = (Player) entityDamageByEntityEvent.getEntity();
            Player attacker = (Player) entityDamageByEntityEvent.getDamager();
            if (GameManager.isTagger(attacker) && GameManager.isRunner(victim)) {
                if (GameManager.getGameStatus() == GameStatus.PLAYING) {
                    try {
                        GameManager.catchTheRunner(attacker, victim);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        GameManager.interruptGame();
                    }
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
                    if (GameManager.getGameStatus() == GameStatus.PLAYING) {
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
