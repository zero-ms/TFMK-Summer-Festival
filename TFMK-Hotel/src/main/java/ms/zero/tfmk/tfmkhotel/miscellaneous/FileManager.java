package ms.zero.tfmk.tfmkhotel.miscellaneous;

import ms.zero.tfmk.tfmkhotel.Objects.HotelManger;
import ms.zero.tfmk.tfmkhotel.Objects.Room;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

import static ms.zero.tfmk.tfmkhotel.miscellaneous.Util.*;

public class FileManager {
    private static File roomFile;
    private static YamlConfiguration roomYaml;

    public static void initRoomFile() {
        File pluginFolder = new File(String.valueOf(GlobalVariable.plugin.getDataFolder()));
        if (!pluginFolder.exists()) {
            Bukkit.getConsoleSender().sendMessage(translate("&c플러그인 폴더가 존재하지 않습니다."));
            Bukkit.getConsoleSender().sendMessage(translate("&a폴더를 생성합니다."));
            pluginFolder.mkdir();
        }

        roomFile = new File(GlobalVariable.plugin.getDataFolder() + File.separator + "room.yml");
        if (!roomFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(translate("&croom.yml 파일이 존재하지 않습니다."));
            Bukkit.getConsoleSender().sendMessage(translate("&aroom.yml 파일을 생성합니다."));
            GlobalVariable.plugin.saveResource("room.yml", true); // Extract room.yml resource from jar.
        }

        roomYaml = YamlConfiguration.loadConfiguration(roomFile);
        reloadRoom();
    }

    public static void reloadRoom() {
        try {
            for (String node : roomYaml.getConfigurationSection("rooms").getKeys(false)) {
                Integer roomNumber = Integer.parseInt(node);
                String ownerUUID = String.valueOf(roomYaml.get("rooms." + node + ".ownerUUID"));
                UUID uuid = UUID.fromString(ownerUUID);
                Room room = new Room(roomNumber, uuid);

                HotelManger.put(uuid, room);
            }

            Bukkit.getConsoleSender().sendMessage(translate("&aroom.yml 리로드가 완료되었습니다."));
        } catch (Exception ex) {
            ex.printStackTrace();
            HotelManger.clearTable();
            Bukkit.getConsoleSender().sendMessage(translate("&croom.yml 파일이 손상되었거나, 데이터가 없습니다. 개발자에게 문의하십시오."));
            Bukkit.getConsoleSender().sendMessage(translate("&c주석(맨 앞에 # 붙음)을 제외하고는 아무 데이터가 없다면, 이 로그를 무시하십시오."));
        }
    }

    public static void saveRoom(UUID uuid, String name, Room room) {
        try {
            roomYaml.set("rooms." + room.getNumber() + ".ownerUUID", uuid.toString());
            roomYaml.set("rooms." + room.getNumber() + ".ownerNickName", name);
            roomYaml.save(roomFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(translate("&croom.yml 파일이 손상되었습니다. 개발자에게 문의하십시오."));
        }
    }
}
