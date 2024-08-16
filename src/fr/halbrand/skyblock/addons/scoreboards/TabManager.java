package fr.halbrand.skyblock.addons.scoreboards;

import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.files.Configs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.util.*;

public class TabManager {

    public static Configs config = new Configs("", "config.yml");

    public static void setup(Player p) {
        Colors color = new Colors(p);
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(sb);

        List<String> headerLines = config.getStringList("Config.Tab.Header");
        List<String> footerLines = config.getStringList("Config.Tab.Footer");

        p.setPlayerListHeaderFooter(color.set(String.join("\n", headerLines)), color.set(String.join("\n", footerLines)));
    }
}
