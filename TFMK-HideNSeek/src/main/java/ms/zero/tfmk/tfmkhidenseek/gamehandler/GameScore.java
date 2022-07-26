package ms.zero.tfmk.tfmkhidenseek.gamehandler;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class GameScore {
    private static HashMap<Player, Integer> pickedUpKeyMap = new HashMap<>();
    private static Integer droppedKeyVolume = 0;
    private static Integer pickedUpKeyVolume = 0;
    private static Integer taggerVolume = 0;
    private static Integer runnerVolume = 0;

    public static void initScores() {
        pickedUpKeyMap.clear();
        droppedKeyVolume = 0;
        pickedUpKeyVolume = 0;
        taggerVolume = 0;
        runnerVolume = 0;
    }

    public static void addPlayer(Player p, Integer i) {
        pickedUpKeyMap.put(p, i);
    }

    public static Integer getPlayerPickedUpKeyVolume(Player p) {
        if (pickedUpKeyMap.containsKey(p)) {
            return pickedUpKeyMap.get(p);
        } else {
            return 0;
        }
    }

    public static void setDroppedKeyVolume(Integer i) {
        droppedKeyVolume = i;
    }

    public static Integer getDroppedKeyVolume() {
        return droppedKeyVolume;
    }

    public static void setPickedUpKeyVolume(Integer i) {
        pickedUpKeyVolume = i;
    }

    public static Integer getPickedUpKeyVolume() {
        return pickedUpKeyVolume;
    }

    public static void dropKey(Player p) {
        droppedKeyVolume += pickedUpKeyMap.get(p);
        pickedUpKeyVolume -= pickedUpKeyMap.get(p);
        pickedUpKeyMap.put(p, 0);
    }

    public static void pickUpKey(Player p) {
        pickedUpKeyMap.put(p, pickedUpKeyMap.get(p) + 1);
        droppedKeyVolume -= 1;
        pickedUpKeyVolume += 1;
    }

    public static void initPlayersVolume(Integer tagger, Integer runner) {
        taggerVolume = tagger;
        runnerVolume = runner;
    }

    public static void decreaseRunner() {
        taggerVolume += 1;
        runnerVolume -= 1;
    }

    public static Integer getTaggerVolume() {
        return taggerVolume;
    }

    public static Integer getRunnerVolume() {
        return runnerVolume;
    }
}
