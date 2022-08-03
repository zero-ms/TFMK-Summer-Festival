package ms.zero.tfmk.tfmkhidenseek.hologram;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeableHologramSet {
    private ArrayList<Hologram> holograms;
    private HashMap<Player, Integer> reserved;
    private Integer reserved1;

    public ChangeableHologramSet(List<Hologram> holograms) {
        this.holograms = new ArrayList<>();
        for (Hologram hologram : holograms) {
            Hologram copiedHologram = new Hologram(hologram.getHologramText(), hologram.getHologramLocation(), hologram.getClickable());
            this.holograms.add(copiedHologram);
        }
        reserved = new HashMap<>();
    }

    public void setTotalReserved1(Integer reserved1) {
        this.reserved1 = reserved1;
    }

    public Integer getTotalReserved1() {
        return this.reserved1;
    }

    public Integer getReserved(Player player) {
        if (reserved.containsKey(player)) {
            Integer reserved2 = reserved.get(player);
            if (reserved2 < reserved1) {
                reserved.put(player, reserved2 + 1);
                return reserved2 + 1;
            } else {
                reserved.put(player, 1);
                return 1;
            }
        } else {
            reserved.put(player, 1);
            return 1;
        }
    }

    public void showHolograms(Player targetPlayer) {
        for (Hologram hologram : holograms) {
            hologram.showHologram(targetPlayer);
        }
    }

    public void removeHolograms(Player targetPlayer) {
        for (Hologram hologram : holograms) {
            hologram.removeHologram(targetPlayer);
        }
    }

    public void updateHologramTexts(Player targetPlayer, ArrayList<Hologram> changedHolograms) {
        int index = 0;
        for (Hologram hologram : holograms) {
            hologram.updateHologram(targetPlayer, changedHolograms.get(index++));
        }
    }

    public List<Integer> getAllSlimeIDs() {
        return holograms.stream().filter(Hologram::getClickable).map(Hologram::getClickableEntityID).collect(Collectors.toList());
    }
}
