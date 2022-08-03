package ms.zero.tfmk.tfmkhidenseek.hologram;

import ms.zero.tfmk.tfmkhidenseek.game.objects.GamePlayer;
import ms.zero.tfmk.tfmkhidenseek.game.util.GameScore;
import org.bukkit.Location;

import java.util.ArrayList;

import static ms.zero.tfmk.tfmkhidenseek.global.GlobalVariable.world;
import static ms.zero.tfmk.tfmkhidenseek.global.Util.translate;

public class HologramRankingPreset {


    private static ArrayList<Hologram> firstPageHologramSet = new ArrayList<>();
    private static ArrayList<Hologram> secondPageHologramSet = new ArrayList<>();

    public static void resetRanking() {
        firstPageHologramSet.clear();
        secondPageHologramSet.clear();


        firstPageHologramSet.add(new Hologram(translate("&7===== (&c킬 랭킹&7) ====="), new Location(world, 285, 86.9, -101.5), false));
        firstPageHologramSet.add(new Hologram(translate("&7#1 &f &e(kill_rank_1_name) &8&o((kill_rank_1_score)명)"), new Location(world, 285, 86.4, -101.5), false));
        firstPageHologramSet.add(new Hologram(translate("&7#2 &f &d(kill_rank_2_name) &8&o((kill_rank_2_score)명)"), new Location(world, 285, 86.05, -101.5), false));
        firstPageHologramSet.add(new Hologram(translate("&7#3 &f &b(kill_rank_3_name) &8&o((kill_rank_3_score)명)"), new Location(world, 285, 85.7, -101.5), false));
        firstPageHologramSet.add(new Hologram(translate("&7#4 &f &f(kill_rank_4_name) &8&o((kill_rank_4_score)명)"), new Location(world, 285, 85.35, -101.5), false));
        firstPageHologramSet.add(new Hologram(translate("&7#5 &f &f(kill_rank_5_name) &8&o((kill_rank_5_score)명)"), new Location(world, 285, 85, -101.5), false));
        firstPageHologramSet.add(new Hologram(translate("&7&o(우클릭하여 전환)"), new Location(world, 285, 84.6, -101.5), true));

        secondPageHologramSet.add(new Hologram(translate("&7===== (&3획득 키 랭킹&7) ====="), new Location(world, 285, 86.9, -101.5), false));
        secondPageHologramSet.add(new Hologram(translate("&7#1 &f &e(key_rank_1_name) &8&o((key_rank_1_score)개)"), new Location(world, 285, 86.4, -101.5), false));
        secondPageHologramSet.add(new Hologram(translate("&7#2 &f &d(key_rank_2_name) &8&o((key_rank_2_score)개)"), new Location(world, 285, 86.05, -101.5), false));
        secondPageHologramSet.add(new Hologram(translate("&7#3 &f &b(key_rank_3_name) &8&o((key_rank_3_score)개)"), new Location(world, 285, 85.7, -101.5), false));
        secondPageHologramSet.add(new Hologram(translate("&7#4 &f &f(key_rank_4_name) &8&o((key_rank_4_score)개)"), new Location(world, 285, 85.35, -101.5), false));
        secondPageHologramSet.add(new Hologram(translate("&7#5 &f &f(key_rank_5_name) &8&o((key_rank_5_score)개)"), new Location(world, 285, 85, -101.5), false));
        secondPageHologramSet.add(new Hologram(translate("&7&o(우클릭하여 전환)"), new Location(world, 285, 84.6, -101.5), true));
    }
    public static void updateRanking() {
        ArrayList<GamePlayer> sortedKillRanking = GameScore.getKillRanking();
        if (sortedKillRanking.size() >= 5) {
            for (int i = 1; i < 5 + 1; i++) {
                firstPageHologramSet.get(i).setHologramText(firstPageHologramSet.get(i).getHologramText().replace("(kill_rank_" + i + "_name)", sortedKillRanking.get(i - 1).getPlayer().getName()));
                firstPageHologramSet.get(i).setHologramText(firstPageHologramSet.get(i).getHologramText().replace("(kill_rank_" + i + "_score)", sortedKillRanking.get(i - 1).getKillScore().toString()));
            }
        } else {
            for (int i = 1; i < sortedKillRanking.size() + 1; i++) {
                firstPageHologramSet.get(i).setHologramText(firstPageHologramSet.get(i).getHologramText().replace("(kill_rank_" + i + "_name)", sortedKillRanking.get(i - 1).getPlayer().getName()));
                firstPageHologramSet.get(i).setHologramText(firstPageHologramSet.get(i).getHologramText().replace("(kill_rank_" + i + "_score)", sortedKillRanking.get(i - 1).getKillScore().toString()));
            }
            for (int i = sortedKillRanking.size() + 1; i < 5 + 1; i++) {
                firstPageHologramSet.get(i).setHologramText(firstPageHologramSet.get(i).getHologramText().replace("(kill_rank_" + i + "_name)", "N/A"));
                firstPageHologramSet.get(i).setHologramText(firstPageHologramSet.get(i).getHologramText().replace("(kill_rank_" + i + "_score)", "N/A"));
            }
        }

        ArrayList<GamePlayer> sortedKeyRanking = GameScore.getPickedUpKeyRanking();
        if (sortedKeyRanking.size() >= 5) {
            for (int i = 1; i < 5 + 1; i++) {
                secondPageHologramSet.get(i).setHologramText(secondPageHologramSet.get(i).getHologramText().replace("(key_rank_" + i + "_name)", sortedKeyRanking.get(i - 1).getPlayer().getName()));
                secondPageHologramSet.get(i).setHologramText(secondPageHologramSet.get(i).getHologramText().replace("(key_rank_" + i + "_score)", sortedKeyRanking.get(i - 1).getPickedUpKeyScore().toString()));
            }
        } else {
            for (int i = 1; i < sortedKeyRanking.size() + 1; i++) {
                secondPageHologramSet.get(i).setHologramText(secondPageHologramSet.get(i).getHologramText().replace("(key_rank_" + i + "_name)", sortedKeyRanking.get(i - 1).getPlayer().getName()));
                secondPageHologramSet.get(i).setHologramText(secondPageHologramSet.get(i).getHologramText().replace("(key_rank_" + i + "_score)", sortedKeyRanking.get(i - 1).getPickedUpKeyScore().toString()));
            }
            for (int i = sortedKeyRanking.size() + 1; i < 5 + 1; i++) {
                secondPageHologramSet.get(i).setHologramText(secondPageHologramSet.get(i).getHologramText().replace("(key_rank_" + i + "_name)", "N/A"));
                secondPageHologramSet.get(i).setHologramText(secondPageHologramSet.get(i).getHologramText().replace("(key_rank_" + i + "_score)", "N/A"));
            }
        }
    }

    public static ArrayList<Hologram> getKillRanking() {
        return firstPageHologramSet;
    }

    public static ArrayList<Hologram> getKeyRanking() {
        return secondPageHologramSet;
    }
}
