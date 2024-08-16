package fr.halbrand.skyblock.addons.guis.jobs;

import fr.halbrand.skyblock.utils.tools.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Jobs {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    public static void open(Player p) {
        Colors color = new Colors(p);

        Inventory inv = Bukkit.createInventory(null, 9 * 6, color.set("&a&lMenu &8&l▪ &bMétier"));

        inv.setItem(20, new Items(p, Material.GOLDEN_HOE, 1).setName(color.set("&bMétier &8&l▪ &eAgriculture")).im());
        inv.setItem(21, new Items(p, Material.JUNGLE_SAPLING, 1).setName(color.set("&bMétier &8&l▪ &cBucheron")).im());
        inv.setItem(22, new Items(p, Material.IRON_PICKAXE, 1).setName(color.set("&bMétier &8&l▪ &8Mineur")).im());
        inv.setItem(23, new Items(p, Material.IRON_SWORD, 1).setName(color.set("&bMétier &8&l▪ &6Chasseur")).im());
        inv.setItem(24, new Items(p, Material.FISHING_ROD, 1).setName(color.set("&bMétier &8&l▪ &aPéche")).im());

        inv.setItem(49, new Items(p, Material.BARRIER, 1).setName(color.set("&c&lRetour &8▕ &7Menu Principal")).im());

        p.openInventory(inv);
    }
}
