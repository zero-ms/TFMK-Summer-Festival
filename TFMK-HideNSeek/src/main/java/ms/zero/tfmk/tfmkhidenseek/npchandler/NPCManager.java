package ms.zero.tfmk.tfmkhidenseek.npchandler;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.*;
import ms.zero.tfmk.tfmkhidenseek.nmshandler.NameTagManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.plugin;

public class NPCManager {
    private static HashMap<Player, NPC> npcByPlayer = new HashMap<>();

    public static void showNPC(Player player) {
        NPC npc = new NPC(getRandomChar(8), entityIDGenerator.decrementAndGet());
        npcByPlayer.put(player, npc);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                showMirroringNPC(player);
            }
        });
    }

    public static void removeNPC(Player player) {
        removeMirroringNPC(player);
        npcByPlayer.remove(player);
    }

    private static void showMirroringNPC(Player player) {
        WrapperPlayServerPlayerInfo playerInfo = new WrapperPlayServerPlayerInfo();
        UUID uuid = UUID.randomUUID();
        String name = npcByPlayer.get(player).getNpcName();
        WrappedGameProfile gameProfile = new WrappedGameProfile(uuid, name);
        Long test1 = System.currentTimeMillis();
        String[] textureData = getSkinTextureAndSignature(player);
        gameProfile.getProperties().put("textures", new WrappedSignedProperty("textures", textureData[0], textureData[1]));
        Long test2 = System.currentTimeMillis() - test1;
        Bukkit.broadcastMessage(player.getName() + ", ping: " + test2.toString() + ", currentTimeMillis: " + System.currentTimeMillis());
        int latency = 10;
        PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, latency, EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(name));
        playerInfo.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        playerInfo.setData(Arrays.asList(playerInfoData));
        playerInfo.sendPacket(player);

        WrapperPlayServerNamedEntitySpawn namedEntitySpawn = new WrapperPlayServerNamedEntitySpawn();
        namedEntitySpawn.setEntityID(npcByPlayer.get(player).getEntityID());
        namedEntitySpawn.setPlayerUUID(uuid);
        namedEntitySpawn.setX(282.5);
        namedEntitySpawn.setY(84);
        namedEntitySpawn.setZ(-101.5);
        namedEntitySpawn.setYaw(180.0f);
        namedEntitySpawn.setPitch(0.0f);
        namedEntitySpawn.sendPacket(player);

        WrapperPlayServerEntityHeadRotation entityHeadRotation = new WrapperPlayServerEntityHeadRotation();
        entityHeadRotation.setEntityID(npcByPlayer.get(player).getEntityID());
        entityHeadRotation.setHeadYaw(getHeadYaw(135.0f));
        entityHeadRotation.sendPacket(player);

        WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata();
        entityMetadata.setEntityID(npcByPlayer.get(player).getEntityID());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(16, serializer, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        entityMetadata.setMetadata(watcher.getWatchableObjects());
        entityMetadata.sendPacket(player);

        WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
        animation.setEntityID(npcByPlayer.get(player).getEntityID());
        animation.setAnimation(0);
        animation.sendPacket(player);

        NameTagManager.hideNameTag(player, name);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                playerInfo.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                playerInfo.setData(Arrays.asList(playerInfoData));
                playerInfo.sendPacket(player);
            }
        }, 5L);

    }

    private static void removeMirroringNPC(Player player) {
        NameTagManager.showNameTag(player, npcByPlayer.get(player).getNpcName());
        WrapperPlayServerEntityDestroy entityDestroy = new WrapperPlayServerEntityDestroy();
        entityDestroy.setEntityIds(IntStream.of(npcByPlayer.get(player).getEntityID()).toArray());
        entityDestroy.sendPacket(player);

    }

    private static Byte getHeadYaw(Float yaw) {
        return (byte) ((yaw + 45f) * 256.0F / 360.0F);
    }

    private static String getRandomChar(Integer length) {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static String[] getSkinTextureAndSignature(Player player) {
        String rawJson = getPlayerInfoFromMojang(player);
        if (!rawJson.equals("error")) {
            String[] split = rawJson.split(",");
            return new String[]{split[3].replace("\"value\" : \"", "").replace("\"", "").replace(" ", ""), split[4].replace("\"signature\" : ", "").replace("\"", "").replace(" ", "").replace("}", "").replace("]", "")};
        } else {
            return new String[]{"ewogICJ0aW1lc3RhbXAiIDogMTY1OTE1MjA4Mzc1MSwKICAicHJvZmlsZUlkIiA6ICJlZjk5ZDc1MzZhMjU0YTMwYjhiMWVkZjI5Mjg1NDcyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYW1ib29fUGhvdG8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDM4ZGVhNGRhNDRhOGU4OTM0MjE1N2ZmM2Y5MWFlM2U3NDZiYTA1NWMzMGMzZDcxZTllMmUwMjdiOGUzZTI3YyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9", "L7ZsHMb5utVB5F4mgIRoC+8Gd/FlyEO3wrtqb2CaBQAtcxiIT9dpi/kYEQnakV2TXz81PBdpK0rrZ4SaLBFyOPvdXOI1If9J+YpK/pdUEsJNxRExVR/+03OjljQ+7tJD8R+qTmzj/QEzipcyp4ShuLkgrdLnNNZMA1YJf69X8PJlDgDbOE6yJDMQD3h9S645n8FyjXmrPACINsap2eM9ghIvgArK9Ec52ZXuWHoXNXfWdUmaslZttfMMPVSGyAwcqINT7Bd1LWHLVUWb2QURTW0GptE72wLlYzBA0KJEMn/TNEWIfMRCaMTRb70MBmDBXfD9EB/0p86hA21rT3gTQjYRv41zETRfA1c7wcCoh4mMxINwuN6kyVJV9GvFBYBJE069qlIYWFG5t8Y+4B1CzXmFdDszyrXHXKVlXMAvn3Xy6vIOq+G3wJFyxxWHBT8snr9ndDqKHcVIsBV27uYsQ2AXr4ij68AL4Z1KZd6keMBjZhvfTA/8N2y93p/OwF44tuPFBF4uD7bm7gvxmCDPsmi1phMK7buZhMaffsG1HOBljmJLKmw4C/eeUJ1ouXU+IHrcSacti24mpiwgitNYh7pt42JHDUL/SUq7v0DUBEDx1jqXOFlb0rfqSVhu+NluOaCZA/UVYq1lMFhvcbuDIbg9kHbf+wJV0E+rc9pHpnU="}; // Bamboo_Photo's skin
        }
    }

    private static String getPlayerInfoFromMojang(Player player) {
        try {
            String targetUrl = String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s/?unsigned=false", player.getUniqueId().toString());
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
