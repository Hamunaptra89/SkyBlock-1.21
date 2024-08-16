package fr.halbrand.skyblock.addons.custom.mecanics;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.tools.Colors;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.text.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityHealthBar implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    private static final DecimalFormat DAMAGE_FORMAT = new DecimalFormat("0.00");
    private final Map<LivingEntity, String> entityNameMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        if (!(entity instanceof ArmorStand)) {
            updateEntityName(entity);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity entity && !(entity instanceof ArmorStand)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateEntityName(entity);
                }
            }.runTaskLater(Main.getInstance(), 1L);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location location = p.getLocation();

        entityNameMap.forEach((entity, name) -> {
            if (isWithinRange(entity, location)) {
                entity.setCustomName(name);
                entity.setCustomNameVisible(true);
            } else {
                entity.setCustomNameVisible(false);
            }
        });
    }

    private void updateEntityName(LivingEntity entity) {
        entityNameMap.put(entity, getEntityName(entity));
    }

    private boolean isWithinRange(LivingEntity entity, Location location) {
        return entity.getLocation().distanceSquared(location) <= 8 * 8;
    }

    private String getEntityName(LivingEntity entity) {
        Colors color = new Colors(entity.getKiller());
        int healthPercent = (int) ((entity.getHealth() / entity.getMaxHealth()) * 100);
        ChatColor chatColor = getHealthColor(healthPercent);

        String hp = DAMAGE_FORMAT.format(entity.getHealth() * 5);
        String maxhp = DAMAGE_FORMAT.format(entity.getMaxHealth() * 5);

        return color.set("&8[&7Lv1&8] &7" + entity.getType().name() + chatColor + " " + hp + "&7/&a" + maxhp + "&c❤");
    }

    private ChatColor getHealthColor(int healthPercent) {
        if (healthPercent <= 25) {
            return ChatColor.RED;
        } else if (healthPercent <= 50) {
            return ChatColor.GOLD;
        } else if (healthPercent <= 75) {
            return ChatColor.YELLOW;
        } else {
            return ChatColor.GREEN;
        }
    }
}