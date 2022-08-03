package ms.zero.tfmk.tfmkhidenseek.global;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Util {
    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void removePotionEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static void removeItem(Player player, ItemStack item) {
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && i.hasItemMeta()) {
                if (i.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    player.getInventory().remove(i);
                }
            }
        }
    }
}
