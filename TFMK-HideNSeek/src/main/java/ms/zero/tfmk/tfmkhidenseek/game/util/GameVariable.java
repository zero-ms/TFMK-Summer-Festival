package ms.zero.tfmk.tfmkhidenseek.game.util;

import java.util.ArrayList;

import ms.zero.tfmk.tfmkhidenseek.game.objects.GameRule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.world;
import static ms.zero.tfmk.tfmkhidenseek.global.Util.*;

public class GameVariable {

    public static ItemStack PUMPKIN_HELMET;
    public static ItemStack GOLDEN_HOE;
    public static ItemStack KEY_PIECE;
    public static Location statisticsNPCLocation = new Location(Bukkit.getWorld("world"), 282.5, 84, -101.5, 180.0f, 0.0f);

    public static Location[] barrierLocation = new Location[30];
    public static Location baseLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -94.5, 180, 5);
    public static Location lobbyLocation = new Location(Bukkit.getWorld("world"), 283.5, 76, -104.5, 180, 5);
    public static Location resultLocation = new Location(Bukkit.getWorld("world"), 283.5, 84, -105.5);

    static {
        for (int i = 0; i < 5; i++) {
            barrierLocation[i] = new Location(world, 275, 78, -103 + (i * -1));
        }
        for (int i = 5; i < 10; i++) {
            barrierLocation[i] = new Location(world, 275, 77, -103 + ((i - 5) * -1));
        }
        for (int i = 10; i < 15; i++) {
            barrierLocation[i] = new Location(world, 291, 78, -103 + ((i - 10) * -1));
        }
        for (int i = 15; i < 20; i++) {
            barrierLocation[i] = new Location(world, 291, 77, -103 + ((i - 15) * -1));
        }
        for (int i = 20; i < 23; i++) {
            barrierLocation[i] = new Location(world, 282 + (i - 20), 78, -108);
        }
        for (int i = 23; i < 26; i++) {
            barrierLocation[i] = new Location(world, 282 + (i - 23), 77, -108);
        }
        barrierLocation[26] = new Location(world, 285, 79, -109);
        barrierLocation[27] = new Location(world, 285, 78, -108);
        barrierLocation[28] = new Location(world, 281, 79, -109);
        barrierLocation[29] = new Location(world, 281, 78, -108);
    }

    public static void initGameItem() {
        PUMPKIN_HELMET = new ItemStack(Material.CARVED_PUMPKIN);
        GOLDEN_HOE = new ItemStack(Material.GOLDEN_HOE);

        ItemMeta goldHoeMeta = GOLDEN_HOE.getItemMeta();
        goldHoeMeta.setDisplayName(translate("&6술래의 낫"));
        ArrayList<String> lores = new ArrayList<>();
        lores.add(translate("&8술래들이 사용하는 낫이다."));
        goldHoeMeta.setLore(lores);
        GOLDEN_HOE.setItemMeta(goldHoeMeta);

        KEY_PIECE = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = KEY_PIECE.getItemMeta();
        meta.setDisplayName(translate("&e해방의 열쇠 조각"));
        lores = new ArrayList<>();
        lores.add(translate("&7이 지옥 같은 멘션을 벗어날 수 있는 열쇠다."));
        lores.add(translate("&f"));
        lores.add(translate("&c[주의사항]"));
        lores.add(String.format(translate("&7총 &c%d&7개를 모아야 탈출이 가능합니다."), GameRule.getNeedKey()));
        lores.add(translate("&7술래에게 잡히면 죽은 위치에 획득한 모든 열쇠를 &a드랍&7합니다."));
        meta.setLore(lores);
        KEY_PIECE.setItemMeta(meta);
    }
}
