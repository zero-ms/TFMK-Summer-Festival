package ms.zero.tfmk.tfmkhidenseek.handler;

import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

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
        if (e.getCurrentItem().hasItemMeta()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("술래의 낫")) {
                Player p = (Player) e.getWhoClicked();
                if (GameManager.isTagger(p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().hasItemMeta()) {
            if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("술래의 낫")) {
                Player p = e.getPlayer();
                if (GameManager.isTagger(p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }
}
