package ms.zero.tfmk.tfmkmenu.gui;

import ms.zero.tfmk.tfmkmenu.reflection.FishingChecker;
import ms.zero.tfmk.tfmkmenu.reflection.HideNSeekChecker;
import ms.zero.tfmk.tfmkmenu.warp.WarpPoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static ms.zero.tfmk.tfmkmenu.global.Util.translate;

public class GuiHandler {
    public static void openWarpGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, translate("&c워프 메뉴"));

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, GuiElements.getBlackGlassPane());
        }
        inventory.setItem(11, GuiElements.getWhiteGlassPane());
        inventory.setItem(13, GuiElements.getWhiteGlassPane());
        inventory.setItem(14, GuiElements.getWhiteGlassPane());

        inventory.setItem(10, GuiElements.getHotelGuiItem());
        inventory.setItem(12, GuiElements.getWaterParkGuiItem());
        inventory.setItem(14, GuiElements.getHarborGuiItem());
        inventory.setItem(16, GuiElements.getMenSionGuiItem());
        player.openInventory(inventory);
    }

    public static void teleportFromClickedItem(Player player, Integer clickedSlot) {
        switch (clickedSlot) {
            case 10:
                if (!FishingChecker.isPlayingFishingContest(player) && !HideNSeekChecker.isPlayingHideNSeek(player)) {
                    if (!GuiCooltime.isCoolTime(player)) {
                        player.teleport(WarpPoint.HOTEL_LOCATION);
                        player.sendMessage(translate("&c[!] &7호텔로 워프했습니다."));
                        GuiCooltime.put(player, System.currentTimeMillis() + 10000);
                    } else {
                        player.sendMessage(translate("&c[!] &7워프는 10초에 한 번씩 할 수 있습니다."));
                        player.sendMessage(translate(String.format(translate("&c[!] &7남은 쿨타임: &f%s초"), GuiCooltime.getRemainingCoolTime(player))));
                    }
                } else {
                    player.sendMessage(translate("&c[!] &7게임 도중에는 워프를 사용할 수 없습니다."));
                }
                player.closeInventory();
                break;
            case 12:
                if (!FishingChecker.isPlayingFishingContest(player) && !HideNSeekChecker.isPlayingHideNSeek(player)) {
                    if (!GuiCooltime.isCoolTime(player)) {
                        player.teleport(WarpPoint.WATERPARK_LOCATION);
                        player.sendMessage(translate("&c[!] &7워터파크 입구로 워프했습니다."));
                        GuiCooltime.put(player, System.currentTimeMillis() + 10000);
                    } else {
                        player.sendMessage(translate("&c[!] &7워프는 10초에 한 번씩 할 수 있습니다."));
                        player.sendMessage(translate(String.format(translate("&c[!] &7남은 쿨타임: &f%s초"), GuiCooltime.getRemainingCoolTime(player))));
                    }
                } else {
                    player.sendMessage(translate("&c[!] &7게임 도중에는 워프를 사용할 수 없습니다."));
                }
                player.closeInventory();
                break;
            case 14:
                if (!FishingChecker.isPlayingFishingContest(player) && !HideNSeekChecker.isPlayingHideNSeek(player)) {
                    if (!GuiCooltime.isCoolTime(player)) {
                        player.teleport(WarpPoint.HARBOR_LOCATION);
                        player.sendMessage(translate("&c[!] &7항구로 워프했습니다."));
                        GuiCooltime.put(player, System.currentTimeMillis() + 10000);
                    } else {
                        player.sendMessage(translate("&c[!] &7워프는 10초에 한 번씩 할 수 있습니다."));
                        player.sendMessage(translate(String.format(translate("&c[!] &7남은 쿨타임: &f%s초"), GuiCooltime.getRemainingCoolTime(player))));
                    }
                } else {
                    player.sendMessage(translate("&c[!] &7게임 도중에는 워프를 사용할 수 없습니다."));
                }
                player.closeInventory();
                break;
            case 16:
                if (!FishingChecker.isPlayingFishingContest(player) && !HideNSeekChecker.isPlayingHideNSeek(player)) {
                    if (!GuiCooltime.isCoolTime(player)) {
                        player.teleport(WarpPoint.MENSION_LOCATION);
                        player.sendMessage(translate("&c[!] &7멘션으로 워프했습니다."));
                        GuiCooltime.put(player, System.currentTimeMillis() + 10000);
                    } else {
                        player.sendMessage(translate("&c[!] &7워프는 10초에 한 번씩 할 수 있습니다."));
                        player.sendMessage(translate(String.format(translate("&c[!] &7남은 쿨타임: &f%s초"), GuiCooltime.getRemainingCoolTime(player))));
                    }
                } else {
                    player.sendMessage(translate("&c[!] &7게임 도중에는 워프를 사용할 수 없습니다."));
                }
                player.closeInventory();
                break;
        }
    }
}
