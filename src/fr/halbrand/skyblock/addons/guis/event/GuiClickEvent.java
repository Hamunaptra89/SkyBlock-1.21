package fr.halbrand.skyblock.addons.guis.event;

import fr.halbrand.skyblock.addons.guis.Menu;
import fr.halbrand.skyblock.addons.guis.collection.Collection;
import fr.halbrand.skyblock.addons.guis.jobs.Jobs;
import fr.halbrand.skyblock.addons.guis.jobs.farming.Farming;
import fr.halbrand.skyblock.utils.tools.Colors;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class GuiClickEvent implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @EventHandler
    public void onTitleOpen(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Colors color = new Colors(p);

        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
            return;
        }

        String[] titles = {
                "&a&lMenu &8&l▪ &ePrincipal",
                "&a&lMenu &8&l▪ &bMétier",
                "&a&lMétier &8&l▪ &6Agriculture",
                "&a&lMétier &8&l▪ &6Agriculture",
                "&a&lMétier &8&l▪ &6Agriculture",
                "&a&lMétier &8&l▪ &6Agriculture",
                "&a&lMétier &8&l▪ &6Agriculture",
                "&a&lMenu &8&l▪ &dCollection"
        };

        for (String title : color.set(List.of(titles))) {
            if (e.getView().getTitle().equals(title)) {
                e.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Colors color = new Colors(p);
        String getName = e.getCurrentItem().getItemMeta().getDisplayName();

        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
            return;
        }

        switch (e.getCurrentItem().getType()) {
            case PAINTING:
                if (getName.equals(color.set("&aMenu &8&l▪ &dCollection"))) {
                    Collection.open(p);
                }
                break;

            case DIAMOND_SWORD:
                if (getName.equals(color.set("&aMenu &8&l▪ &bMétier"))) {
                    Jobs.open(p);
                }
                break;

            case GOLDEN_HOE:
                if (getName.equals(color.set("&bMétier &8&l▪ &eAgriculture"))) {
                    Farming.open(p);
                }
                break;

            case BARRIER:
                if (getName.equals(color.set("&c&lQuitter &8▕ &7Menu"))) {
                    p.closeInventory(); }

                else if (getName.equals(color.set("&c&lRetour &8▕ &7Menu Principal"))) {
                    Menu.open(p); }

                else if (getName.equals(color.set("&c&lRetour &8▕ &7Menu des métiers"))) {
                    Jobs.open(p); }
                break;

            default:
                break;
        }
    }
}
