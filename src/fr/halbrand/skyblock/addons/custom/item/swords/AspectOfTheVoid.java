package fr.halbrand.skyblock.addons.custom.item.swords;

import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.tools.message.Message;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AspectOfTheVoid implements Listener {

    /*************************************
     * Rewritten on 16/08/2024 at 18h00. *
     *************************************/

    private final Map<UUID, Integer> cooldowns = new HashMap<>(); // Map pour stocker les cooldowns des joueurs.
    private final double COOLDOWN = 1; // Cooldown en secondes.
    private final int REQUIRED_MANA = 35; // Nombre de mana pour utiliser l'abilité.
    private final int TELEPORT_RANGE = 8; // Nombre de block dont le joueur sera téléporté avec l'abilité.

    @EventHandler
    public void AspectOfTheVoid_Use(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        PStats Stats = new PStats(p);
        Colors color = new Colors(p);

        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        String name = meta.getDisplayName();

        if (!meta.hasDisplayName()) {
            return;
        }

        if (!name.equals(color.set("&dAspect of the Void"))) {
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

            Block b = p.getTargetBlock(null, TELEPORT_RANGE);
            Location loc = new Location(b.getWorld(), b.getX(), b.getY(), b.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
            Location locAbove = loc.clone().add(0, 1, 0);

            if (isSafeLocation(loc) && isSafeLocation(locAbove)) {
                Stats.rem(EnumStats.MANA, REQUIRED_MANA);
                cooldowns.put(uuid, (int) ((double) System.currentTimeMillis() / 1000L + COOLDOWN));

                p.teleport(loc);
                p.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            } else {
                Message.send(p, "&cLa destination n'est pas sûre !");
            }
        }
    }

    private boolean isSafeLocation(Location loc) {
        Block block = loc.getBlock();
        return block.getType() == Material.AIR || block.isPassable();
    }
}