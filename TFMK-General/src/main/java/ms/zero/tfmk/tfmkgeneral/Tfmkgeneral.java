package ms.zero.tfmk.tfmkgeneral;

import ms.zero.tfmk.tfmkgeneral.event.JoinQuitEvent;
import ms.zero.tfmk.tfmkgeneral.event.PortalEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static ms.zero.tfmk.tfmkgeneral.global.GlobalVariable.*;

public final class Tfmkgeneral extends JavaPlugin {

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new JoinQuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PortalEvent(), this);
    }

    @Override
    public void onDisable() {

    }
}
