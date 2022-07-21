package ms.zero.tfmk.tfmkhotel;

import ms.zero.tfmk.tfmkhotel.handler.CommandListener;
import ms.zero.tfmk.tfmkhotel.handler.EventListener;
import ms.zero.tfmk.tfmkhotel.handler.TabCompleter;
import ms.zero.tfmk.tfmkhotel.miscellaneous.FileManager;
import static ms.zero.tfmk.tfmkhotel.miscellaneous.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhotel.miscellaneous.Util.translate;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tfmkhotel extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-Hotel 플러그인이 활성화 되었습니다."));

        plugin = this;

        FileManager.initRoomFile();

        getCommand("room").setExecutor(new CommandListener());
        getCommand("room").setTabCompleter(new TabCompleter());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-Hotel 플러그인이 활성화 되었습니다."));
    }
}
