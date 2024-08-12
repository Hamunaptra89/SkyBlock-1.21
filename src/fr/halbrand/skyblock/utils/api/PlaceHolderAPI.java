package fr.halbrand.skyblock.utils.api;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.JobStats;
import fr.halbrand.skyblock.utils.files.data.PStats;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;

public class PlaceHolderAPI extends PlaceholderExpansion {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    @Override
    public String getIdentifier() {
        return "arthaniacore";
    }

    @Override
    public String getAuthor() {
        return Main.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String s) {
        if (p == null) {
            return "";
        }

        PStats Stats = new PStats(p);

        switch (s) {
            case "stats_health" -> {
                return String.valueOf(Stats.get(EnumStats.HEALTH));
            }
            case "stats_maxhealth" -> {
                return String.valueOf(Stats.get(EnumStats.MAX_HEALTH));
            }
            case "stats_vitality" -> {
                return String.valueOf(Stats.get(EnumStats.VITALITY));
            }
            case "stats_defence" -> {
                return String.valueOf(Stats.get(EnumStats.DEFENSE));
            }
            case "stats_mana" -> {
                return String.valueOf(Stats.get(EnumStats.MANA));
            }
            case "stats_maxmana" -> {
                return String.valueOf(Stats.get(EnumStats.MAX_MANA));
            }
            case "stats_speed" -> {
                return String.valueOf(Stats.get(EnumStats.SPEED));
            }
            case "stats_farmingfortune" -> {
                return String.valueOf(Stats.get(EnumStats.FARMING_FORTUNE));
            }
            case "stats_miningspeed" -> {
                return String.valueOf(Stats.get(EnumStats.MINING_SPEED));
            }
            case "stats_damage" -> {
                return String.valueOf(Stats.get(EnumStats.DAMAGE));
            }
        }


        JobStats jobs = new JobStats(p);

        return switch (s) {
            case "jobs_farmer_level" -> String.valueOf(jobs.getLevel("Farmer"));
            case "jobs_farmer_nextlevel" -> String.valueOf(jobs.getNextLevel("Farmer"));
            case "jobs_farmer_xp" -> String.valueOf(jobs.getXp("Farmer"));
            case "jobs_farmer_xprequired" -> String.valueOf(jobs.getXpRequired("Farmer"));
            default -> s;
        };

    }
}