package ms.zero.tfmk.tfmkhidenseek.global;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class GlobalVariable {

    public static Plugin plugin;
    public static World world = Bukkit.getWorld("world");
    public static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
}
