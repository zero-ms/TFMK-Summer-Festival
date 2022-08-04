package ms.zero.tfmk.tfmkhidenseek;

import ms.zero.tfmk.tfmkhidenseek.command.CommandListener;
import ms.zero.tfmk.tfmkhidenseek.command.TabCompleter;
import ms.zero.tfmk.tfmkhidenseek.event.GameEvent;
import ms.zero.tfmk.tfmkhidenseek.event.NPCEvent;
import ms.zero.tfmk.tfmkhidenseek.file.KeyLocationFileHandler;
import ms.zero.tfmk.tfmkhidenseek.file.SettingFileHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static ms.zero.tfmk.tfmkhidenseek.global.Util.*;
import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;

public final class Tfmkhidenseek extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-HideNSeek 플러그인이 활성화 되었습니다."));

        plugin = this;

        KeyLocationFileHandler.initFile();
        SettingFileHandler.initFile();

        getCommand("game").setExecutor(new CommandListener());
        getCommand("game").setTabCompleter(new TabCompleter());
        getCommand("이스터에그").setExecutor(new CommandListener());

        getServer().getPluginManager().registerEvents(new GameEvent(), this);
        getServer().getPluginManager().registerEvents(new NPCEvent(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translate("&aTFMK-HideNSeek 플러그인이 비활성화 되었습니다."));
    }
}
