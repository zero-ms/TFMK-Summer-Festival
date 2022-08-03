package ms.zero.tfmk.tfmkhidenseek.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;

public class FileHandler {

    protected static String fileName;
    protected static File file;
    protected static YamlConfiguration yamlConfiguration;

    public static void checkPluginFolder() {
        File pluginFolder = new File(String.valueOf(plugin.getDataFolder()));
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
    }

    public static void createFile() {
        file = new File(plugin.getDataFolder() + File.separator + fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, true);
        }
    }
}
