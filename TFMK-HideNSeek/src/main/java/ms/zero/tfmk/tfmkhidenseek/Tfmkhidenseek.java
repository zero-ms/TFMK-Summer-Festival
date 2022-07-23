package ms.zero.tfmk.tfmkhidenseek;

import ms.zero.tfmk.tfmkhidenseek.handler.CommandListener;
import ms.zero.tfmk.tfmkhidenseek.handler.EventListener;
import ms.zero.tfmk.tfmkhidenseek.handler.FileHandler;
import ms.zero.tfmk.tfmkhidenseek.handler.TabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.*;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;

public final class Tfmkhidenseek extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-HideNSeek 플러그인이 활성화 되었습니다."));

        plugin = this;

        FileHandler.initSettingFile();

        getCommand("game").setExecutor(new CommandListener());
        getCommand("game").setTabCompleter(new TabCompleter());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-HideNSeek 플러그인이 비활성화 되었습니다."));
    }
}
