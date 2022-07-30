package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;

public class HologramManager {
    private static ArrayList<Integer> entityIDs = new ArrayList<>();

    public static void createHologram(Player player, Location location, String text) {
        WrapperPlayServerSpawnEntity spawnEntity = new WrapperPlayServerSpawnEntity();
        entityIDs.add(entityIDGenerator.decrementAndGet());
        spawnEntity.setEntityID(entityIDGenerator.get());
        spawnEntity.setType(EntityType.ARMOR_STAND);
        UUID uuid = UUID.randomUUID();
        spawnEntity.setUniqueId(uuid);
        spawnEntity.setX(location.getX());
        spawnEntity.setY(location.getY());
        spawnEntity.setZ(location.getZ());
        spawnEntity.setPitch(0.0f);
        spawnEntity.setYaw(0.0f);
        spawnEntity.sendPacket(player);

        WrapperPlayServerEntityMetadata entityMetaData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        Optional<?> optionalChatComponent = Optional
                .of(WrappedChatComponent.fromText(text).getHandle());
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optionalChatComponent);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));
        entityMetaData.setMetadata(dataWatcher.getWatchableObjects());
        entityMetaData.setEntityID(entityIDGenerator.get());
        entityMetaData.sendPacket(player);
    }

    public static void removeHolograms(List<Player> players) {
        for (Player player : players) {
            entityIDs.forEach(integer -> {
                WrapperPlayServerEntityDestroy entityDestroy = new WrapperPlayServerEntityDestroy();
                entityDestroy.setEntityIds(entityIDs.stream().mapToInt(i -> i).toArray());
                entityDestroy.sendPacket(player);
            });
        }
    }
}
