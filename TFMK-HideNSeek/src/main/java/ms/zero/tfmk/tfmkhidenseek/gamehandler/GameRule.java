package ms.zero.tfmk.tfmkhidenseek.gamehandler;

public class GameRule {
	private static Integer minPlayers;
	private static Integer leastTaggers;
	private static Integer needKey;

	public enum PlayerType {
		RUNNER,
		TAGGER,
	}

	public enum Reason {
		NPC,
		FORCE,
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
	public static void setMinPlayers(Integer i) {
		minPlayers = i;
	}
	public static void setLeastTaggers(Integer i) {
		leastTaggers = i;
	}
	public static void setNeedKey(Integer i) {
		needKey = i;
	}
}
