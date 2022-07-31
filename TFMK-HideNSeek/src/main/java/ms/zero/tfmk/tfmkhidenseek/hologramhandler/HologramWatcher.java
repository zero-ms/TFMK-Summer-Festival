package ms.zero.tfmk.tfmkhidenseek.hologramhandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HologramWatcher {
    private Player player;
    private HashMap<Integer, Boolean> armorStandIDs = new HashMap<>();
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
        ArrayList<Integer> sortedArmorStandIDs = armorStandIDs.keySet().stream().filter(id -> armorStandIDs.get(id)).collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(sortedArmorStandIDs, Collections.reverseOrder());
        return sortedArmorStandIDs;
    }

    public ArrayList<Integer> getAllArmorStands() {
        return new ArrayList<>(this.armorStandIDs.keySet());
    }
}
