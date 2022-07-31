package ms.zero.tfmk.tfmkhidenseek.hologramhandler;

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
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class HologramManager {
    private static HashMap<Player, HologramWatcher> playerToHologramWatcher = new HashMap<>();
    private static HashMap<Integer, HologramWatcher> HologramWatcherBySlimeID = new HashMap<>();
    private static PacketAdapter clickListener;

    public static void initListener() {
        clickListener = new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Integer entityID = event.getPacket().getIntegers().read(0);
                if (HologramWatcherBySlimeID.containsKey(entityID)) {
                    HologramWatcher playerHologramInfo = HologramWatcherBySlimeID.get(entityID);
                    if (event.getPacket().getEntityUseActions().read(0) == EnumWrappers.EntityUseAction.INTERACT) {
                        if (event.getPacket().getHands().read(0) == EnumWrappers.Hand.MAIN_HAND) {
                            playerHologramInfo.getWatcher().sendMessage(translate("&6[!] &7전환되었습니다."));
                            chagneHologramPage(playerHologramInfo);
                        }
                    }
                }
            }
        };
        protocolManager.addPacketListener(clickListener);
    }


    public static void removeListener() {
        protocolManager.removePacketListener(clickListener);
    }

    private static void chagneHologramPage(HologramWatcher playerHologramInfo) { // 0 = Kill ranking, 1 = Key ranking
        Integer changedPage = playerHologramInfo.changePage();
        updateHologramPage(playerHologramInfo, changedPage);
    }

    private static Boolean isRankHologram(String text) {
        if (text.contains("#") || text.contains("=====")) {
            return true;
        } else {
            return false;
        }
    }

    private static void updateHologramPage(HologramWatcher playerHologramInfo, Integer page) {
        int index = 0;
        if (page == 0) {
            for (Integer armorStandID : playerHologramInfo.getRankArmorStands()) {
                updateArmorStandMetaData(playerHologramInfo.getWatcher(), armorStandID, HologramRankingPreset.getKillRanking(index).getHologramText());
                index += 1;
            }
        } else {
            for (Integer armorStandID : playerHologramInfo.getRankArmorStands()) {
                updateArmorStandMetaData(playerHologramInfo.getWatcher(), armorStandID, HologramRankingPreset.getKeyRanking(index).getHologramText());
                index += 1;
            }
        }
    }

    private static HologramWatcher getPlayerHologram(Player player) {
        if (playerToHologramWatcher.containsKey(player)) {
            return playerToHologramWatcher.get(player);
        } else {
            return new HologramWatcher(player);
        }
    }

    private static void updateArmorStandMetaData(Player player, Integer armorStandID, String text) {
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
    }

    private static Integer spawnArmorStand(Player player, Location location, String text) {
        WrapperPlayServerSpawnEntity armorStandSpawnEntity = new WrapperPlayServerSpawnEntity();
        Integer armorStandID = entityIDGenerator.decrementAndGet();
        HologramWatcher playersHologramInfo = getPlayerHologram(player);
        playersHologramInfo.addArmorStandID(armorStandID, isRankHologram(text));

        playerToHologramWatcher.put(player, playersHologramInfo);
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
        HologramWatcher playerHologramInfo = getPlayerHologram(player);
        HologramWatcherBySlimeID.put(slimeID, playerHologramInfo);
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


    public static void removeHolograms(Player player) {
        HologramWatcher hologramWatcher = getPlayerHologram(player);
        WrapperPlayServerEntityDestroy armorStandDestroy = new WrapperPlayServerEntityDestroy();
        armorStandDestroy.setEntityIds(hologramWatcher.getAllArmorStands().stream().mapToInt(i -> i).toArray());
        armorStandDestroy.sendPacket(player);

        WrapperPlayServerEntityDestroy slimeDestroy = new WrapperPlayServerEntityDestroy();
        slimeDestroy.setEntityIds(HologramWatcherBySlimeID.keySet().stream().mapToInt(i -> i).toArray());
        slimeDestroy.sendPacket(player);
    }

    public static void clearWatchers() {
        playerToHologramWatcher.clear();
        HologramWatcherBySlimeID.clear();
    }

    public static HologramWatcher getHologramWatcher(Player player) {
        return playerToHologramWatcher.get(player);
    }
}
