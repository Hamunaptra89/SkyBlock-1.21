package fr.halbrand.skyblock.addons.custom.items.swords.wand;

import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WandOfHealing implements Listener {

    public Map<UUID, Long> cooldowns;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        PStats Stats = new PStats(p);

        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if (item.getType() == Material.STICK && item.hasItemMeta()) {

            if (meta.hasDisplayName() && meta.getDisplayName().equals("cc")) {
                if (cooldowns.containsKey(uuid) && System.currentTimeMillis() < cooldowns.get(uuid) + 6000L) {
                    p.sendMessage("Wand of Mending is still on cooldown for another " + ((cooldowns.get(uuid) + 6000L - System.currentTimeMillis()) / 1000L) + " seconds.");
                    return;
                }

                Stats.add(EnumStats.HEALTH, 5);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                cooldowns.put(uuid, System.currentTimeMillis());
            }
        }
    }
}
