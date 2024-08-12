package fr.halbrand.skyblock.addons.player;

import fr.halbrand.skyblock.addons.UpdateListener;
import fr.halbrand.skyblock.utils.tools.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinEvent implements Listener {

    /*************************************
     * Rewritten on 23/03/2024 at 18h00. *
     *************************************/

    /*
     * Quand le joueur rejoinds il lance
     * la mise à jour de ses statistiques.
     */

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Colors color = new Colors(p);

        UpdateListener.UpdatePlayer();

        p.getInventory().setItem(8, new Items(p, Material.NETHER_STAR, 1).setName(color.set("&a&lMenu &8| &7Clic-Droit")).im());
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 1, false, false));
    }
}