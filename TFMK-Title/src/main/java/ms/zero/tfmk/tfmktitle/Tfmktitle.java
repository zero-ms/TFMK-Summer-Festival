package ms.zero.tfmk.tfmktitle;

import ms.zero.tfmk.tfmktitle.handler.CommandListener;
import ms.zero.tfmk.tfmktitle.objects.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ms.zero.tfmk.tfmktitle.handler.EventListener;

public final class Tfmktitle extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("titleupdate").setExecutor(new CommandListener());
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void updatePlayersTeam() {
        TeamManager.updatePlayersTeam();
    }
}
