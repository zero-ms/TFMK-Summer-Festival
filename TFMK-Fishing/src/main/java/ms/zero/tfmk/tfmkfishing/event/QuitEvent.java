package ms.zero.tfmk.tfmkfishing.event;

import ms.zero.tfmk.tfmkfishing.fishing.ContestManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        if (ContestManager.isContestPlayer(player)) {
            ContestManager.quit(player);
        }
    }
}
