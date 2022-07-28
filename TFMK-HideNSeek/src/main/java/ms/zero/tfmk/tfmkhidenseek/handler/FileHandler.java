package ms.zero.tfmk.tfmkhidenseek.handler;

import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameRule;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.KeyDropper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.*;

public class FileHandler {
    private static File settingFile;
    private static YamlConfiguration settingYaml;
    private static File keyDropListFile;

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
            Integer needKey = settingYaml.getInt("needKey: 5");
            GameRule.setMinPlayers(minPlayers);
            GameRule.setLeastTaggers(leastTaggers);
            GameRule.setNeedKey(needKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(translate("&cconfig.yml 파일이 손상되었습니다. 개발자에게 문의하시시오."));
        }
    }

    public static void initKeyDropListFile() {
        File pluginFolder = new File(String.valueOf(plugin.getDataFolder()));
        if (!pluginFolder.exists()) {
            Bukkit.getConsoleSender().sendMessage(translate("&c플러그인 폴더가 존재하지 않습니다."));
            Bukkit.getConsoleSender().sendMessage(translate("&a폴더를 생성합니다."));
            pluginFolder.mkdir();
        }

        keyDropListFile = new File(plugin.getDataFolder() + File.separator + "drop_list.dat");
        if (!keyDropListFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(translate("&cdrop_list.dat 파일이 존재하지 않습니다."));
            Bukkit.getConsoleSender().sendMessage(translate("&adrop_list.dat 파일을 생성합니다."));
            plugin.saveResource("drop_list.dat", true);
        }

        reloadKeyDropList();
    }

    private static Location parseLocationFromString(String context) {
        String[] split = context.split(",");
        Location l = new Location(world, Integer.parseInt(split[0].replace("Location(", "")),
                Integer.parseInt(split[1].replace(" ", "")),
                Integer.parseInt(split[2].replace(" ", "").replace(")", "")));
        return l;
    }

    public static void reloadKeyDropList() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(keyDropListFile));
            String context;
            while ((context = reader.readLine()) != null) {
                KeyDropper.put(parseLocationFromString(context));
            }
            reader.close();
            Bukkit.getConsoleSender()
                    .sendMessage(String.format(translate("&adrop_list.dat 으로부터 %d개의 위치를 읽어왔습니다."), KeyDropper.size()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
