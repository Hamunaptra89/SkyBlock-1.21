package fr.halbrand.skyblock.addons;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.addons.custom.mecanics.lores.*;
import fr.halbrand.skyblock.addons.player.scoreboard.*;
import fr.halbrand.skyblock.addons.stats.HealthListener;
import fr.halbrand.skyblock.addons.stats.ManaListener;
import fr.halbrand.skyblock.addons.stats.SpeedListener;
import fr.halbrand.skyblock.addons.stats.VitalityListener;
import fr.halbrand.skyblock.utils.files.Configs;
import fr.halbrand.skyblock.utils.tools.message.ActionBar;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.*;

public class UpdateListener implements Listener {

    public static Configs config = new Configs("", "config.yml");
    public static Configs farmer = new Configs("configs/jobs/", "farmer.yml");

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    /*
     * Pour tout les joueurs :
     * (Tout les 5 ticks (0.25s)):
     *
     * Mise à jour de l'object en main (Stats),
     * Mise à jour des objects dans les slots d'armure (Stats).
     * Mise à jour de la Santé (Stats),
     * Mise à jour de la Vitesse (Stats),
     * Mise à jour de l'ActionBar,
     *
     *
     * (Tout les 20 ticks (1s)) :
     *
     * Mise à jour de la Vitalité (Stats),
     * Mise à jour du Lana (Stats),
     */

    public static void UpdatePlayer() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                HealthListener.UpdateHealth(p);
                SpeedListener.UpdateSpeed(p);
            }, 1L, 5L);

            Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                VitalityListener.UpdateVitality(p);
                ManaListener.UpdateMana(p);

                UpdateListener.UpdateActionBar(p, "Normal", 0);
                TabManager.setup(p);
                SBManager.setup(p);
            }, 1L, 20L);

            Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                ItemsManager.UpdateHandItemsStats(p);
                ItemsManager.UpdateArmorItemsStats(p);
            }, 1L, 2L);
        });
    }


    /*
     * Mise à jour (retrait) des statistique du joueur
     * (Tout les objets de l'armure et de la main).
     */

    public static void RemoveStats(Player p) {
        LoreManager.checkItem(p, p.getInventory().getItemInMainHand(), true);

        for (ItemStack item : p.getInventory().getArmorContents()) {
            LoreManager.checkItem(p, item, true);
        }
    }

    /*
     * Mise à jour des informations
     * du joueur dans l'actionbar.
     */

    public static void UpdateActionBar(Player p, String t, double xp) {
        String str = "";

        switch (t) {
            case "Farmer":
                str = farmer.getString("Farmer.ActionBar").replaceAll("%arthaniacore_jobs_xpgained%", String.valueOf(xp));
                break;

            case "Normal":
                str = config.getString("Config.ActionBar");
                break;

            default:
        }
        ActionBar.send(p, str);
    }
}