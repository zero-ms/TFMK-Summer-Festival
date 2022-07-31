package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import org.bukkit.entity.Player;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class GamePlayer {
    private Player player;
    private GameRule.PlayerType initialPlayerType;
    private GameRule.PlayerType finalPlayerType;
    private Integer pickedUpKeyScore = 0;
    private Integer killScore = 0;

    public GamePlayer(Player player, GameRule.PlayerType playerType) {
        this.player = player;
        this.initialPlayerType = playerType;
        this.finalPlayerType = playerType;
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
