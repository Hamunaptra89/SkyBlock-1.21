package fr.halbrand.skyblock.addons.guis.event;

import fr.halbrand.skyblock.addons.guis.Menu;
import fr.halbrand.skyblock.utils.tools.Colors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InvItemEvent implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @EventHandler
    public void onInventoryInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Colors color = new Colors(p);
        ItemStack item = e.getItem();

        if (item == null || item.getItemMeta() == null) {
            return;
        }

        String getName = item.getItemMeta().getDisplayName();

        if (item.getType() == Material.NETHER_STAR && getName.equals(color.set("&a&lMenu &8| &7Clic-Droit"))) {
            Menu.open(p);
        }
    }
    
    @EventHandler
    public void ItemMoveEvent(InventoryMoveItemEvent e) {
        Player p = (Player) e.getInitiator();
        Colors color = new Colors(p);

        ItemStack item = e.getItem();
        String getName = item.getItemMeta().getDisplayName();

        if (item.getItemMeta() == null) {
            return;
        }

        if (getName.equals(color.set("&a&lMenu &8| &7Clic-Droit"))) {
            e.setCancelled(true);
            Menu.open(p);
        }
    }
}
