package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;

public class NPCManager {
    private static HashMap<Player, Integer> playerToEntityID = new HashMap<>();
    private static ArrayList<Player> players;

    public static void showNPC(List<Player> players) {
        NPCManager.players = new ArrayList<>(players);
        players.forEach(player -> playerToEntityID.put(player, entityIDGenerator.decrementAndGet()));
        showMirroringNPC();
    }

    public static void removeNPC() {
        removeMirroringNPC();
        playerToEntityID.clear();
        players.clear();
    }

    private static void showMirroringNPC() {
        for (Player player : players) {
            WrapperPlayServerPlayerInfo playerInfo = new WrapperPlayServerPlayerInfo();
            UUID uuid = player.getUniqueId();
            WrappedGameProfile gameProfile = new WrappedGameProfile(uuid, player.getName());
            int latency = 10;
            PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, latency, EnumWrappers.NativeGameMode.CREATIVE,
                    WrappedChatComponent.fromText(player.getName()));
            playerInfo.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
            playerInfo.setData(Arrays.asList(playerInfoData));
            playerInfo.sendPacket(player);

            WrapperPlayServerNamedEntitySpawn namedEntitySpawn = new WrapperPlayServerNamedEntitySpawn();
            namedEntitySpawn.setEntityID(playerToEntityID.get(player));
            namedEntitySpawn.setPlayerUUID(uuid);
            namedEntitySpawn.setX(282.5);
            namedEntitySpawn.setY(84);
            namedEntitySpawn.setZ(-101.5);
            namedEntitySpawn.setYaw(180.0f);
            namedEntitySpawn.setPitch(0.0f);
            namedEntitySpawn.sendPacket(player);

            WrapperPlayServerEntityHeadRotation entityHeadRotation = new WrapperPlayServerEntityHeadRotation();
            entityHeadRotation.setEntityID(playerToEntityID.get(player));
            entityHeadRotation.setHeadYaw(getHeadYaw(135.0f));
            entityHeadRotation.sendPacket(player);

            WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata();
            entityMetadata.setEntityID(playerToEntityID.get(player));
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
            watcher.setObject(16, serializer, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
            entityMetadata.setMetadata(watcher.getWatchableObjects());
            entityMetadata.sendPacket(player);

            WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
            animation.setEntityID(playerToEntityID.get(player));
            animation.setAnimation(0);
            animation.sendPacket(player);

            NameTagManager.hideNameTag(player, player.getName());
        }
    }

    private static void removeMirroringNPC() {
        for (Player player : players) {
            NameTagManager.showNameTag(player, player.getName());
            WrapperPlayServerEntityDestroy entityDestroy = new WrapperPlayServerEntityDestroy();
            entityDestroy.setEntityIds(IntStream.of(playerToEntityID.get(player)).toArray());
            entityDestroy.sendPacket(player);
        }
    }

    private static Byte getHeadYaw(Float yaw) {
        return (byte) ((yaw + 45f) * 256.0F / 360.0F);
    }
}
