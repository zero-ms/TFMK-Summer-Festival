package ms.zero.tfmk.tfmkfishing.fishing.objects;

import ms.zero.tfmk.tfmkfishing.fishing.util.FishInfo;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ContestPlayer {
    private Player player;
    private Integer score;
    private ArrayList<FishType> caughtFish;
    private ItemStack backedUpItem;

    public ContestPlayer(Player player, ItemStack backUpItem) {
        this.player = player;
        score = 0;
        caughtFish = new ArrayList<>();
        this.backedUpItem = backUpItem;
    }

    public void caughtFish(FishType fishType) {
        caughtFish.add(fishType);
        score += FishInfo.getScoreFromFishType(fishType);
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getBackedUpItem() {
        return this.backedUpItem;
    }

    public Integer getScore() {
        return this.score;
    }
}
