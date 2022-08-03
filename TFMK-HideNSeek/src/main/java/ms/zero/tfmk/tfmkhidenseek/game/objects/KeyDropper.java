package ms.zero.tfmk.tfmkhidenseek.game.objects;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.game.util.GameVariable.*;

public class KeyDropper {

    private static HashMap<Location, Boolean> gennedKey = new HashMap<>();
    private static ArrayList<Location> keyGenLocations = new ArrayList<>();

    public static void resetAllKeys() {
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
    }

    public static void addGenerationPoint(Location location) {
        gennedKey.put(location, false);
        keyGenLocations.add(location);
    }

    public static Integer getRegisteredPointSize() {
        return keyGenLocations.size();
    }

    private static Location getRandomKeyLocation() {
        Collections.shuffle(keyGenLocations);
        int index = (int) (Math.random() * keyGenLocations.size());
        if (gennedKey.get(keyGenLocations.get(index))) {
            return getRandomKeyLocation();
        } else {
            gennedKey.put(keyGenLocations.get(index), true);
            return keyGenLocations.get(index);
        }
    }

    public static void dropKeyToRandomLocation() {
        Location randomLocation = getRandomKeyLocation();
        world.dropItemNaturally(randomLocation, KEY_PIECE);
    }
}
