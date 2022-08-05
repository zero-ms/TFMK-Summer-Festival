package ms.zero.tfmk.tfmkfishing.fishing.util;

import ms.zero.tfmk.tfmkfishing.fishing.objects.FishType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static ms.zero.tfmk.tfmkfishing.global.Util.translateHexCodes;
import static ms.zero.tfmk.tfmkfishing.global.Util.translate;

public class FishInfo {
    private static HashMap<FishType, Integer> scoreTable = new HashMap<>();
    private static HashMap<FishType, ItemStack> itemTable = new HashMap<>();
    private static ItemStack fishingRod;


    static {
        scoreTable.put(FishType.RED_SEABREAM, 5);
        scoreTable.put(FishType.SALMON, 3);
        scoreTable.put(FishType.BLOWFISH, 2);
        scoreTable.put(FishType.MANDARIN_FISH, 1);
        scoreTable.put(FishType.SEASHELL, 0);
        scoreTable.put(FishType.SEAWEED, 0);

        ArrayList<String> lore = new ArrayList<>();

        ItemStack redSeaBream = new ItemStack(Material.TROPICAL_FISH);
        ItemMeta redSeaBreamMeta = redSeaBream.getItemMeta();
        redSeaBreamMeta.setDisplayName(translateHexCodes("&#bdb8b2참돔"));

        lore.add(translate("&7고급진 참돔이다."));
        lore.add(translate("&7낚기 힘든 생선이다."));
        lore.add(translate("&f"));
        lore.add(translate("&7점수: &65&7점"));

        redSeaBreamMeta.setLore(lore);
        redSeaBream.setItemMeta(redSeaBreamMeta);

        ItemStack salmon =  new ItemStack(Material.SALMON);
        ItemMeta salmonMeta = salmon.getItemMeta();
        salmonMeta.setDisplayName(translateHexCodes("&#ff6d1e연어"));

        lore = new ArrayList<>();
        lore.add(translate("&7기름지고 고소한 연어다."));
        lore.add(translate("&7개발자는 연어 회를 아주 좋아한다."));
        lore.add(translate("&f"));
        lore.add(translate("&7점수: &63&7점"));

        salmonMeta.setLore(lore);
        salmon.setItemMeta(salmonMeta);

        ItemStack blowFish = new ItemStack(Material.PUFFERFISH);
        ItemMeta blowFishMeta = blowFish.getItemMeta();
        blowFishMeta.setDisplayName(translateHexCodes("&#ab8748복어"));

        lore = new ArrayList<>();
        lore.add(translate("&7독이 잔뜩 오른 복어이다."));
        lore.add(translate("&7어쩌면 인생에서 딱 한 번만 맛볼 수 있을지도 모른다."));
        lore.add(translate("&f"));
        lore.add(translate("&7점수: &62&7점"));

        blowFishMeta.setLore(lore);
        blowFish.setItemMeta(blowFishMeta);

        ItemStack mandarinFish = new ItemStack(Material.COD);
        ItemMeta mandarinFishMeta = mandarinFish.getItemMeta();
        mandarinFishMeta.setDisplayName(translateHexCodes("&#644d24쏘가리"));


        lore = new ArrayList<>();
        lore.add(translate("&7매운탕에 넣어먹으면 맛있는 쏘가리다."));
        lore.add(translate("&7자주 보이는 생선 중 하나다."));
        lore.add(translate("&f"));
        lore.add(translate("&7점수: &61&7점"));

        mandarinFishMeta.setLore(lore);
        mandarinFish.setItemMeta(mandarinFishMeta);

        ItemStack seaShell = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta seaShellMeta = seaShell.getItemMeta();
        seaShellMeta.setDisplayName(translateHexCodes("&#f7b7c5바닷조개"));

        lore = new ArrayList<>();
        lore.add(translate("&7예쁜 바닷조개이다."));
        lore.add(translate("&7그다지 쓸모는 없어보인다..."));
        lore.add(translate("&f"));
        lore.add(translate("&7점수: &60&7점"));

        seaShellMeta.setLore(lore);
        seaShell.setItemMeta(seaShellMeta);

        ItemStack seaWeed = new ItemStack(Material.DRIED_KELP);
        ItemMeta seaWeedMeta = seaWeed.getItemMeta();
        seaWeedMeta.setDisplayName(translateHexCodes("&#599111미역"));

        lore = new ArrayList<>();
        lore.add(translate("&7짭잘한 바다향기가 느껴지는 미역이다."));
        lore.add(translate("&7낚싯대로 어떻게 낚은거지?"));
        lore.add(translate("&f"));
        lore.add(translate("&7점수: &60&7점"));

        seaWeedMeta.setLore(lore);
        seaWeed.setItemMeta(seaWeedMeta);


        itemTable.put(FishType.RED_SEABREAM, redSeaBream);
        itemTable.put(FishType.SALMON, salmon);
        itemTable.put(FishType.BLOWFISH, blowFish);
        itemTable.put(FishType.MANDARIN_FISH, mandarinFish);
        itemTable.put(FishType.SEASHELL, seaShell);
        itemTable.put(FishType.SEAWEED, seaWeed);

        fishingRod = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishingRodMeta = fishingRod.getItemMeta();
        fishingRodMeta.setDisplayName(translate("&bTFMK &7낚싯대"));
        fishingRodMeta.setLore(Arrays.asList(translate("&7TFMK팀이 한땀한땀 제작한 낚싯대이다."), translate("&7잘 손질되어 있다.")));
        fishingRod.setItemMeta(fishingRodMeta);
    }

    public static Integer getScoreFromFishType(FishType fishType) {
        return scoreTable.get(fishType);
    }

    public static ItemStack getItemFromFishType(FishType fishType) {
        return itemTable.get(fishType);
    }

    public static ItemStack getFishingRod() {
        return fishingRod;
    }
}
