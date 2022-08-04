package ms.zero.tfmk.tfmkhidenseek.npc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.IntStream;

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityHeadRotation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import ms.zero.tfmk.tfmkhidenseek.global.EntityIDGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ms.zero.tfmk.tfmkhidenseek.nms.NameTagManager;

import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.*;

public class NPC {
    private String npcName;
    private Integer npcEntityID;
    private String textures;
    private String signature;
    private Location npcLocation;

    public NPC() {
        this.npcEntityID = EntityIDGenerator.generateEntityID();
    }

    public void spawnNPC(Player npcWatcher) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> sendSpawnNPCPacket(npcWatcher));
    }

    private void sendSpawnNPCPacket(Player player) {
        WrapperPlayServerPlayerInfo playerInfo = new WrapperPlayServerPlayerInfo();
        UUID uuid = UUID.randomUUID();
        setSkinFromPlayer(player);
        WrappedGameProfile gameProfile = new WrappedGameProfile(uuid, npcName);
        gameProfile.getProperties().put("textures",
                new WrappedSignedProperty("textures", textures, signature));
        int latency = 10;
        PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, latency,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(npcName));
        playerInfo.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        playerInfo.setData(Collections.singletonList(playerInfoData));
        playerInfo.sendPacket(player);

        WrapperPlayServerNamedEntitySpawn namedEntitySpawn = new WrapperPlayServerNamedEntitySpawn();
        namedEntitySpawn.setEntityID(npcEntityID);
        namedEntitySpawn.setPlayerUUID(uuid);
        namedEntitySpawn.setX(npcLocation.getX());
        namedEntitySpawn.setY(npcLocation.getY());
        namedEntitySpawn.setZ(npcLocation.getZ());
        namedEntitySpawn.setYaw(npcLocation.getYaw());
        namedEntitySpawn.setPitch(npcLocation.getPitch());
        namedEntitySpawn.sendPacket(player);

        WrapperPlayServerEntityHeadRotation entityHeadRotation = new WrapperPlayServerEntityHeadRotation();
        entityHeadRotation.setEntityID(npcEntityID);
        entityHeadRotation.setHeadYaw(getHeadYaw(135.0f));
        entityHeadRotation.sendPacket(player);

        WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata();
        entityMetadata.setEntityID(npcEntityID);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(16, serializer, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        entityMetadata.setMetadata(watcher.getWatchableObjects());
        entityMetadata.sendPacket(player);

        WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
        animation.setEntityID(npcEntityID);
        animation.setAnimation(0);
        animation.sendPacket(player);

        NameTagManager.hideNameTag(player, npcName);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            playerInfo.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
            playerInfo.setData(Collections.singletonList(playerInfoData));
            playerInfo.sendPacket(player);
        }, 5L);
    }

    public void removeNPC(Player npcWatcher) {
        NameTagManager.showNameTag(npcWatcher, npcName);
        WrapperPlayServerEntityDestroy entityDestroy = new WrapperPlayServerEntityDestroy();
        entityDestroy.setEntityIds(IntStream.of(npcEntityID).toArray());
        entityDestroy.sendPacket(npcWatcher);
    }

    public NPC setNPCName(String name) {
        this.npcName = name;
        return this;
    }

    public NPC setLocation(Location location) {
        this.npcLocation = location;
        return this;
    }

    private NPC setTextures(String textures) {
        this.textures = textures;
        return this;
    }

    private NPC setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public NPC setSkinFromPlayer(Player player) {
        String[] textures = getSkinTextureAndSignature(player);
        return this.setTextures(textures[0]).setSignature(textures[1]);
    }

    public String getNPCName() {
        return this.npcName;
    }

    public Integer getNPCID() {
        return this.npcEntityID;
    }

    public Location getLocation() {
        return this.npcLocation;
    }

    private Byte getHeadYaw(Float yaw) {
        return (byte) ((yaw + 45f) * 256.0F / 360.0F);
    }

    private String[] getSkinTextureAndSignature(Player player) {
        String rawJson = getPlayerInfoFromMojang(player);
        if (!rawJson.equals("error")) {
            String[] split = rawJson.split(",");
            return new String[]{
                    split[3]
                            .replace("\"value\" : \"", "")
                            .replace("\"", "")
                            .replace(" ", ""),
                    split[4]
                            .replace("\"signature\" : ", "")
                            .replace("\"", "")
                            .replace(" ", "")
                            .replace("}", "")
                            .replace("]", "")};
        } else {
            return new String[]{"ewogICJ0aW1lc3RhbXAiIDogMTY1OTE1MjA4Mzc1MSwKICAicHJvZmlsZUlkIiA6ICJlZjk5ZDc1MzZhMjU0YTMwYjhiMWVkZjI5Mjg1NDcyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYW1ib29fUGhvdG8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDM4ZGVhNGRhNDRhOGU4OTM0MjE1N2ZmM2Y5MWFlM2U3NDZiYTA1NWMzMGMzZDcxZTllMmUwMjdiOGUzZTI3YyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9", "L7ZsHMb5utVB5F4mgIRoC+8Gd/FlyEO3wrtqb2CaBQAtcxiIT9dpi/kYEQnakV2TXz81PBdpK0rrZ4SaLBFyOPvdXOI1If9J+YpK/pdUEsJNxRExVR/+03OjljQ+7tJD8R+qTmzj/QEzipcyp4ShuLkgrdLnNNZMA1YJf69X8PJlDgDbOE6yJDMQD3h9S645n8FyjXmrPACINsap2eM9ghIvgArK9Ec52ZXuWHoXNXfWdUmaslZttfMMPVSGyAwcqINT7Bd1LWHLVUWb2QURTW0GptE72wLlYzBA0KJEMn/TNEWIfMRCaMTRb70MBmDBXfD9EB/0p86hA21rT3gTQjYRv41zETRfA1c7wcCoh4mMxINwuN6kyVJV9GvFBYBJE069qlIYWFG5t8Y+4B1CzXmFdDszyrXHXKVlXMAvn3Xy6vIOq+G3wJFyxxWHBT8snr9ndDqKHcVIsBV27uYsQ2AXr4ij68AL4Z1KZd6keMBjZhvfTA/8N2y93p/OwF44tuPFBF4uD7bm7gvxmCDPsmi1phMK7buZhMaffsG1HOBljmJLKmw4C/eeUJ1ouXU+IHrcSacti24mpiwgitNYh7pt42JHDUL/SUq7v0DUBEDx1jqXOFlb0rfqSVhu+NluOaCZA/UVYq1lMFhvcbuDIbg9kHbf+wJV0E+rc9pHpnU="}; // Bamboo_Photo's skin
        }
    }

    private static String getPlayerInfoFromMojang(Player player) {
        try {
            String targetUrl = String.format(
                    "https://sessionserver.mojang.com/session/minecraft/profile/%s/?unsigned=false",
                    player.getUniqueId());
            URL url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // optional default is GET
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 200) {
                return response.toString();
            } else {
                return "error";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "error";
    }
}
