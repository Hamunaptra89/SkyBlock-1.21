package fr.halbrand.skyblock.addons.player.scoreboard;

import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.files.Configs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.util.*;

public class SBManager {

    public static Configs config = new Configs("", "config.yml");

    public static void setup(Player p) {
        Colors color = new Colors(p);
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = sb.registerNewObjective("test", "dummy");

        String title = config.getString("Config.Scoreboard.Title");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(color.set(title));

        List<String> lines = config.getStringList("Config.Scoreboard.Lines");
        int scoreValue = lines.size();

        for (String line : lines) {
            Score score = objective.getScore(color.set(line));
            score.setScore(scoreValue);
            scoreValue--;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            Colors colors = new Colors(player);
            Team tabTeam = sb.getTeam(player.getName());
            if (tabTeam == null) {
                tabTeam = sb.registerNewTeam(player.getName());
            }

            tabTeam.addEntry(player.getName());
            tabTeam.setPrefix(colors.set("&7Hp: &c%arthaniacore_stats_health%/%arthaniacore_stats_maxhealth%❤ &8» "));
            tabTeam.setColor(ChatColor.AQUA);
            tabTeam.setSuffix(colors.set(" &8» &7Def: &a%arthaniacore_stats_defence%❈"));
        });

        p.setScoreboard(sb);
    }
}
