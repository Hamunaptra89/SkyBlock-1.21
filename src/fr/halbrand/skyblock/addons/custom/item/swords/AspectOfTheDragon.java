package fr.halbrand.skyblock.addons.custom.item.swords;

import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.tools.message.Message;

import org.bukkit.Effect;
import org.bukkit.Location;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class AspectOfTheDragon implements Listener {

    /*************************************
     * Rewritten on 16/08/2024 at 18h00. *
     *************************************/

    private final Map<UUID, Integer> cooldowns = new HashMap<>(); // Map pour stocker les cooldowns des joueurs.
    private final double COOLDOWN = 60; // Cooldown en secondes.
    private final double ABILITY_DAMAGE = 2400; // Nombre de dommage que provoque l'abilité
    // (x5 car tout est multiplier par 5, la barre de vie des mobs, la vie du joueur etc)
    private final int REQUIRED_MANA = 100; // Nombre de mana pour utiliser l'abilité.
    private final int ABILITY_RANGE = 4; // Nombre de block dont le joueur sera téléporté avec l'abilité.

    @EventHandler
    public void AspectOfTheDragon_Use(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        PStats Stats = new PStats(p);
        Colors color = new Colors(p);

        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        String name = meta.getDisplayName();

        if (!meta.hasDisplayName()) {
            return;
        }

        if (!name.equals(color.set("&dAspect of the Dragon"))) {
            return;
        }


        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            int cooldownEnd = cooldowns.getOrDefault(uuid, 0);

            if (System.currentTimeMillis() / 1000L < cooldownEnd) {
                int timeLeft = cooldownEnd - (int) (System.currentTimeMillis() / 1000L);
                Message.send(p, "&cVeuillez attendre avant d'utiliser l'abilité à nouveau. &8(&e%cooldown%s&8)".replace("%cooldown%", String.valueOf(timeLeft)));
                return;
            }


            if (Stats.get(EnumStats.MANA) < REQUIRED_MANA) {
                Message.send(p, "&cVous n'avez pas assez de mana !");
                return;
            }

            double MAX_MANA = Stats.get(EnumStats.MAX_MANA);
            double FINAL_DAMAGE = ABILITY_DAMAGE * (1 + MAX_MANA / 100.0 * 0.1) ;

            Location loc = p.getLocation();
            Vector direction = loc.getDirection().normalize();
            p.getWorld().playEffect(p.getLocation(), Effect.ENDERDRAGON_GROWL, null);
            Stats.rem(EnumStats.MANA, REQUIRED_MANA);
            cooldowns.put(uuid, (int) ((double) System.currentTimeMillis() / 1000L + COOLDOWN));

            for (Entity entity : p.getNearbyEntities(10, 5, 10)) {
                if (entity instanceof LivingEntity target && entity.getType() != EntityType.PLAYER) {
                    Vector toTarget = target.getLocation().toVector().subtract(loc.toVector());
                    double distance = toTarget.length();
                    toTarget = toTarget.normalize();

                    if (toTarget.dot(direction) > 0.5 && distance <= ABILITY_RANGE) {
                        target.damage(FINAL_DAMAGE, p);

                        Vector knockback = toTarget.multiply(2);
                        target.setVelocity(knockback);
                    }
                }
            }

        }
    }
}