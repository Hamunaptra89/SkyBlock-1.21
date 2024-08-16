package fr.halbrand.skyblock.utils.tools;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Colors {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    /*
     * Méthode servant à simplifier
     * la configuration (couleur).
     */

    private static Player p;

    public Colors(Player p) {
        Colors.p = p;
    }

    public String set(String msg) {
        return PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', msg));
    }

    public List<String> set(List<String> msgs) {
        List<String> lores = new ArrayList<>();

        for (String msg : msgs) {
            lores.add(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', msg)));
        }

        return lores;
    }
}
