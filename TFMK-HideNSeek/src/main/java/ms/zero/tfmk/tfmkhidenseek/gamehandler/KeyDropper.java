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
    private static HashMap<Location, Boolean> gennedKey = new HashMap<>();
    private static ArrayList<Location> keyGenLocations = new ArrayList<>();

    public static void reset() {
        for (Location location : keyGenLocations) {
            gennedKey.put(location, false);
        }
        List<Entity> entityList = world.getEntities();
        int count = 0;
        for (Entity entity : entityList) {
            if (entity instanceof Item) {
                ItemStack droppedItem = ((Item) entity).getItemStack();
                if (droppedItem.hasItemMeta()) {
                    if (droppedItem.getItemMeta().getDisplayName().contains("해방의 열쇠 조각")) {
                        count += 1;
                        entity.remove();
                    }
                }
            }
        }
        Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] &7열쇠조각 &c%d&7개가 삭제됨."), count));
    }

    public static void put(Location location) {
        gennedKey.put(location, false);
        keyGenLocations.add(location);
    }

    public static int size() {
        return keyGenLocations.size();
    }

    private static Location getRandomLocation() {
        Collections.shuffle(keyGenLocations);
        int index = (int) (Math.random() * keyGenLocations.size());
        if (gennedKey.get(keyGenLocations.get(index))) {
            return getRandomLocation();
        } else {
            gennedKey.put(keyGenLocations.get(index), true);
            return keyGenLocations.get(index);
        }
    }

    public static void spawnKey() {
        Location randomLocation = getRandomLocation();
        world.dropItemNaturally(randomLocation, KEY_PIECE);
        Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] &7열쇠조각 좌표: %d, %d, %d"),
                randomLocation.getBlockX(), randomLocation.getBlockY(), randomLocation.getBlockZ()));
    }
}
