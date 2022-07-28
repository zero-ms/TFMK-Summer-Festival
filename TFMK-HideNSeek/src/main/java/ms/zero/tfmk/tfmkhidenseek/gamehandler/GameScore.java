package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class GameScore {
    private static HashMap<Player, Integer> pickedUpKeyMap = new HashMap<>();
    private static Integer droppedKey = 0;
    private static Integer pickedUpKey = 0;
    private static Integer tagger = 0;
    private static Integer runner = 0;

    public static void initScores() {
        pickedUpKeyMap.clear();
        droppedKey = 0;
        pickedUpKey = 0;
        tagger = 0;
        runner = 0;
    }

    public static void addPlayer(Player p, Integer i) {
        pickedUpKeyMap.put(p, i);
    }

    public static Integer getPlayerPickedUpKey(Player p) {
        return pickedUpKeyMap.getOrDefault(p, 0);
    }

    public static void setDroppedKey(Integer i) {
        droppedKey = i;
    }

    public static Integer getDroppedKey() {
        return droppedKey;
    }

    public static void setPickedUpKey(Integer i) {
        pickedUpKey = i;
    }

    public static Integer getPickedUpKey() {
        return pickedUpKey;
    }

    public static void dropKey(Player p) {
        droppedKey += pickedUpKeyMap.get(p);
        pickedUpKey -= pickedUpKeyMap.get(p);
        pickedUpKeyMap.put(p, 0);
    }

    public static void pickUpKey(Player p) {
        pickedUpKeyMap.put(p, pickedUpKeyMap.get(p) + 1);
        droppedKey -= 1;
        pickedUpKey += 1;
    }

    public static void initPlayers(Integer t, Integer r) {
        tagger = t;
        runner = r;
    }

    public static void decreaseRunner() {
        tagger += 1;
        runner -= 1;
    }

    public static Integer getTagger() {
        return tagger;
    }

    public static Integer getRunner() {
        return runner;
    }
}
