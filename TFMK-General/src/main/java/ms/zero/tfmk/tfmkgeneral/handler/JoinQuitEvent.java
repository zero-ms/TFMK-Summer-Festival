package ms.zero.tfmk.tfmkgeneral.handler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static ms.zero.tfmk.tfmkgeneral.miscellaneous.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkgeneral.miscellaneous.Util.*;

public class JoinQuitEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        playerJoinEvent.setJoinMessage(String.format(translate("&a[+] &f%s &7&o(%d명)"), player.getName(), Bukkit.getOnlinePlayers().size()));
        if (!player.hasPlayedBefore()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.teleport(new Location(Bukkit.getWorld("world"), -744.5, 90, 828.5, 180.0f, 0.0f));
                }
            }, 5L);

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        playerQuitEvent.setQuitMessage(String.format(translate("&c[-] &f%s &7&o(%d명)"), player.getName(), Bukkit.getOnlinePlayers().size()));
    }
}
