package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.protocolManager;

public class GlowManager {
    private static ArrayList<Integer> glowedEntityIDs = new ArrayList<>();
    private static PacketAdapter glowingListener;

    static {
        glowingListener = new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent packetEvent) {
                if (GameManager.isTagger(packetEvent.getPlayer())) {
                    if (glowedEntityIDs.contains(packetEvent.getPacket().getIntegers().read(0))) {
                        if (packetEvent.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
                            WrappedDataWatcher dataWatcher = new WrappedDataWatcher(
                                    packetEvent.getPacket().getWatchableCollectionModifier().read(0));
                            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
                            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, serializer), (byte) (0x40));
                            packetEvent.getPacket().getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
                        }
                    }
                }
            }
        };
        protocolManager.addPacketListener(glowingListener);
    }

    public static void add(Player player) {
        glowedEntityIDs.add(player.getEntityId());
        makePlayerGlowing(player);
    }

    public static void remove(Player player) {
        glowedEntityIDs.remove(Integer.valueOf(player.getEntityId()));
        removePlayerGlowing(player);
    }

    private static void makePlayerGlowing(Player player) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, player.getEntityId());
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        dataWatcher.setEntity(player);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, serializer), (byte) (0x40));
        packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        GameManager.getTagger().forEach(p -> {
            try {
                protocolManager.sendServerPacket(p, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private static void removePlayerGlowing(Player player) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, player.getEntityId());
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        dataWatcher.setEntity(player);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, serializer), (byte) (0));
        packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        GameManager.getTagger().forEach(p -> {
            try {
                protocolManager.sendServerPacket(p, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
