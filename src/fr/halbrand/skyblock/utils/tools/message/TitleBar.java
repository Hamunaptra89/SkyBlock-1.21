package fr.halbrand.skyblock.utils.tools.message;

import fr.halbrand.skyblock.utils.tools.Colors;
import org.bukkit.entity.Player;

public class TitleBar {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    public static void send(Player p, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Colors color = new Colors(p);
        p.sendTitle(color.set(title), color.set(subTitle), fadeIn, stay, fadeOut);
    }
}
