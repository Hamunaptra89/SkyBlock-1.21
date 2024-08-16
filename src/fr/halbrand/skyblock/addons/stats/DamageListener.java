package fr.halbrand.skyblock.addons.stats;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.text.*;

public class DamageListener implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity entity && !(e.getEntity() instanceof ArmorStand)) {
            if (e.getDamager() instanceof Player p) {
                PStats stats = new PStats(p);

                int DAMAGE = stats.get(EnumStats.DAMAGE);
                int STRENGTH = stats.get(EnumStats.STRENGTH);
                double damage = e.getFinalDamage() * (1 + DAMAGE / 100.0) * (1 + STRENGTH / 100.0);

                spawnDamageIndication(entity, damage * 5);
                e.setDamage(damage);
            }
        }
    }

    private void spawnDamageIndication(LivingEntity entity, double damage) {
        Colors color = new Colors(entity.getKiller());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        double xOffset = (Math.random() - 0.5) * 1;
        double zOffset = (Math.random() - 0.5) * 1;

        ArmorStand armorStand = entity.getLocation().getWorld().spawn(entity.getLocation().add(xOffset + 0.40, 0.15, zOffset + 0.40), ArmorStand.class, as -> {
            as.setInvisible(true);
            as.setSmall(true);
            as.setGravity(false);
            as.setCustomName(color.set(String.format("&c%s ❤", decimalFormat.format(damage))));
            as.setCustomNameVisible(true);
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.remove();
            }
        }.runTaskLater(Main.getInstance(), 15L);
    }
}