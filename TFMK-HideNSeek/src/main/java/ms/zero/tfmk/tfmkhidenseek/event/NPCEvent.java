package ms.zero.tfmk.tfmkhidenseek.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import ms.zero.tfmk.tfmkhidenseek.game.GameManager;
import ms.zero.tfmk.tfmkhidenseek.global.HotelChecker;
import ms.zero.tfmk.tfmkhidenseek.game.util.GameRule;
import net.citizensnpcs.api.event.NPCRightClickEvent;

import static ms.zero.tfmk.tfmkhidenseek.global.Util.*;

public class NPCEvent implements Listener {

    @EventHandler
    public void onPlayerNPCRightClickEvent(NPCRightClickEvent npcRightClickEvent) {
        String npcName = ChatColor.stripColor(npcRightClickEvent.getNPC().getName());
        Player clicker = npcRightClickEvent.getClicker();
        if (npcName.equalsIgnoreCase("참가하기")) {
            if (HotelChecker.isPlayerAlreadyAssigned(clicker)) {
                if (!GameManager.join(clicker)) {
                    clicker.sendMessage(translate("&6[TFMK] &7참가할 수 없습니다."));
                }
            } else {
                clicker.sendMessage(translate("&6[TFMK] &7호텔 체크인을 먼저 해주세요."));
            }
        } else if (npcName.equalsIgnoreCase("퇴장하기")) {
            if (!GameManager.quit(clicker, GameRule.Reason.NPC)) {
                clicker.sendMessage(translate("&6[TFMK] &7퇴장할 수 없습니다."));
            }
        }
    }
}
