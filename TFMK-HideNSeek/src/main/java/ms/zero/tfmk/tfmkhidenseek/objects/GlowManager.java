package ms.zero.tfmk.tfmkhidenseek.objects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.plugin;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.pm;

public class GlowManager {
	private static ArrayList<Integer> glowedEntityIDList = new ArrayList<>();
	private static PacketAdapter glowingListener;

	static {
		glowingListener = new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_METADATA) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (GameManager.isTagger(event.getPlayer())) {
					if (glowedEntityIDList.contains(event.getPacket().getIntegers().read(0))) {
						if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
							WrappedDataWatcher watcher = new WrappedDataWatcher(
									event.getPacket().getWatchableCollectionModifier().read(0));
							WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
							watcher.setObject(0, serializer, (byte) (0x40));
							event.getPacket().getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
						}
					}
				}
			}
		};
		pm.addPacketListener(glowingListener);
	}

	public static void add(Player p) {
		glowedEntityIDList.add(p.getEntityId());
		makePlayerGlowing(p);
	}

	public static void remove(Player p) {
		glowedEntityIDList.remove(Integer.valueOf(p.getEntityId()));
		removePlayerGlowing(p);
	}

	private static void makePlayerGlowing(Player p) {
		PacketContainer container = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
		container.getIntegers().write(0, p.getEntityId());
		WrappedDataWatcher watcher = new WrappedDataWatcher();
		WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
		watcher.setEntity(p);
		watcher.setObject(0, serializer, (byte) (0x40));
		container.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

		GameManager.getTagger().forEach(player -> {
			try {
				pm.sendServerPacket(player, container);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}

	private static void removePlayerGlowing(Player p) {
		PacketContainer container = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
		container.getIntegers().write(0, p.getEntityId());
		WrappedDataWatcher watcher = new WrappedDataWatcher();
		WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
		watcher.setEntity(p);
		watcher.setObject(0, serializer, (byte) (0));
		container.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

		GameManager.getTagger().forEach(player -> {
			try {
				pm.sendServerPacket(player, container);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}
}
