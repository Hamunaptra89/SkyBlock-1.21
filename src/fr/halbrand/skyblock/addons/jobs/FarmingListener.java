package fr.halbrand.skyblock.addons.jobs;

import fr.halbrand.skyblock.addons.UpdateListener;
import fr.halbrand.skyblock.utils.files.data.JobStats;
import fr.halbrand.skyblock.utils.files.Configs;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FarmingListener implements Listener {

    /*************************************
     * Rewritten on 15/08/2024 at 20h00. *
     *************************************/

    public static Configs farmer = new Configs("configs/jobs/", "farmer.yml");
    private final Set<Location> Block_Locations = new HashSet<>();
    private final Map<Location, Long> cooldown = new HashMap<>();
    private final long COOLDOWN_TIME_SECONDS = 600;

    @EventHandler
    public void onBreakCrops(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Location loc = b.getLocation();
        Material m = b.getType();
        int count = 1;

        ConfigurationSection section = farmer.getConfigurationSection("Farmer.Custom-Drops." + b.getType().name());

        if (cooldown.containsKey(loc)) {
            long lastBreakTime = cooldown.get(loc);
            long currentTime = System.currentTimeMillis() / 1000;

            if (currentTime - lastBreakTime < COOLDOWN_TIME_SECONDS) {
                return;
            }
        }

        if (section != null && isGrow(b)) {
            if (m == Material.SUGAR_CANE || m == Material.BAMBOO || m == Material.KELP || m == Material.CHORUS_PLANT || m == Material.CHORUS_FLOWER) {
                count = countBlocks(b);
            }

            handleCropsBreak(p, b, count);
            e.setCancelled(true);
            b.setType(Material.AIR);
        }

        cooldown.put(loc, System.currentTimeMillis() / 1000);
    }

    private void handleCropsBreak(Player p, Block b, int count) {
        JobStats jobs = new JobStats(p);
        Random rdm = new Random();

        ConfigurationSection section = farmer.getConfigurationSection("Farmer.Custom-Drops." + b.getType().name());
        ConfigurationSection drops = section.getConfigurationSection("Drops");

        if (drops != null) {
            for (String key : drops.getKeys(false)) {
                ConfigurationSection items = drops.getConfigurationSection(key);
                int minAmount = items.getInt("Min", 1);
                int maxAmount = items.getInt("Max", 1);
                int amount = minAmount + rdm.nextInt(maxAmount - minAmount + 1);

                Material material = Material.matchMaterial(key);
                b.getLocation().getWorld().dropItem(b.getLocation(), new ItemStack(material, amount));
            }

            double xp = section.getDouble("Xp");
            double total_xp = xp * count;
            jobs.addXp("Farmer", total_xp);

            UpdateListener.UpdateActionBar(p, "Farmer", total_xp);
        }
    }

    private boolean isGrow(Block b) {
        BlockData data = b.getBlockData();

        if (!farmer.getBoolean("Farmer.Did_Crops_Grow_For_Xp")) {
            return true;
        }

        if (data instanceof Ageable) {
            return ((Ageable) data).getAge() == ((Ageable) data).getMaximumAge();
        }

        return false;
    }

    private int countBlocks(Block b) {
        Block_Locations.clear();
        return CalculBlockCount(b);
    }

    private int CalculBlockCount(Block b) {
        Material m = b.getType();
        int count = 0;

        if (m == Material.SUGAR_CANE || m == Material.BAMBOO || m == Material.KELP) {
            count++;
            count += CalculBlockCount(b.getRelative(0, 1, 0));
        } else if (m == Material.CHORUS_PLANT || m == Material.CHORUS_FLOWER) {
            count++;
            count += CalculBlockCount(b.getRelative(0, 1, 0));
            count += CalculBlockCount(b.getRelative(1, 0, 0));
            count += CalculBlockCount(b.getRelative(-1, 0, 0));
            count += CalculBlockCount(b.getRelative(0, 0, 1));
            count += CalculBlockCount(b.getRelative(0, 0, -1));
        }

        return count;
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e) {
        Block b = e.getBlock();
        Location loc = b.getLocation();

        cooldown.remove(loc);
    }
}