package ms.zero.tfmk.tfmkhidenseek.objects;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Hologram {
    private Player player;
    private ArrayList<Integer> armorStandIDs = new ArrayList<>();
    private Integer hologramPage = 0;

    public Hologram(Player p) {
        this.player = p;
    }

    public void addArmorStandID(Integer id) {
        armorStandIDs.add(id);
    }

    public Player getPlayer() {
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
}
