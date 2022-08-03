package ms.zero.tfmk.tfmkfishing.event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import ms.zero.tfmk.tfmkfishing.fishing.ContestManager;

public class FishingEvent implements Listener {
    @EventHandler
    public void onPlayerFishing(PlayerFishEvent fishEvent) {
        Player fisher = fishEvent.getPlayer();
        if (ContestManager.isContestPlayer(fisher)) {
            if (fishEvent.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                fishEvent.setCancelled(true);
                Entity fishingRod = fishEvent.getHook();
                fishingRod.remove();
                ContestManager.catchTheFish(fisher);
            }
        } else {
            fishEvent.setCancelled(true);
        }

    }
}
