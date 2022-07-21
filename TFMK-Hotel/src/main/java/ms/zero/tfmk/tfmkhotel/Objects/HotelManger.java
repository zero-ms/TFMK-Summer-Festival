package ms.zero.tfmk.tfmkhotel.Objects;

import ms.zero.tfmk.tfmkhotel.miscellaneous.FileManager;
import static ms.zero.tfmk.tfmkhotel.miscellaneous.GlobalVariable.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class HotelManger {
    private static HashMap<UUID, Room> roomTable = new HashMap<>();
    private static HashMap<Integer, Room> roomNumberTable = new HashMap<>();

    public static void put(UUID u, Room r) {
        roomNumberTable.put(r.getNumber(), r);
        roomTable.put(u, r);
    }

    public static Room get(UUID u) {
        return roomTable.get(u);
    }

    public static Room get(Integer i) {
        return roomNumberTable.get(i);
    }

    public static Boolean isExistRoom(Integer i) {
        return roomNumberTable.containsKey(i);
    }

    public static Boolean isPlayerAlreadyAssigned(UUID u) {
        return roomTable.containsKey(u);
    }

    public static Boolean isOwner(UUID u, Integer n) {
        if (roomNumberTable.containsKey(n)) {
            return roomNumberTable.get(n).getUUID().equals(u);
        } else {
            return false;
        }
    }

    public static Iterator<Integer> getList() {
        return roomNumberTable.keySet().iterator();
    }

    public static ReturnType assignNewRoom(Player p, Integer apart) {
        if (!isPlayerAlreadyAssigned(p.getUniqueId())) {
            Integer roomNumber = getRandomNumber(apart, 0);
            if (roomNumber == -1) {
                Bukkit.getConsoleSender().sendMessage("&4경고! 방이 부족합니다.");
                return ReturnType.ROOM_INSUFFICIENT;
            } else {
                Room r = new Room(roomNumber, p.getUniqueId());
                HotelManger.put(p.getUniqueId(), r);
                FileManager.saveRoom(p.getUniqueId(), p.getName(), r);
                return ReturnType.REQUEST_SUCCESS;
            }
        } else {
            return ReturnType.ALREADY_ASSIGNED_PLAYER;
        }
    }

    private static Integer getRandomNumber(Integer apart, Integer tryCount) {
        if (tryCount >= 140) {
            return -1;
        } else {
            int floor = (int)(Math.random() * 4 + 2);
            int room = (int)(Math.random() * 13 + 1);
            Integer roomNumber = (apart * 1000) + (floor * 100) + room;

            if (isExistRoom(roomNumber)) {
                return getRandomNumber(apart, tryCount++);
            } else {
                return roomNumber;
            }
        }
        // 1000: 1 ~ 2 // apart
        // 100: 2 ~ 6 // floor
        // 10: 01 ~ 14 // room number
        // total room = 140
    }

    public static void clearTable() {
        roomTable.clear();
        roomNumberTable.clear();
    }
}
