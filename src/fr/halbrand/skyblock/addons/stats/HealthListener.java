package fr.halbrand.skyblock.addons.stats;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

public class HealthListener implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    /*
     * Si la vraie santé est égale à 0 ou
     * que la santé est égale à la santé maximale,
     * ne rien faire.
     *
     * Si la santé est supérieure à sa santé maximale,
     * réglez la santé du joueur sur sa santé maximale.
     *
     * Mise à jour de la santé du joueur par rapport
     * à sa santé personnalisée créée par le plugin,
     * en arrondissant tout à un entier pour éviter les bugs.
     */

    public static void UpdateHealth(Player p) {
        PStats Stats = new PStats(p);

        int HEALTH = Stats.get(EnumStats.HEALTH);
        int MAX_HEALTH = Stats.get(EnumStats.MAX_HEALTH);
        int health = CalculValue(HEALTH);
        int maxHealth = CalculValue(MAX_HEALTH);

        if (HEALTH <= 0) {
            return;
        }

        if (HEALTH > MAX_HEALTH) {
            Stats.set(EnumStats.HEALTH, MAX_HEALTH);
            health = maxHealth;
        }

        p.setMaxHealth(maxHealth);
        p.setHealth(health);
    }


    /*
     * Calcule le nombre de cœurs visibles selon la vie du joueur.
     */

    private static int CalculValue(int HEALTH) {
        if (HEALTH <= 0) {
            return 0;
        } else if (HEALTH <= 100) {
            return HEALTH / 5;
        } else {
            return (HEALTH / 100) + 20;
        }
    }


    /*
     * Suppression de la régénération de la santé
     * pour éviter que les cœurs ne clignotent.
     */

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }


    /*
     * Si la santé moins les dommages reçus
     * est supérieure ou égale à 1, retirer les dommages.
     */

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PStats Stats = new PStats(p);

        int HEALTH = Stats.get(EnumStats.HEALTH);

        if (HEALTH <= 0 || p.isDead()) {
            Stats.set(EnumStats.HEALTH, 0);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                int MAX_HEALTH = Stats.get(EnumStats.MAX_HEALTH);

                Stats.set(EnumStats.HEALTH, MAX_HEALTH);
                p.spigot().respawn();
            }, 2);
        }
    }
}