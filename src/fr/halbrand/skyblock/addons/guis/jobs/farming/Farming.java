package fr.halbrand.skyblock.addons.guis.jobs.farming;

import fr.halbrand.skyblock.utils.files.Configs;
import fr.halbrand.skyblock.utils.files.data.JobStats;
import fr.halbrand.skyblock.utils.tools.*;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import java.util.*;

public class Farming {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    public static Configs farmer = new Configs("configs/jobs/", "farmer.yml");

    public static void open(Player p) {
        JobStats jobs = new JobStats(p);
        Colors color = new Colors(p);

        Inventory inv = Bukkit.createInventory(null, farmer.getInt("Farmer.Gui.Settings.Slot"), color.set(farmer.getString("Farmer.Gui.Settings.Title")));

        ConfigurationSection section = farmer.getConfigurationSection("Farmer.Gui.Items");

        List<Integer> slots = farmer.getIntegerList("Farmer.Gui.Special.Slots");

        for (int index = 0; index < slots.size(); index++) {
            ItemStack item;
            int slot = slots.get(index);

            if (index < jobs.getLevel("Farmer")) {
                item = new Items(p, Material.valueOf(farmer.getString("Farmer.Gui.Special.Unlocked.Material")), index + 1)
                        .setName(color.set(farmer.getString("Farmer.Gui.Special.Unlocked.Name").replaceAll("%level%", String.valueOf(index + 1))))
                        .setLore(color.set(farmer.getStringList("Farmer.Gui.Special.Unlocked.Lore"))).im();

            } else if (index == jobs.getLevel("Farmer")) {
                item = new Items(p, Material.valueOf(farmer.getString("Farmer.Gui.Special.Actual.Material")), index + 1)
                        .setName(color.set(farmer.getString("Farmer.Gui.Special.Actual.Name").replaceAll("%level%", String.valueOf(index + 1))))
                        .setLore(color.set(farmer.getStringList("Farmer.Gui.Special.Actual.Lore"))).im();

            } else {
                item = new Items(p, Material.valueOf(farmer.getString("Farmer.Gui.Special.Locked.Material")), index + 1)
                        .setName(color.set(farmer.getString("Farmer.Gui.Special.Locked.Name").replaceAll("%level%", String.valueOf(index + 1))))
                        .setLore(color.set(farmer.getStringList("Farmer.Gui.Special.Locked.Lore"))).im();
            }
            inv.setItem(slot, item);
        }

        if (section != null) {
            for (String key : section.getKeys(false)) {
                String path = "Farmer.Gui.Items." + key;
                int slot = farmer.getInt(path + ".Slot");

                if (slot >= 0 && slot < farmer.getInt("Farmer.Gui.Settings.Slot")) {
                    inv.setItem(slot,
                            new Items(p,
                                    Material.getMaterial(farmer.getString(path + ".Material")),
                                    farmer.getInt(path + ".Amount"))
                                    .setName(color.set(farmer.getString(path + ".Name")))
                                    .setLore(color.set(farmer.getStringList(path + ".Lore"))).im());

                }
            }
        }
        p.openInventory(inv);
    }
}
