package ms.zero.tfmk.tfmkmenu;

import ms.zero.tfmk.tfmkmenu.command.WarpCommand;
import ms.zero.tfmk.tfmkmenu.event.GUIEvent;
import ms.zero.tfmk.tfmkmenu.event.SneakSwapEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tfmkmenu extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new GUIEvent(), this);
        Bukkit.getPluginManager().registerEvents(new SneakSwapEvent(), this);

        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("menu").setExecutor(new WarpCommand());
        getCommand("워프").setExecutor(new WarpCommand());
        getCommand("메뉴").setExecutor(new WarpCommand());
    }

    @Override
    public void onDisable() {

    }
}
