package ms.zero.tfmk.tfmkhidenseek.handler;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameManager;
import ms.zero.tfmk.tfmkhidenseek.gamehandler.GameScore;
import ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util;
import ms.zero.tfmk.tfmkhidenseek.objects.HologramManager;
import ms.zero.tfmk.tfmkhidenseek.objects.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.entityIDGenerator;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.GlobalVariable.world;
import static ms.zero.tfmk.tfmkhidenseek.miscellaneous.Util.translate;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player commandSender = (Player) sender;
        if (label.equalsIgnoreCase("game")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("toggle")) {

                } else if (args[0].equalsIgnoreCase("stop")) {
                    GameManager.interruptGame();
                } else if (args[0].equalsIgnoreCase("status")) {
                    commandSender.sendMessage(
                            String.format(translate("&c[DEBUG] &7drop_key: %d pick_key: %d tagger: %d runner: %d"),
                                    GameScore.getDroppedKeyScore(), GameScore.getPickedUpKeyScore(), GameScore.getTaggerCount(),
                                    GameScore.getRunnerCount()));
                } else if (args[0].equalsIgnoreCase("ar")) {
                    StringBuilder argsBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        argsBuilder.append(args[i]).append(" ");
                    }
                    argsBuilder.deleteCharAt(argsBuilder.length()-1);
                    Util.spawnArmorStand(translate(argsBuilder.toString()));
                } else if (args[0].equalsIgnoreCase("test")) {
                    Class tfmkTitlePlugin = Bukkit.getPluginManager().getPlugin("Tfmktitle").getClass();
                    try {
                        Method updateMethod = tfmkTitlePlugin.getMethod("updatePlayersTeam", null);
                        updateMethod.invoke(null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (args[0].equalsIgnoreCase("st")) {
                    NPCManager.showNPC(Arrays.asList(commandSender));
                    HologramManager.createHologram(commandSender, new Location(world, 282.5, 86, -101.5), translate("&f처음 역할: &a도망자"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 282.5, 86.35, -101.5), translate("&f최종 역할: &c술래"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 282.5, 86.7, -101.5), translate("&f획득한 키: &32&f개"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 282.5, 87.05, -101.5), translate("&c죽인 사람: &41&f명"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 84.6, -101.5), translate("&7&o(클릭하여 전환)"), true);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 85, -101.5), translate("&7#5 &f &fBamboo_Photo &8&o(1명)"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 85.35, -101.5), translate("&7#4 &f &fLukasCZZero &8&o(2명)"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 85.7, -101.5), translate("&7#3 &f &bchanchani &8&o(5명)"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 86.05, -101.5), translate("&7#2 &f &dLov_vol &8&o(10명)"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 86.4, -101.5), translate("&7#1 &f &eTFMK-Master &8&o(11명)"), false);
                    HologramManager.createHologram(commandSender, new Location(world, 285, 86.9, -101.5), translate("&7===== (&c킬 랭킹&7) ====="), false);
                } else if (args[0].equalsIgnoreCase("slime")) {

                    WrapperPlayServerSpawnEntity spawnEntity = new WrapperPlayServerSpawnEntity();
                    Integer armorID = entityIDGenerator.decrementAndGet();
                    spawnEntity.setEntityID(armorID);
                    spawnEntity.setType(EntityType.ARMOR_STAND);
                    UUID uuid = UUID.randomUUID();
                    spawnEntity.setUniqueId(uuid);
                    spawnEntity.setX(commandSender.getLocation().getX());
                    spawnEntity.setY(commandSender.getLocation().getY());
                    spawnEntity.setZ(commandSender.getLocation().getZ());
                    spawnEntity.setPitch(0.0f);
                    spawnEntity.setYaw(0.0f);
                    spawnEntity.sendPacket(commandSender);

                    WrapperPlayServerEntityMetadata entityMetaData = new WrapperPlayServerEntityMetadata();
                    WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
                    Optional<?> optionalChatComponent = Optional
                            .of(WrappedChatComponent.fromText("test hologram").getHandle());
                    dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
                    dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optionalChatComponent);
                    dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
                    dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x02 | 0x08 | 0x10));
                    entityMetaData.setMetadata(dataWatcher.getWatchableObjects());
                    entityMetaData.setEntityID(armorID);
                    entityMetaData.sendPacket(commandSender);

                    WrapperPlayServerSpawnEntityLiving spawnEntityLiving = new WrapperPlayServerSpawnEntityLiving();
                    Integer slimeID = entityIDGenerator.decrementAndGet();
                    spawnEntityLiving.setEntityID(slimeID);
                    spawnEntityLiving.setUniqueId(UUID.randomUUID());
                    spawnEntityLiving.setType(75);
                    spawnEntityLiving.setX(commandSender.getLocation().getX());
                    spawnEntityLiving.setY(commandSender.getLocation().getY());
                    spawnEntityLiving.setZ(commandSender.getLocation().getZ());
                    spawnEntityLiving.sendPacket(commandSender);

                    WrapperPlayServerEntityMetadata slimeMeatData = new WrapperPlayServerEntityMetadata();
                    WrappedDataWatcher dataWatcher2 = new WrappedDataWatcher();
                    dataWatcher2.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);

                    slimeMeatData.setMetadata(dataWatcher2.getWatchableObjects());
                    slimeMeatData.setEntityID(slimeID);
                    slimeMeatData.sendPacket(commandSender);



                    WrapperPlayServerMount mount = new WrapperPlayServerMount();
                    mount.setEntityID(armorID);
                    mount.setPassengerIds(IntStream.of(slimeID).toArray());
                    mount.sendPacket(commandSender);
                }
            }
            return true;
        } else if (label.equalsIgnoreCase("이스터에그")) {
            commandSender.sendMessage(translate("&e[개발자의 한마디] &7플러그인, 기타 외주 문의 DataCat#0001"));
            return true;
        }
        return false;
    }
}
