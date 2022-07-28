package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;

public class HologramManager {
    private static ArrayList<Integer> entityIDList = new ArrayList<>();

    private static void createHologram(Player p, Location l, String text) {
        WrapperPlayServerSpawnEntity spawnEntity = new WrapperPlayServerSpawnEntity();
        entityIDList.add(entityIDGenerator.decrementAndGet());
        spawnEntity.setEntityID(entityIDGenerator.get());
        spawnEntity.setType(EntityType.ARMOR_STAND);
        UUID uuid = UUID.randomUUID();
        spawnEntity.setUniqueId(uuid);
        spawnEntity.setX(l.getX());
        spawnEntity.setY(l.getY());
        spawnEntity.setZ(l.getZ());
        spawnEntity.setPitch(0.0f);
        spawnEntity.setYaw(0.0f);
        spawnEntity.sendPacket(p);

        WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        Optional<?> opt = Optional
                .of(WrappedChatComponent.fromText(text).getHandle());
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));
        metadata.setMetadata(watcher.getWatchableObjects());
        metadata.setEntityID(entityIDGenerator.get());
        metadata.sendPacket(p);
    }

    private static void removeHolograms(List<Player> playerList) {
        for (Player p : playerList) {
            entityIDList.forEach(integer -> {
                WrapperPlayServerEntityDestroy destroyEntity = new WrapperPlayServerEntityDestroy();
                destroyEntity.setEntityIds(entityIDList.stream().mapToInt(i -> i).toArray());
                destroyEntity.sendPacket(p);
            });
        }
    }
}
