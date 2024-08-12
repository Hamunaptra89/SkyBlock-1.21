package fr.halbrand.skyblock.addons.stats;

import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.PStats;

import org.bukkit.entity.Player;

public class ManaListener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h15. *
     *************************************/

    public static void UpdateMana(Player p) {
        PStats Stats = new PStats(p);

        int MANA = Stats.get(EnumStats.MANA);
        int MAX_MANA = Stats.get(EnumStats.MAX_MANA);

        if (MANA < MAX_MANA) {
            MANA += MAX_MANA / 50;

            if (MANA > MAX_MANA) {
                MANA = MAX_MANA;
            }

            Stats.set(EnumStats.MANA, MANA);

        } else if (MANA > MAX_MANA) {
            Stats.set(EnumStats.MANA, MAX_MANA);
        }
    }
}