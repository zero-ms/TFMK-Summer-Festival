package ms.zero.tfmk.tfmkfishing;

import ms.zero.tfmk.tfmkfishing.command.FishCommand;
import ms.zero.tfmk.tfmkfishing.command.TabCompleter;
import ms.zero.tfmk.tfmkfishing.event.FishingEvent;
import ms.zero.tfmk.tfmkfishing.fishing.ContestManager;
import ms.zero.tfmk.tfmkfishing.fishing.objects.ContestTimer;
import ms.zero.tfmk.tfmkfishing.fishing.objects.FishType;
import ms.zero.tfmk.tfmkfishing.fishing.util.RandomSelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static ms.zero.tfmk.tfmkfishing.global.GlobalVariable.*;

public final class Tfmkfishing extends JavaPlugin {

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new FishingEvent(), this);
        getCommand("fishing").setExecutor(new FishCommand());
        getCommand("fishing").setTabCompleter(new TabCompleter());
        ContestTimer.locationClipper();
        ContestTimer.startTimer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Boolean isJoinedFishingContest(Player player) {
        return ContestManager.isContestPlayer(player);
    }
}
