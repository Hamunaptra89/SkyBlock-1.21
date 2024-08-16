package fr.halbrand.skyblock.addons.custom.item.tools;

import fr.halbrand.skyblock.utils.tools.*;
import fr.halbrand.skyblock.utils.files.Configs;
import fr.halbrand.skyblock.utils.tools.Items;
import fr.halbrand.skyblock.utils.tools.message.ActionBar;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import java.util.*;

public class PumpkinDicer implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    Configs config = new Configs("", "config.yml");

    private final Map<Location, Long> cooldown = new HashMap<>();
    private final long COOLDOWN_TIME_SECONDS = 600;

    @EventHandler
    public void PumpkinDicer_Break(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Colors color = new Colors(p);
        Random r = new Random();
        Block b = e.getBlock();
        Material m = b.getType();
        Location loc = b.getLocation();

        if (m != Material.PUMPKIN) {
            return;
        }

        if (cooldown.containsKey(loc)) {
            long lastBreakTime = cooldown.get(loc);
            long currentTime = System.currentTimeMillis() / 1000;

            if (currentTime - lastBreakTime < COOLDOWN_TIME_SECONDS) {
                return;
            }
        }

        String tierPath = "";
        String name = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

        if (name.equals(color.set(config.getString("Config.CustomItems.Pumpkin-Dicer.Tier-1.Name")))) {
            tierPath = "Tier-1";
        } else if (name.equals(color.set(config.getString("Config.CustomItems.Pumpkin-Dicer.Tier-2.Name")))) {
            tierPath = "Tier-2";
        } else if (name.equals(color.set(config.getString("Config.CustomItems.Pumpkin-Dicer.Tier-3.Name")))) {
            tierPath = "Tier-3";
        } else {
            return;
        }

        if (!config.contains("Config.CustomItems.Pumpkin-Dicer." + tierPath)) {
            return;
        }

        for (int i = 4; i >= 1; i--) {
            String dropPath = "Config.CustomItems.Pumpkin-Dicer." + tierPath + ".Drop." + i;
            if (config.contains(dropPath + ".Chance") && config.getDouble(dropPath + ".Chance") >= r.nextDouble()) {
                p.getInventory().addItem(new Items(p, Material.PUMPKIN, config.getInt(dropPath + ".Amount")).im());
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                ActionBar.send(p, config.getString(dropPath + ".ActionBar"));
                break;
            }
        }

        cooldown.put(loc, System.currentTimeMillis() / 1000);
    }

    @EventHandler
    public void onPumpkinGrow(BlockGrowEvent e) {
        Block b = e.getBlock();
        Location loc = b.getLocation();

        cooldown.remove(loc);
    }
}