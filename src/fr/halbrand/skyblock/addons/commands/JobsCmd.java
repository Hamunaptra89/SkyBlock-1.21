package fr.halbrand.skyblock.addons.commands;

import fr.halbrand.skyblock.addons.guis.jobs.Jobs;
import fr.halbrand.skyblock.addons.guis.jobs.farming.Farming;


import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class JobsCmd implements CommandExecutor {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return false;
        }

        if (args.length < 1) {
            Jobs.open(p);
        }

        if (args[0].equalsIgnoreCase("farmer")) {
            Farming.open(p);
        }

        if (args[0].equalsIgnoreCase("yeti")) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ALLAY, String.valueOf(4));
            npc.setName(ChatColor.RED + "Yeti " + ChatColor.GREEN + 1 + ChatColor.RED + "❤");
            npc.spawn(p.getLocation());

            ArmorStand healthDisplay = (ArmorStand) npc.getEntity().getWorld().spawnEntity(npc.getEntity().getLocation().add(0, 0.75, 0), EntityType.ARMOR_STAND);
            healthDisplay.setCustomNameVisible(true);
            healthDisplay.setGravity(false);
            healthDisplay.setVisible(false);
            healthDisplay.setSmall(true);
            healthDisplay.setCustomName(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Lvl" + 175 + ChatColor.DARK_GRAY + "] " + npc.getName());

            npc.setProtected(false);
        }

        return true;
    }
}
