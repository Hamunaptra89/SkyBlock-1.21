package fr.halbrand.skyblock.addons.guis;

import fr.halbrand.skyblock.utils.tools.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Menu {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    public static void open(Player p) {
        Colors color = new Colors(p);

        Inventory inv = Bukkit.createInventory(null, 9 * 6, color.set("&a&lMenu &8&l▪ &ePrincipal"));

        inv.setItem(20, new Items(p, Material.DIAMOND_SWORD, 1).setName(color.set("&aMenu &8&l▪ &bMétier")).im());
        inv.setItem(21, new Items(p, Material.PAINTING, 1).setName(color.set("&aMenu &8&l▪ &dCollection")).setLore("&dd %arthaniacore_stats_health%", "cc", "ee", "", "cc").im());

        inv.setItem(49, new Items(p, Material.BARRIER, 1).setName(color.set("&c&lQuitter &8▕ &7Menu")).im());

        p.openInventory(inv);
    }
}
