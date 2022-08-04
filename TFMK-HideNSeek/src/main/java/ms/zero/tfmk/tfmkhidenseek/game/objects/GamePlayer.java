package ms.zero.tfmk.tfmkhidenseek.game.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GamePlayer {
    private Player player;
    private GameRule.PlayerType initialPlayerType;
    private GameRule.PlayerType finalPlayerType;
    private Integer pickedUpKeyScore = 0;
    private Integer killScore = 0;
    private ItemStack hatBackupItem;
    private ItemStack toolBackupItem;

    public GamePlayer(Player player, GameRule.PlayerType playerType) {
        this.player = player;
        this.initialPlayerType = playerType;
        this.finalPlayerType = playerType;
    }

    public void setBackupItem(ItemStack hat, ItemStack tool) {
        this.hatBackupItem = hat;
        this.toolBackupItem = tool;
    }

    public ItemStack getHatBackupItem() {
        return this.hatBackupItem;
    }

    public ItemStack getToolBackupItem() {
        return this.toolBackupItem;
    }

    public void setInitialPlayerType(GameRule.PlayerType playerType) {
        this.initialPlayerType = playerType;
    }
    public void setFinalPlayerType(GameRule.PlayerType playerType) {
        this.finalPlayerType = playerType;
    }

    public void addPickedUpKeyScore() {
        this.pickedUpKeyScore += 1;
    }

    public void addKillScore() {
        killScore += 1;
    }

    public GameRule.PlayerType getInitialPlayerType() {
        return this.initialPlayerType;
    }

    public GameRule.PlayerType getFinalPlayerType() {
        return this.finalPlayerType;
    }

    public Integer getPickedUpKeyScore() {
        return this.pickedUpKeyScore;
    }

    public Integer getKillScore() {
        return this.killScore;
    }

    public Player getPlayer() {
        return this.player;
    }
}
