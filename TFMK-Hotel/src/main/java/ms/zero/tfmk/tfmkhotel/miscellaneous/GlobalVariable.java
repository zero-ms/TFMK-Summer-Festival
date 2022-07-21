package ms.zero.tfmk.tfmkhotel.miscellaneous;

import org.bukkit.plugin.Plugin;

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
}


