package fr.halbrand.skyblock.addons.player;

import fr.halbrand.skyblock.addons.UpdateListener;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    /*************************************
     * Rewritten on 23/03/2024 at 20h00. *
     *************************************/

    /*
     * Quand le joueur quitte il met à jour (retrait)
     * ses statistique de tout les objets de l'armure et de la main.
     */

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        UpdateListener.RemoveStats(p);
    }
}