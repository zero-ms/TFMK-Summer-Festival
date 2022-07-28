package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;

public class NPCManager {
    private static HashMap<Player, Integer> entityIDMap = new HashMap<>();
    private static ArrayList<Player> playerList;

    public static void showNPC(List<Player> list) {
        playerList = new ArrayList<>(list);
        list.forEach(player -> entityIDMap.put(player, entityIDGenerator.decrementAndGet()));
        showMirroringNPC();
    }

    public static void removeNPC() {
        removeMirroringNPC();
        entityIDMap.clear();
        playerList.clear();
    }

    private static void showMirroringNPC() {
        for (Player p : playerList) {
            WrapperPlayServerPlayerInfo playerInfo = new WrapperPlayServerPlayerInfo();
            UUID uuid = p.getUniqueId();
            WrappedGameProfile profile = new WrappedGameProfile(uuid, p.getName());
            int latency = 10;
            PlayerInfoData data = new PlayerInfoData(profile, latency, EnumWrappers.NativeGameMode.CREATIVE,
                    WrappedChatComponent.fromText(p.getName()));
            playerInfo.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
            playerInfo.setData(Arrays.asList(data));
            playerInfo.sendPacket(p);

            WrapperPlayServerNamedEntitySpawn entitySpawn = new WrapperPlayServerNamedEntitySpawn();
            entitySpawn.setEntityID(entityIDMap.get(p));
            entitySpawn.setPlayerUUID(uuid);
            entitySpawn.setX(282.5);
            entitySpawn.setY(84);
            entitySpawn.setZ(-101.5);
            entitySpawn.setYaw(180.0f);
            entitySpawn.setPitch(0.0f);
            entitySpawn.sendPacket(p);

            WrapperPlayServerEntityHeadRotation headRotation = new WrapperPlayServerEntityHeadRotation();
            headRotation.setEntityID(entityIDMap.get(p));
            headRotation.setHeadYaw(getHeadYaw(135.0f));
            headRotation.sendPacket(p);

            WrapperPlayServerEntityMetadata metaData = new WrapperPlayServerEntityMetadata();
            metaData.setEntityID(entityIDMap.get(p));
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
            watcher.setObject(16, serializer, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
            metaData.setMetadata(watcher.getWatchableObjects());
            metaData.sendPacket(p);

            WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
            animation.setEntityID(entityIDMap.get(p));
            animation.setAnimation(0);
            animation.sendPacket(p);

            NameTagManager.hideNameTag(p, p.getName());
        }
    }

    private static void removeMirroringNPC() {
        for (Player p : playerList) {
            NameTagManager.showNameTag(p, p.getName());
            WrapperPlayServerEntityDestroy destroyEntity = new WrapperPlayServerEntityDestroy();
            destroyEntity.setEntityIds(IntStream.of(entityIDMap.get(p)).toArray());
            destroyEntity.sendPacket(p);
        }
    }

    private static Byte getHeadYaw(Float yaw) {
        return (byte) ((yaw + 45f) * 256.0F / 360.0F);
    }
}
