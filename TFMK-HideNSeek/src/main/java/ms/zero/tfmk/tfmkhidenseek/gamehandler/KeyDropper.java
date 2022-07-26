package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class KeyDropper {
    private static HashMap<Location, Boolean> keyGenMap = new HashMap<>();
    private static ArrayList<Location> keyGenList = new ArrayList<>();
    public static void reset() {
        for (Location l : keyGenList) {
            keyGenMap.put(l, false);
        }
        List<Entity> entityList = world.getEntities();
        int count = 0;
        for (Entity e : entityList) {
            if (e instanceof Item) {
                ItemStack i = ((Item) e).getItemStack();
                if (i.hasItemMeta()) {
                    if (i.getItemMeta().getDisplayName().contains("해방의 열쇠 조각")) {
                        count += 1;
                        e.remove();
                    }
                }
            }
        }
        Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] &7열쇠조각 &c%d&7개가 삭제됨."), count));
    }

    public static void put(Location l) {
        keyGenMap.put(l, false);
        keyGenList.add(l);
    }

    public static int size() {
        return keyGenList.size();
    }

    private static Location getRandomLocation() {
        Collections.shuffle(keyGenList);
        int index = (int) (Math.random() * keyGenList.size());
        if (keyGenMap.get(keyGenList.get(index))) {
            return getRandomLocation();
        } else {
            keyGenMap.put(keyGenList.get(index), true);
            return keyGenList.get(index);
        }
    }

    public static void spawnKey() {
        Location l = getRandomLocation();
        world.dropItemNaturally(l, KEY_PIECE);
        Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] &7열쇠조각 좌표: %d, %d, %d"), l.getBlockX(), l.getBlockY(), l.getBlockZ()));
    }
}
