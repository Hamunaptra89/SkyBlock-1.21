package fr.halbrand.skyblock.addons.stats;

import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.PStats;

import org.bukkit.entity.Player;

public class VitalityListener {

    /*************************************
     * Rewritten on 16/06/2024 at 18h00. *
     *************************************/

    /*
     * Si la santé est inférieur à sa santé maximale,
     * ajouter X point de vie à sa santé,
     * 1 point de vitalité = 1 point de vie restauré.
     */

    public static void UpdateVitality(Player p) {
        PStats Stats = new PStats(p);

        int HEALTH = Stats.get(EnumStats.HEALTH);
        int MAX_HEALTH = Stats.get(EnumStats.MAX_HEALTH);
        int VITALITY = Stats.get(EnumStats.VITALITY);
        int PLAYER_FOOD = p.getFoodLevel();

        if (HEALTH < MAX_HEALTH && PLAYER_FOOD == 20) {
            Stats.add(EnumStats.HEALTH, (MAX_HEALTH / 50) + VITALITY);
        }
    }
}