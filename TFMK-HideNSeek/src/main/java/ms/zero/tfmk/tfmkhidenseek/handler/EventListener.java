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
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerNPCRightClickEvent(NPCRightClickEvent e) {
        String npcName = ChatColor.stripColor(e.getNPC().getName());
        Player p = e.getClicker();
        if (npcName.equalsIgnoreCase("참가하기")) {
            if (!GameManager.join(p)) {
                p.sendMessage(translate("&6[TFMK] &7참가할 수 없습니다."));
            }
        } else if (npcName.equalsIgnoreCase("퇴장하기")) {
            if (!GameManager.quit(p, GameRule.Reason.NPC)) {
                p.sendMessage(translate("&6[TFMK] &7퇴장할 수 없습니다."));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent e) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            Player p = (Player) e.getWhoClicked();
            if (GameManager.isTagger(p)) {
                e.setCancelled(true);
            }
        }
        if (e.getCurrentItem() != null && e.getCurrentItem().equals(GOLDEN_HOE)) {
            Player p = (Player) e.getWhoClicked();
            if (GameManager.isTagger(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ItemStack i = e.getItemDrop().getItemStack();
        if (i.equals(GOLDEN_HOE)) {
            if (GameManager.isTagger(e.getPlayer())) {
                e.setCancelled(true);
            }
        }

        if (i.hasItemMeta()) {
            if (i.getItemMeta().getDisplayName().equals(KEY_PIECE.getItemMeta().getDisplayName())) {
                if (GameManager.isRunner(e.getPlayer())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Player attacker = (Player) e.getDamager();
            if (GameManager.isTagger(attacker) && GameManager.isRunner(victim)) {
                if (GameManager.isGameStarted()) {
                    GameManager.catchTheRunner(victim);
                } else {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        ItemStack i = e.getItem().getItemStack();
        if (i.hasItemMeta()) {
            if (i.getItemMeta().getDisplayName().equals(KEY_PIECE.getItemMeta().getDisplayName())) {
                Player p = (Player) e.getEntity();
                if (GameManager.isRunner(p)) {
                    if (GameManager.isGameStarted()) {
                        GameManager.pickUpKey((Player) e.getEntity());
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
