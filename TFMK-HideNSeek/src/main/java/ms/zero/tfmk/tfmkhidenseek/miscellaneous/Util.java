package ms.zero.tfmk.tfmkhidenseek.miscellaneous;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.GOLDEN_HOE;

public class Util {
    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void removePotionEffects(Player p) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    public static void removeItem(Player p, ItemStack item) {
        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null && i.hasItemMeta()) {
                if (i.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    p.getInventory().remove(i);
                }
            }
        }
    }
}
