package ms.zero.tfmk.tfmkhidenseek.miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class GlobalVariable {
    public static ItemStack PUMPKIN_HELMET;
    public static ItemStack GOLDEN_HOE;
    public static ItemStack KEY_PIECE;
    static {
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
        lores.add(translate("&7이 지옥같은 저택을 벗어날 수 있는 열쇠다."));
        lores.add(translate("&f"));
        lores.add(translate("&c[주의사항]"));
        lores.add(translate("&7총 &c10&7개를 모아야 탈출이 가능합니다."));
        lores.add(translate("&7술래에게 잡히면 죽은 위치에 획득한 모든 열쇠를 &a드랍&7합니다."));
        meta.setLore(lores);
        KEY_PIECE.setItemMeta(meta);
    }
    public static Plugin plugin;
    public static World world = Bukkit.getWorld("world");
}
