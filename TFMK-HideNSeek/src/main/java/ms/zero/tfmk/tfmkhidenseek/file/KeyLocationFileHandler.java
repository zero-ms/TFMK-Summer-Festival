package ms.zero.tfmk.tfmkhidenseek.file;

import java.io.BufferedReader;
import java.io.FileReader;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import ms.zero.tfmk.tfmkhidenseek.game.objects.KeyDropper;

import static ms.zero.tfmk.tfmkhidenseek.global.Util.*;
import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;

public class KeyLocationFileHandler extends FileHandler {

    public static void initFile() {
        fileName = "drop_list.dat";
        checkPluginFolder();
        try {
            createFile();
            getKeyLocation();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Location parseLocationFromString(String context) {
        String[] split = context.split(",");
        Location l = new Location(world,
                Integer.parseInt(split[0].replace("Location(", "")),
                Integer.parseInt(split[1].replace(" ", "")),
                Integer.parseInt(split[2].replace(" ", "").replace(")", "")));
        return l;
    }

    public static void getKeyLocation() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String context;
            while ((context = reader.readLine()) != null) {
                KeyDropper.addGenerationPoint(parseLocationFromString(context));
            }
            reader.close();
            Bukkit.getConsoleSender()
                    .sendMessage(String.format(translate("&adrop_list.dat 으로부터 %d개의 위치를 읽어왔습니다."),
                            KeyDropper.getRegisteredPointSize()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
