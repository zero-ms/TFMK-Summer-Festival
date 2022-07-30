package ms.zero.tfmk.tfmkhidenseek.miscellaneous;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;

public class Util {
    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void removePotionEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static void removeItem(Player player, ItemStack item) {
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && i.hasItemMeta()) {
                if (i.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                    player.getInventory().remove(i);
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
        Player player = Bukkit.getPlayer("Bamboo_Photo");
        spawnEntity.setX(player.getLocation().getX());
        spawnEntity.setY(player.getLocation().getY());
        spawnEntity.setZ(player.getLocation().getZ());
        spawnEntity.setPitch(0.0f);
        spawnEntity.setYaw(0.0f);
        spawnEntity.sendPacket(player);

        WrapperPlayServerEntityMetadata metaData = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        Optional<?> chatComponent = Optional
                .of(WrappedChatComponent.fromText(text).getHandle());
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), chatComponent);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));
        metaData.setMetadata(dataWatcher.getWatchableObjects());
        metaData.setEntityID(entityIDGenerator.get());
        metaData.sendPacket(player);
    }
    /*
    public static void test(String name) {
        Player p = Bukkit.getPlayer("Bamboo_Photo");

        WrapperPlayServerPlayerInfo playerInfo = new WrapperPlayServerPlayerInfo();
        UUID uuid = p.getUniqueId();
        WrappedGameProfile profile = new WrappedGameProfile(uuid, p.getName());
        int latency = 10;
        PlayerInfoData data = new PlayerInfoData(profile, latency, EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(p.getName()));
        playerInfo.setAction(PlayerInfoAction.REMOVE_PLAYER);
        playerInfo.setData(Arrays.asList(data));
        playerInfo.broadcastPacket();

        String tempUUID = sendGet(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, System.currentTimeMillis()));
        if (tempUUID.length() == 36) {
            uuid = UUID.fromString(tempUUID);
        }
        playerInfo = new WrapperPlayServerPlayerInfo();
        profile = new WrappedGameProfile(uuid, name);
        data = new PlayerInfoData(profile, latency, EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(name));
        playerInfo.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        playerInfo.setData(Arrays.asList(data));
        playerInfo.broadcastPacket();

        WrapperPlayServerEntityDestroy destroyEntity = new WrapperPlayServerEntityDestroy();
        destroyEntity.setEntityIds(IntStream.of(p.getEntityId()).toArray());
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getEntityId() != p.getEntityId()).forEach(destroyEntity::sendPacket);

        WrapperPlayServerNamedEntitySpawn entitySpawn = new WrapperPlayServerNamedEntitySpawn();
        entitySpawn.setEntityID(p.getEntityId());
        entitySpawn.setPlayerUUID(uuid);
        entitySpawn.setX(p.getLocation().getX());
        entitySpawn.setY(p.getLocation().getY());
        entitySpawn.setZ(p.getLocation().getZ());
        entitySpawn.setYaw(p.getLocation().getYaw());
        entitySpawn.setPitch(p.getLocation().getPitch());
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getEntityId() != p.getEntityId()).forEach(entitySpawn::sendPacket);

        WrapperPlayServerEntityHeadRotation headRotation = new WrapperPlayServerEntityHeadRotation();
        headRotation.setEntityID(p.getEntityId());
        headRotation.setHeadYaw(getHeadYaw(p.getLocation().getYaw()));
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getEntityId() != p.getEntityId()).forEach(headRotation::sendPacket);

        WrapperPlayServerEntityMetadata metaData = new WrapperPlayServerEntityMetadata();
        metaData.setEntityID(p.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(16, serializer, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        metaData.setMetadata(watcher.getWatchableObjects());
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getEntityId() != p.getEntityId()).forEach(metaData::sendPacket);

        WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
        animation.setEntityID(p.getEntityId());
        animation.setAnimation(0);
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getEntityId() != p.getEntityId()).forEach(animation::sendPacket);
    }
    public static String sendGet(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // optional default is GET
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return parseJson(response.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    public static String parseJson(String context) {
        String[] split = context.split(":");
        String uuid = split[2].replace("\"", "").replace("}", "");
        StringBuilder sb = new StringBuilder();
        sb.append(uuid);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        System.out.println(sb.toString());
        if (sb.toString().length() == 36) {
            return sb.toString();
        } else {
            return "error";
        }
    }
    */
}
