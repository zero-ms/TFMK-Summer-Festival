package ms.zero.tfmk.tfmkhidenseek.file;

import org.bukkit.configuration.file.YamlConfiguration;

import ms.zero.tfmk.tfmkhidenseek.game.util.GameRule;
import ms.zero.tfmk.tfmkhidenseek.game.util.GameVariable;

public class SettingFileHandler extends FileHandler {

    public static void initFile() {
        fileName = "config.yml";
        checkPluginFolder();
        createFile();
        loadYaml();
        readSetting();
    }

    public static void loadYaml() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static void readSetting() {
        try {
            Integer minPlayers = yamlConfiguration.getInt("minPlayers");
            Integer leastTaggers = yamlConfiguration.getInt("leastTaggers");
            Integer needKey = yamlConfiguration.getInt("needKey");
            GameRule.setMinPlayers(minPlayers);
            GameRule.setLeastTaggers(leastTaggers);
            GameRule.setNeedKey(needKey);
            GameVariable.initGameItem();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
