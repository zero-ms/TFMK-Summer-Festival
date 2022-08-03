package ms.zero.tfmk.tfmkhotel.global;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class GlobalVariable {
    public static Plugin plugin;
    public enum ReturnType {
        ERROR,
        ROOM_INSUFFICIENT,
        PERMISSION_DENIED,
        REQUEST_SUCCESS,
        REQUEST_FAILED,
        ALREADY_ASSIGNED_PLAYER,
    }

    public static ArrayList<Player> eventLockList = new ArrayList<>();
}


