package ms.zero.tfmk.tfmkhidenseek.miscellaneous;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.*;

public class Util {
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

	public static void spawnNPC() {
		Player targetPlayer = Bukkit.getPlayer("Bamboo_Photo");
		PacketContainer container = pm.createPacket(PacketType.Play.Server.PLAYER_INFO);

		WrappedGameProfile profile = new WrappedGameProfile(UUID.randomUUID(), "testNPC");
		int latency = 150;
		PlayerInfoData data = new PlayerInfoData(profile, latency, NativeGameMode.CREATIVE,
				WrappedChatComponent.fromText("testNPC"));

		container.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
		container.getPlayerInfoDataLists().write(0, Arrays.asList(data));

		try {
			pm.sendServerPacket(targetPlayer, container);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		}
	}

	public static void spawnArmorStand() {

	}

	public static void removeNameTag() {
		Player p1 = Bukkit.getPlayer("Bamboo_Photo");
		Player p2 = Bukkit.getPlayer("LukasCZzero");

		WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
		wrapper.setDisplayName(WrappedChatComponent.fromText("testTeam"));
		wrapper.setNameTagVisibility("never");
		int data = wrapper.getPackOptionData() | 1;
		wrapper.setPackOptionData(data);
		wrapper.setPlayers(Arrays.asList(p1.getUniqueId().toString(), p2.getUniqueId().toString()));
		wrapper.setMode(0);
		wrapper.broadcastPacket();

	}
}
