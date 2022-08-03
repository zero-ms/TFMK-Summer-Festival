package ms.zero.tfmk.tfmkfishing.fishing.util;

import ms.zero.tfmk.tfmkfishing.fishing.objects.FishType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class RandomSelector {

    private static LinkedHashMap<FishType, Integer> percentageTable = new LinkedHashMap<>();
    private static ArrayList<FishType> fishTypes = new ArrayList<>();

    static {
        percentageTable.put(FishType.RED_SEABREAM, 10);
        percentageTable.put(FishType.SALMON, 15);
        percentageTable.put(FishType.BLOWFISH, 25);
        percentageTable.put(FishType.MANDARIN_FISH, 30);
        percentageTable.put(FishType.SEASHELL, 10);
        percentageTable.put(FishType.SEAWEED, 10);

        fishTypes.add(FishType.RED_SEABREAM);
        fishTypes.add(FishType.SALMON);
        fishTypes.add(FishType.BLOWFISH);
        fishTypes.add(FishType.MANDARIN_FISH);
        fishTypes.add(FishType.SEASHELL);
        fishTypes.add(FishType.SEAWEED);
    }

    public static FishType getRandomFish() {
        Random random = new Random();
        Integer randomValue = random.nextInt(100) + 1;
        // 10, 15, 25, 30, 10, 10
        Integer index = 0;
        for (Integer percentage : percentageTable.values()) {
            randomValue -= percentage;
            if (randomValue > 0) {
                index += 1;
            } else {
                break;
            }
        }

        return fishTypes.get(index);
    }
}
