package ms.zero.tfmk.tfmkhidenseek.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.global.Util.translate;

public class HologramManager {

    private static HashMap<Integer, Hologram> hologramIDToHologram = new HashMap<>();
    private static HashMap<Integer, ChangeableHologramSet> clickableEntityIDToHologramSet = new HashMap<>();
    private static PacketAdapter clickListener;

    static {
        clickListener = new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Integer entityID = event.getPacket().getIntegers().read(0);
                Player player = event.getPlayer();
                if (clickableEntityIDToHologramSet.containsKey(entityID)) {
                    ChangeableHologramSet hologramSet = clickableEntityIDToHologramSet.get(entityID);
                    if (event.getPacket().getEntityUseActions().read(0) == EnumWrappers.EntityUseAction.INTERACT) {
                        if (event.getPacket().getHands().read(0) == EnumWrappers.Hand.MAIN_HAND) {
                            Integer reserved = hologramSet.getReserved(player);
                            player.sendMessage(translate("&6[!] &7전환되었습니다."));
                            if (reserved == 1) {
                                hologramSet.updateHologramTexts(player, HologramRankingPreset.getKeyRanking());
                            } else {
                                hologramSet.updateHologramTexts(player, HologramRankingPreset.getKillRanking());
                            }

                        }
                    }
                }
            }
        };
        protocolManager.addPacketListener(clickListener);
    }

    public static Hologram createHologram(String hologramText, Location hologramLocation, Boolean clickable) {
        Hologram hologram = new Hologram(hologramText, hologramLocation, clickable);
        hologramIDToHologram.put(hologram.getHologramID(), hologram);
        return hologram;
    }

    public static ChangeableHologramSet createHologramSet(List<Hologram> holograms) {
        ChangeableHologramSet hologramSet = new ChangeableHologramSet(holograms);
        hologramSet.getAllSlimeIDs().forEach(id -> clickableEntityIDToHologramSet.put(id, hologramSet));
        return hologramSet;
    }

    public static void clearAllHologramsFromPlayer(Player targetPlayer) {
        for (ChangeableHologramSet hologramSet : clickableEntityIDToHologramSet.values()) {
            hologramSet.removeHolograms(targetPlayer);
        }
        for (Hologram hologram : hologramIDToHologram.values()) {
            hologram.removeHologram(targetPlayer);
        }
    }

    public static void clearAllHologramObjects() {
        clickableEntityIDToHologramSet.clear();
        hologramIDToHologram.clear();
    }
}
