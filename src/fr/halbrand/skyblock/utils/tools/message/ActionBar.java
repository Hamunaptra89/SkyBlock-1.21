package fr.halbrand.skyblock.utils.tools.message;

import fr.halbrand.skyblock.utils.tools.Colors;
import org.bukkit.entity.Player;

public class ActionBar {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    public static void send(Player p, String msg) {
        Colors color = new Colors(p);
        p.sendActionBar(color.set(msg));
    }
}
