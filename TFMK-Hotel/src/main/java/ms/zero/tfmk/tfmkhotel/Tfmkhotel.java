package ms.zero.tfmk.tfmkhotel;

import ms.zero.tfmk.tfmkhotel.command.CommandListener;
import ms.zero.tfmk.tfmkhotel.event.EventListener;
import ms.zero.tfmk.tfmkhotel.event.LocationChecker;
import ms.zero.tfmk.tfmkhotel.event.TabCompleter;
import ms.zero.tfmk.tfmkhotel.file.FileManager;
import static ms.zero.tfmk.tfmkhotel.global.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhotel.global.Util.translate;

import ms.zero.tfmk.tfmkhotel.hotel.HotelManger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Tfmkhotel extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-Hotel 플러그인이 활성화 되었습니다."));

        plugin = this;

        FileManager.initRoomFile();

        getCommand("room").setExecutor(new CommandListener());
        getCommand("room").setTabCompleter(new TabCompleter());

        LocationChecker.startLocationChecker();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-Hotel 플러그인이 활성화 되었습니다."));
    }
    public static Boolean isPlayerAssignedRoom(UUID u) {
        return HotelManger.isPlayerAlreadyAssigned(u);
    }
}
