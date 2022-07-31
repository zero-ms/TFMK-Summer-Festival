package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;

public class HologramManager {
    private static HashMap<Player, Hologram> playerToArmorStandID = new HashMap<>();
    private static HashMap<Integer, Hologram> playerBySlimeID = new HashMap<>();
    private static PacketAdapter clickListener;
    static {
        clickListener = new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Integer entityID = event.getPacket().getIntegers().read(0);
                if (playerBySlimeID.containsKey(entityID)) {
                    Hologram playerHologramInfo = playerBySlimeID.get(entityID);
                    if (event.getPacket().getEntityUseActions().read(0) == EnumWrappers.EntityUseAction.INTERACT) {
                        if (event.getPacket().getHands().read(0) == EnumWrappers.Hand.MAIN_HAND) {
                            playerHologramInfo.getPlayer().sendMessage("You changed!");
                        }
                    }
                }
            }
        };
        protocolManager.addPacketListener(clickListener);
    }

    private static void chagneHologramPage(Integer page) { // 0 = Kill ranking, 1 = Key ranking

    }

    private static Hologram getPlayerHologram(Player player) {
        if (playerToArmorStandID.containsKey(player)) {
            return playerToArmorStandID.get(player);
        } else {
            return new Hologram(player);
        }
    }

    private static Integer spawnArmorStand(Player player, Location location, String text) {
        WrapperPlayServerSpawnEntity armorStandSpawnEntity = new WrapperPlayServerSpawnEntity();
        Integer armorStandID = entityIDGenerator.decrementAndGet();
        Hologram playersHologramInfo = getPlayerHologram(player);
        playersHologramInfo.addArmorStandID(armorStandID);
        playerToArmorStandID.put(player, playersHologramInfo);
        armorStandSpawnEntity.setEntityID(armorStandID);
        armorStandSpawnEntity.setType(EntityType.ARMOR_STAND);
        UUID uuid = UUID.randomUUID();
        armorStandSpawnEntity.setUniqueId(uuid);
        armorStandSpawnEntity.setX(location.getX());
        armorStandSpawnEntity.setY(location.getY());
        armorStandSpawnEntity.setZ(location.getZ());
        armorStandSpawnEntity.setPitch(0.0f);
        armorStandSpawnEntity.setYaw(0.0f);
        armorStandSpawnEntity.sendPacket(player);

        WrapperPlayServerEntityMetadata armorStandMetaData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher armorStandDataWatcher = new WrappedDataWatcher();
        Optional<?> optionalChatComponent = Optional
                .of(WrappedChatComponent.fromText(text).getHandle());
        armorStandDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        armorStandDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optionalChatComponent);
        armorStandDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        armorStandDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));
        armorStandMetaData.setMetadata(armorStandDataWatcher.getWatchableObjects());
        armorStandMetaData.setEntityID(armorStandID);
        armorStandMetaData.sendPacket(player);

        return armorStandID;
    }

    private static Integer spawnSlime(Player player, Location location) {
        WrapperPlayServerSpawnEntityLiving slimeEntityLiving = new WrapperPlayServerSpawnEntityLiving();
        Integer slimeID = entityIDGenerator.decrementAndGet();
        Hologram playerHologramInfo = getPlayerHologram(player);
        playerBySlimeID.put(slimeID, playerHologramInfo);
        slimeEntityLiving.setEntityID(slimeID);
        slimeEntityLiving.setUniqueId(UUID.randomUUID());
        slimeEntityLiving.setType(75);
        slimeEntityLiving.setX(location.getX());
        slimeEntityLiving.setY(location.getY());
        slimeEntityLiving.setZ(location.getZ());
        slimeEntityLiving.sendPacket(player);

        WrapperPlayServerEntityMetadata slimeMeatData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher slimeDataWatcher = new WrappedDataWatcher();
        slimeDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        slimeDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Integer.class)), 1);
        slimeMeatData.setMetadata(slimeDataWatcher.getWatchableObjects());
        slimeMeatData.setEntityID(slimeID);
        slimeMeatData.sendPacket(player);

        return slimeID;
    }

    private static void makeEntityMount(Player player, Integer vehicle, Integer passenger) {
        WrapperPlayServerMount mount = new WrapperPlayServerMount();
        mount.setEntityID(vehicle);
        mount.setPassengerIds(IntStream.of(passenger).toArray());
        mount.sendPacket(player);
    }
    public static void createHologram(Player player, Location location, String text, Boolean clickable) {
        if (clickable) {
            Integer slimeID = spawnSlime(player, location);
            Integer armorStandID = spawnArmorStand(player, location, text);
            makeEntityMount(player, armorStandID, slimeID);
        } else {
            spawnArmorStand(player, location, text);
        }
    }

    public static void removeHolograms(List<Player> players) {
        for (Player player : players) {

        }
    }
}
