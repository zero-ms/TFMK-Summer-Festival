package ms.zero.tfmk.tfmkhidenseek.handler;

import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerNPCRightClickEvent(NPCRightClickEvent e) {
        String npcName = ChatColor.stripColor(e.getNPC().getName());
        Player p = e.getClicker();
        if (npcName.equalsIgnoreCase("참가하기")) {
            if (!GameManager.join(p)) {
                p.sendMessage(translate("&6[TFMK] &7이미 게임이 시작됐습니다."));
            }
        } else if (npcName.equalsIgnoreCase("퇴장하기")) {
            if (!GameManager.quit(p)) {
                p.sendMessage(translate("&6[TFMK] &7이미 게임이 시작됐습니다."));
            }
        }
    }
}
