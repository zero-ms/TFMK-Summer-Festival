package ms.zero.tfmk.tfmkmenu.gui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static ms.zero.tfmk.tfmkmenu.global.Util.translate;

public class GuiElements {
    private static ItemStack hotelGuiItem;
    private static ItemStack waterParkGuiItem;
    private static ItemStack harborGuiItem;
    private static ItemStack menSionGuiItem;
    private static ItemStack blackGlassPane;
    private static ItemStack whiteGlassPane;

    static {
        hotelGuiItem = new ItemStack(Material.RED_BED);
        ItemMeta hotelGuiItemMeta = hotelGuiItem.getItemMeta();
        hotelGuiItemMeta.setDisplayName(translate("&c호텔로 이동"));
        hotelGuiItemMeta.setLore(Arrays.asList(translate("&7휴식을 취할 수 있는 TFMK 호텔로 이동합니다."), translate("&7다른 컨텐츠를 이용하기 위해서는 호텔 체크인을 먼저 해야합니다."), translate("&f"), translate("&3&o< 클릭 시 이동합니다 >")));
        hotelGuiItem.setItemMeta(hotelGuiItemMeta);


        waterParkGuiItem = new ItemStack(Material.BLUE_WOOL);
        ItemMeta waterParkGuiItemMeta = waterParkGuiItem.getItemMeta();
        waterParkGuiItemMeta.setDisplayName(translate("&b워터파크 입구로 이동"));
        waterParkGuiItemMeta.setLore(Arrays.asList(translate("&7무더운 여름! 워터파크에서 시원하게 시간을 보내보세요."), translate("&7실제 워터파크 놀이기구들을 즐겨볼 수 있습니다!"), translate("&f"), translate("&3&o< 클릭 시 이동합니다 >")));
        waterParkGuiItem.setItemMeta(waterParkGuiItemMeta);

        harborGuiItem = new ItemStack(Material.QUARTZ_PILLAR);
        ItemMeta harborGuiItemMeta = harborGuiItem.getItemMeta();
        harborGuiItemMeta.setDisplayName(translate("&3항구로 이동"));
        harborGuiItemMeta.setLore(Arrays.asList(translate("&7짝수 시마다 열리는 낚시대회에 참가해보세요!"), translate("&7다양한 물고기를 낚아 TFMK 토큰으로 바꿀 수 있습니다."), translate("&f"), translate("&3&o< 클릭 시 이동합니다 >")));
        harborGuiItem.setItemMeta(harborGuiItemMeta);

        menSionGuiItem = new ItemStack(Material.DARK_OAK_LOG);
        ItemMeta menSionGuiItemMeta = menSionGuiItem.getItemMeta();
        menSionGuiItemMeta.setDisplayName(translate("&6멘션으로 이동"));
        menSionGuiItemMeta.setLore(Arrays.asList(translate("&7멘션에서 벌어지는 알 수 없는 일들..."), translate("&7직접 무슨 일이 일어나는지 조사하러 떠나봐요!"), translate("&f"), translate("&3&o< 클릭 시 이동합니다 >")));
        menSionGuiItem.setItemMeta(menSionGuiItemMeta);

        blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackGlassPaneMeta = blackGlassPane.getItemMeta();
        blackGlassPaneMeta.setDisplayName(translate("&f"));
        blackGlassPane.setItemMeta(blackGlassPaneMeta);

        whiteGlassPane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta whiteGlassPaneMeta = whiteGlassPane.getItemMeta();
        whiteGlassPaneMeta.setDisplayName(translate("&f"));
        whiteGlassPane.setItemMeta(whiteGlassPaneMeta);
    }

    public static ItemStack getHotelGuiItem() {
        return hotelGuiItem;
    }

    public static ItemStack getWaterParkGuiItem() {
        return waterParkGuiItem;
    }

    public static ItemStack getHarborGuiItem() {
        return harborGuiItem;
    }

    public static ItemStack getMenSionGuiItem() {
        return menSionGuiItem;
    }

    public static ItemStack getBlackGlassPane() {
        return blackGlassPane;
    }

    public static ItemStack getWhiteGlassPane() {
        return whiteGlassPane;
    }
}
