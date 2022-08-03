package ms.zero.tfmk.tfmkhidenseek.hologram;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.global.EntityIDGenerator;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class Hologram {

    private String hologramText;
    private Integer armorStandID;
    private Location hologramLocation;
    private Boolean clickable;
    private ClickableEntity clickableEntity;

    public Hologram(String text, Location location, Boolean clickable) {
        this.hologramText = text;
        this.armorStandID = EntityIDGenerator.generateEntityID();
        this.hologramLocation = location;
        this.clickable = clickable;
        if (clickable) {
            clickableEntity = new ClickableEntity(this.armorStandID);
        }
    }

    public String getHologramText() {
        return this.hologramText;
    }

    public Integer getHologramID() {
        return this.armorStandID;
    }

    public Location getHologramLocation() {
        return this.hologramLocation;
    }

    public Boolean getClickable() {
        return this.clickable;
    }

    public ClickableEntity getClickableEntity() {
        return this.clickableEntity;
    }

    public Integer getClickableEntityID() {
        return this.clickableEntity.getSlimeEntityID();
    }

    public Hologram setHologramText(String hologramText) {
        this.hologramText = hologramText;
        return this;
    }

    public void showHologram(Player targetPlayer) {
        sendSpawnArmorStandPacket(targetPlayer);
    }

    public void removeHologram(Player targetPlayer) {
        sendEntityDestroyPacket(targetPlayer);
    }

    private void sendEntityDestroyPacket(Player targetPlayer) {
        WrapperPlayServerEntityDestroy armorStandDestroy = new WrapperPlayServerEntityDestroy();
        armorStandDestroy.setEntityIds(Collections.singletonList(this.armorStandID).stream().mapToInt(i -> i).toArray());
        armorStandDestroy.sendPacket(targetPlayer);

        if (clickable) {
            WrapperPlayServerEntityDestroy slimeDestroy = new WrapperPlayServerEntityDestroy();
            slimeDestroy.setEntityIds(Collections.singletonList(this.clickableEntity.getSlimeEntityID()).stream().mapToInt(i -> i).toArray());
            slimeDestroy.sendPacket(targetPlayer);
        }
    }

    private void sendEntityMountPacket(Player targetPlayer) {
        WrapperPlayServerMount mount = new WrapperPlayServerMount();
        mount.setEntityID(this.armorStandID);
        mount.setPassengerIds(IntStream.of(this.clickableEntity.getSlimeEntityID()).toArray());
        mount.sendPacket(targetPlayer);
    }

    private void sendSlimePacket(Player targetPlayer) {
        WrapperPlayServerSpawnEntityLiving slimeEntityLiving = new WrapperPlayServerSpawnEntityLiving();
        Integer slimeID = this.clickableEntity.getSlimeEntityID();
        slimeEntityLiving.setEntityID(slimeID);
        slimeEntityLiving.setUniqueId(UUID.randomUUID());
        slimeEntityLiving.setType(75);
        slimeEntityLiving.setX(this.hologramLocation.getX());
        slimeEntityLiving.setY(this.hologramLocation.getY());
        slimeEntityLiving.setZ(this.hologramLocation.getZ());
        slimeEntityLiving.sendPacket(targetPlayer);

        WrapperPlayServerEntityMetadata slimeMeatData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher slimeDataWatcher = new WrappedDataWatcher();
        slimeDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)),
                (byte) 0x20);
        slimeDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Integer.class)), 1);
        slimeMeatData.setMetadata(slimeDataWatcher.getWatchableObjects());
        slimeMeatData.setEntityID(slimeID);
        slimeMeatData.sendPacket(targetPlayer);
    }

    private void sendSpawnArmorStandPacket(Player targetPlayer) {
        if (clickable) {
            sendSlimePacket(targetPlayer);
        }
        WrapperPlayServerSpawnEntity armorStandSpawnEntity = new WrapperPlayServerSpawnEntity();
        armorStandSpawnEntity.setEntityID(this.armorStandID);
        armorStandSpawnEntity.setType(EntityType.ARMOR_STAND);
        UUID uuid = UUID.randomUUID();
        armorStandSpawnEntity.setUniqueId(uuid);
        armorStandSpawnEntity.setX(this.hologramLocation.getX());
        armorStandSpawnEntity.setY(this.hologramLocation.getY());
        armorStandSpawnEntity.setZ(this.hologramLocation.getZ());
        armorStandSpawnEntity.setPitch(0.0f);
        armorStandSpawnEntity.setYaw(0.0f);
        armorStandSpawnEntity.sendPacket(targetPlayer);

        WrapperPlayServerEntityMetadata armorStandMetaData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher armorStandDataWatcher = new WrappedDataWatcher();
        Optional<?> optionalChatComponent = Optional
                .of(WrappedChatComponent.fromText(this.hologramText).getHandle());
        armorStandDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)),
                (byte) 0x20);
        armorStandDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2,
                WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optionalChatComponent);
        armorStandDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)),
                true);
        armorStandDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)),
                (byte) (0x01 | 0x08 | 0x10));
        armorStandMetaData.setMetadata(armorStandDataWatcher.getWatchableObjects());
        armorStandMetaData.setEntityID(armorStandID);
        armorStandMetaData.sendPacket(targetPlayer);

        if (clickable) {
            sendEntityMountPacket(targetPlayer);
        }
    }

    private void sendArmorStandMetaDataPacket(Player targetPlayer) {
        WrapperPlayServerEntityMetadata armorStandMetaData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher armorStandDataWatcher = new WrappedDataWatcher();
        Optional<?> optionalChatComponent = Optional
                .of(WrappedChatComponent.fromText(this.hologramText).getHandle());
        armorStandDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)),
                (byte) 0x20);
        armorStandDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2,
                WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optionalChatComponent);
        armorStandDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)),
                true);
        armorStandDataWatcher.setObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)),
                (byte) (0x01 | 0x08 | 0x10));
        armorStandMetaData.setMetadata(armorStandDataWatcher.getWatchableObjects());
        armorStandMetaData.setEntityID(this.armorStandID);
        armorStandMetaData.sendPacket(targetPlayer);
    }

    public Boolean checkClickable(Integer slimeEntityID) {
        if (clickable) {
            if (this.clickableEntity.getSlimeEntityID() == slimeEntityID) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateHologram(Player targetPlayer, Hologram changedHologram) {
        this.hologramText = changedHologram.hologramText;
        sendArmorStandMetaDataPacket(targetPlayer);
    }
}
