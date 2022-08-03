package ms.zero.tfmk.tfmktitle.event;

import ms.zero.tfmk.tfmktitle.objects.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static ms.zero.tfmk.tfmktitle.global.Util.translate;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        TeamManager.updatePlayersTeam();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        TeamManager.updatePlayersTeam();
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent asyncPlayerChatEvent) {
        Player player = asyncPlayerChatEvent.getPlayer();
        if (player.isOp()) {
            asyncPlayerChatEvent.setFormat(translate("&c[ADMIN] &f%1$s: %2$s"));
        } else {
            asyncPlayerChatEvent.setFormat(translate("&7[USER] &f%1$s: %2$s"));
        }
    }
}
