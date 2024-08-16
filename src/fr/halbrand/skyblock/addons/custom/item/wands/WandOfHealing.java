package fr.halbrand.skyblock.addons.custom.item.wands;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.tools.message.Message;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WandOfHealing implements Listener {

    /*************************************
     * Rewritten on 16/08/2024 at 18h00. *
     *************************************/

    private final Map<UUID, Integer> cooldowns = new HashMap<>(); // Map pour stocker les cooldowns des joueurs.
    private final double COOLDOWN = 60; // Cooldown en secondes.
    private final int REQUIRED_MANA = 50; // Nombre de mana pour utiliser l'abilité.
    private final double HEALTH_REGEN_AMOUNT = 5; // Nombre de vie régénéré à chaque DELAY_TIME.
    private final int DELAY_TIME = 1; // Nombre de temps avant chaque répétition de la tâche en secondes.
    private final int EXECUTE_TIME = 5; // Nombre de seconde avant la fin de la tâche.

    @EventHandler
    public void WandOfHealing_Use(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        PStats Stats = new PStats(p);
        Colors color = new Colors(p);

        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        String name = meta.getDisplayName();

        if (!meta.hasDisplayName()) {
            return;
        }

        if (!name.equals(color.set("&bSceptre de Soin"))) {
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

            Stats.rem(EnumStats.MANA, REQUIRED_MANA);
            cooldowns.put(uuid, (int) ((double) System.currentTimeMillis() / 1000L + COOLDOWN));
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);

            new BukkitRunnable() {
                int secondsLeft = EXECUTE_TIME;

                @Override
                public void run() {
                    if (secondsLeft <= 0) {
                        cancel();
                        return;
                    }

                    Stats.add(EnumStats.HEALTH, HEALTH_REGEN_AMOUNT);
                    secondsLeft--;
                }
            }.runTaskTimer(Main.getInstance(), 0L, DELAY_TIME * 20L);
        }
    }
}