package ms.zero.tfmk.tfmkhidenseek.miscellaneous;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.*;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction;

import ms.zero.tfmk.tfmkhidenseek.objects.NameTagManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;

public class Util {
    private static AtomicInteger entityIDGenerator = new AtomicInteger(-1);

    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void removePotionEffects(Player p) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    public static void removeItem(Player p, ItemStack item) {
        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null && i.hasItemMeta()) {
                if (i.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    p.getInventory().remove(i);
                }
            }
        }
    }

    public static void spawnArmorStand(String text) {
        WrapperPlayServerSpawnEntity spawnEntity = new WrapperPlayServerSpawnEntity();
        spawnEntity.setEntityID(entityIDGenerator.decrementAndGet());
        spawnEntity.setType(EntityType.ARMOR_STAND);
        UUID uuid = UUID.randomUUID();
        spawnEntity.setUniqueId(uuid);
        Player p = Bukkit.getPlayer("Bamboo_Photo");
        spawnEntity.setX(p.getLocation().getX());
        spawnEntity.setY(p.getLocation().getY());
        spawnEntity.setZ(p.getLocation().getZ());
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

    public static void spawnNPC() {
        Player p = Bukkit.getPlayer("Bamboo_Photo");
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
        entitySpawn.setEntityID(entityIDGenerator.decrementAndGet());
        entitySpawn.setPlayerUUID(uuid);
        entitySpawn.setX(282.5);
        entitySpawn.setY(84);
        entitySpawn.setZ(-101.5);
        entitySpawn.setYaw(180.0f);
        entitySpawn.setPitch(0.0f);
        entitySpawn.sendPacket(p);

        WrapperPlayServerEntityHeadRotation headRotation = new WrapperPlayServerEntityHeadRotation();
        headRotation.setEntityID(entityIDGenerator.get());
        headRotation.setHeadYaw(getHeadYaw(135.0f));
        headRotation.sendPacket(p);

        WrapperPlayServerEntityMetadata metaData = new WrapperPlayServerEntityMetadata();
        metaData.setEntityID(entityIDGenerator.get());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(16, serializer, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        metaData.setMetadata(watcher.getWatchableObjects());
        metaData.sendPacket(p);

        WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
        animation.setEntityID(entityIDGenerator.get());
        animation.setAnimation(0);
        animation.sendPacket(p);

        NameTagManager.hideNameTag(p, p.getName());
    }
    private static Byte getHeadYaw(Float yaw) {
        return (byte) ((yaw + 45f) * 256.0F / 360.0F);
    }
}
