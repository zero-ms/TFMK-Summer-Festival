package ms.zero.tfmk.tfmkhidenseek.game.objects;

public class GameRule {
    private static Integer minPlayers = 0;
    private static Integer leastTaggers = 0;
    private static Integer needKey = 0;

    public enum PlayerType {
        RUNNER,
        TAGGER,
    }

    public enum Reason {
        NPC,
        FORCE,
    }

    public enum EndReason {
        TAGGER_WIN,
        TAGGER_INSUFFICIENT,
        KEY_SUFFICIENT,
        TIMEOUT,
        CAN_PLAY,
    }

    public static Integer getMinPlayers() {
        return minPlayers;
    }

    public static Integer getLeastTaggers() {
        return leastTaggers;
    }

    public static Integer getNeedKey() {
        return needKey;
    }

    public static void setMinPlayers(Integer minPlayers) {
        GameRule.minPlayers = minPlayers;
    }

    public static void setLeastTaggers(Integer leastTaggers) {
        GameRule.leastTaggers = leastTaggers;
    }

    public static void setNeedKey(Integer needKey) {
        GameRule.needKey = needKey;
    }
}
