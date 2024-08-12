package fr.halbrand.skyblock.addons.stats;

import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.*;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;

public class DefenceListener implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    /*
     * Si la santé moins le dommage final reçu
     * sont supérieur ou égal à 1, retiré le
     * nombre de dommage à la santé du joueur.
     */

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            PStats Stats = new PStats(p);

            int HEALTH = Stats.get(EnumStats.HEALTH);
            int DEFENCE = Stats.get(EnumStats.DEFENSE);

            int DAMAGE = (int) (e.getFinalDamage() * 5 * (1 - (double) DEFENCE / (DEFENCE + 100)));

            if (HEALTH - DAMAGE > 0) {
                e.setDamage(0);
                Stats.rem(EnumStats.HEALTH, DAMAGE);
            }
        }
    }
}