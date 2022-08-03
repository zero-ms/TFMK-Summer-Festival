package ms.zero.tfmk.tfmkhidenseek.npc;

import org.bukkit.entity.Player;

import java.util.*;

public class NPCManager {
    private static HashMap<Player, NPC> npcByPlayer = new HashMap<>();

    public static NPC createNewNPCForPlayer(Player player) {
        NPC npc = new NPC();
        npcByPlayer.put(player, npc);
        return npc;
    }

    public static void removeNPCFromPlayer(Player player) {
        if (npcByPlayer.containsKey(player)) {
            NPC npc = npcByPlayer.get(player);
            npc.removeNPC(player);
        }
    }

    public static void clearNPCObjects() {
        npcByPlayer.clear();
    }

    public static String getRandomNPCName() {
        return "tfmknpc";
    }
}
