package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.world;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class KeyDropper {
    private static HashMap<Location, Boolean> keyGenMap = new HashMap<>();
    private static ArrayList<Location> keyGenList = new ArrayList<>();
    private static ItemStack keyItem;
    static {
        keyItem = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = keyItem.getItemMeta();
        meta.setDisplayName(translate("&e해방의 열쇠 조각"));
        ArrayList<String> lores = new ArrayList<>();
        lores.add(translate("&7이 지옥같은 저택을 벗어날 수 있는 열쇠다."));
        lores.add(translate("&f"));
        lores.add(translate("&c[주의사항]"));
        lores.add(translate("&7총 &c10&7개를 모아야 탈출이 가능합니다."));
        lores.add(translate("&7술래에게 잡히면 죽은 위치에 획득한 모든 열쇠를 &a드랍&7합니다."));
        meta.setLore(lores);
        keyItem.setItemMeta(meta);
    }

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
                        count++;
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
        world.dropItemNaturally(l, keyItem);
        Bukkit.getPlayer("Bamboo_Photo").sendMessage(String.format(translate("&c[DEBUG] 열쇠조각 좌표: %d, %d, %d"), l.getBlockX(), l.getBlockY(), l.getBlockZ()));
    }
}
