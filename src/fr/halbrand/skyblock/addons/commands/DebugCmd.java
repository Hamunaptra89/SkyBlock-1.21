package fr.halbrand.skyblock.addons.commands;

import fr.halbrand.skyblock.utils.tools.Colors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCmd implements CommandExecutor {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return false;
        }

        Colors color = new Colors(p);

        if (args.length < 1) {
            p.sendMessage(color.set("\n" +
                    "&e&lStatistiques :\n" +
                    "&8▪ &7Vie &b➠ &c%arthaniacore_stats_health%/%arthaniacore_stats_maxhealth%❤\n" +
                    "&8▪ &7Vitalité &b➠ &4%arthaniacore_stats_vitality%♨\n" +
                    "&8▪ &7Defence &b➠ &a%arthaniacore_stats_defence%❈\n" +
                    "&8▪ &7Mana &b➠ &b%arthaniacore_stats_mana%/%arthaniacore_stats_maxmana%❀\n" +
                    "&8▪ &7Vitesse &b➠ &f%arthaniacore_stats_speed%✦\n" +
                    "&8▪ &7Farming Fortune &b➠ &6%arthaniacore_stats_farmingfortune%☘\n" +
                    "&8▪ &7Mining Speed &b➠ &6%arthaniacore_stats_miningspeed%⸕\n" +
                    "&8▪ &7Damage &b➠ &c%arthaniacore_stats_damage%❈" +
                    "\n"));
            return true;
        }

        return true;
    }
}
