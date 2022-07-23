package ms.zero.tfmk.tfmkhidenseek.handler;

import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.*;

public class FileHandler {
    private static File settingFile;
    private static YamlConfiguration settingYaml;

    public static void initSettingFile() {
        File pluginFolder = new File(String.valueOf(plugin.getDataFolder()));
        if (!pluginFolder.exists()) {
            Bukkit.getConsoleSender().sendMessage(translate("&c플러그인 폴더가 존재하지 않습니다."));
            Bukkit.getConsoleSender().sendMessage(translate("&a폴더를 생성합니다."));
            pluginFolder.mkdir();
        }

        settingFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!settingFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(translate("&cconfig.yml 파일이 존재하지 않습니다."));
            Bukkit.getConsoleSender().sendMessage(translate("&aconfig.yml 파일을 생성합니다."));
            plugin.saveResource("config.yml", true);
        }

        settingYaml = YamlConfiguration.loadConfiguration(settingFile);

        reloadSetting();
    }

    public static void reloadSetting() {
        try {
            Integer minPlayers = settingYaml.getInt("minPlayers");
            Integer leastTaggers = settingYaml.getInt("leastTaggers");
            GameRule.setMinPlayers(minPlayers);
            GameRule.setLeastTaggers(leastTaggers);
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(translate("&cconfig.yml 파일이 손상되었습니다. 개발자에게 문의하시시오."));
        }
    }
}