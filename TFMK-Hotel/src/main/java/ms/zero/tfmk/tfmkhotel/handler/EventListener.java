package ms.zero.tfmk.tfmkhotel.handler;

import ms.zero.tfmk.tfmkhotel.Objects.HotelManger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static ms.zero.tfmk.tfmkhotel.miscellaneous.Util.translate;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType().equals(Material.DARK_OAK_DOOR)) {
                SignChecker checker = new SignChecker(b.getLocation());
                Integer roomNumber = checker.getRoomNumberFromSign();
                if (roomNumber > 1000) {
                    if (!HotelManger.isOwner(p.getUniqueId(), roomNumber)) {
                        e.setCancelled(true);

                        BlockData data = b.getBlockData();
                        Openable o = (Openable) data;
                        o.setOpen(false);
                        b.setBlockData(o);

                        p.sendMessage(translate("&a[TFMK] &7당신의 방이 아닙니다!"));
                    }
                } else {
                    p.sendMessage(translate("&c[ERROR] &7SignChecker error. 관리자에게 문의하세요."));
                }
            }
        }
    }
}
