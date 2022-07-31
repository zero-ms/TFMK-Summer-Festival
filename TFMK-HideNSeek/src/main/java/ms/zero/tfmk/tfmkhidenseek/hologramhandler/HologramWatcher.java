package ms.zero.tfmk.tfmkhidenseek.hologramhandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HologramWatcher {
    private Player player;
    private LinkedHashMap<Integer, Boolean> armorStandIDs = new LinkedHashMap<>();
    private Integer hologramPage = 0;

    public HologramWatcher(Player p) {
        this.player = p;
    }

    public void addArmorStandID(Integer id, Boolean rankHologram) {
        armorStandIDs.put(id, rankHologram);
    }

    public Player getWatcher() {
        return this.player;
    }

    public Integer changePage() {
        if (hologramPage == 0) {
            hologramPage = 1;
            return 1;
        } else {
            hologramPage = 0;
            return 0;
        }
    }

    public ArrayList<Integer> getRankArmorStands() {
        return new ArrayList<>(armorStandIDs.keySet()).stream().filter(id -> armorStandIDs.get(id)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Integer> getAllArmorStands() {
        return new ArrayList<>(this.armorStandIDs.keySet());
    }
}