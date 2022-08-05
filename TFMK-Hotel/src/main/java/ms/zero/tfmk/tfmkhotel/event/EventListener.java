package ms.zero.tfmk.tfmkhotel.event;

import ms.zero.tfmk.tfmkhotel.hotel.HotelManger;
import ms.zero.tfmk.tfmkhotel.hotel.SignChecker;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static ms.zero.tfmk.tfmkhotel.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhotel.global.Util.translate;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!p.getName().equals("Bamboo_Photo")) {
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
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerNPCRightClickEvent(NPCRightClickEvent npcRightClickEvent) {
        String npcName = ChatColor.stripColor(npcRightClickEvent.getNPC().getName());
        Player player = npcRightClickEvent.getClicker();
        if (npcName.contains("호텔리어")) {
            switch(HotelManger.assignNewRoom(player, Integer.valueOf(npcName.replace("동 호텔리어", "")))) {
                case REQUEST_SUCCESS:
                    player.sendMessage(translate("&a[TFMK] &7방을 할당받았습니다!"));
                    player.sendMessage(String.format(translate("&a[TFMK] &7방 번호: &6%d&7호"), HotelManger.get(player.getUniqueId()).getNumber()));
                    break;
                case ALREADY_ASSIGNED_PLAYER:
                    player.sendMessage(translate("&a[TFMK] &7이미 방이 있습니다."));
                    player.sendMessage(String.format(translate("&a[TFMK] &7손님의 방은 &6%d&7호 입니다."), HotelManger.get(player.getUniqueId()).getNumber()));
                    break;
                case ROOM_INSUFFICIENT:
                    player.sendMessage(translate("&a[TFMK] &7방이 &c부족&7합니다. 관리자에게 문의하세요."));
                    break;
                case REQUEST_FAILED:
                    player.sendMessage(translate("&a[TFMK] &7방 배정에 실패하였습니다."));
                    player.sendMessage(translate("&a[TFMK] &7호텔 건물을 잘못 입력했을 확률이 높습니다."));
                    break;
                default:
                    player.sendMessage(translate("&c[ERROR] &7ReturnType error. 관리자에게 문의하세요."));
                    break;
            }
        }
    }
}
