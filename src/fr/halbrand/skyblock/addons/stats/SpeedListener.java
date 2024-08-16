package fr.halbrand.skyblock.addons.stats;

import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.*;

import org.bukkit.entity.Player;

public class SpeedListener {

    /*************************************
     * Rewritten on 16/06/2024 at 18h00. *
     *************************************/

    /*
     * Si la vitesse du joueur est supérieur à 500
     * mettre un cap à 400.
     */

    public static void UpdateSpeed(Player p) {
        PStats Stats = new PStats(p);

        int SPEED = Stats.get(EnumStats.SPEED);
        float final_speed;

        if (SPEED <= 400) {
            final_speed = (float) SPEED / 1000 + 0.1F;
        } else {
            final_speed = 0.5F;
        }

        p.setWalkSpeed(final_speed);
    }
}