package fr.halbrand.skyblock.addons.custom.items.swords;

import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.tools.message.Message;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AspectOfTheEnd implements Listener {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @EventHandler
    public void AspectOfTheEnd_Use(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Colors color = new Colors(p);
        PStats Stats = new PStats(p);

        if (p.getItemInHand().getItemMeta().getDisplayName().equals(color.set("&aAspect of the End"))
                && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            if (Stats.get(EnumStats.MANA) >= 50) {
                Block b = p.getTargetBlock(null, 4);
                Location loc = new Location(b.getWorld(), b.getX(), b.getY(), b.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                Location locAbove = loc.clone().add(0, 1, 0);

                if (isSafeLocation(loc) && isSafeLocation(locAbove)) {
                    Stats.rem(EnumStats.MANA, 50);

                    p.teleport(loc);
                    p.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                } else {
                    Message.send(p, "&cLa destination n'est pas sûre !");
                }
            } else {
                Message.send(p, "&cVous n'avez pas assez de mana !");
            }
        }
    }

    private boolean isSafeLocation(Location loc) {
        Block block = loc.getBlock();
        return block.getType() == Material.AIR || block.isPassable();
    }
}