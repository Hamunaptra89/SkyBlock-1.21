package fr.halbrand.skyblock.addons.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class Menu implements CommandExecutor {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return false;
        }

        if (args.length < 1) {
            fr.halbrand.skyblock.addons.guis.Menu.open(p);
            return true;
        }

        return true;
    }
}
