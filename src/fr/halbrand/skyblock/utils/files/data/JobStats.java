package fr.halbrand.skyblock.utils.files.data;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.files.Configs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import java.io.*;
import java.util.List;

public class JobStats {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    private final FileConfiguration config;
    private final File file;
    private final Player p;

    public JobStats(Player p) {
        this.p = p;
        this.file = new File(Main.getInstance().getDataFolder() + "/data", p.getName() + "/jobs.yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            config.set("Jobs.Farmer.Level", 0);
            config.set("Jobs.Farmer.Xp", 0);
            config.set("Jobs.Miner.Level", 0);
            config.set("Jobs.Miner.Xp", 0);
            config.set("Jobs.Lumberjack.Level", 0);
            config.set("Jobs.Lumberjack.Xp", 0);
            save();
        }
    }

    public int getLevel(String jobName) {
        return config.getInt("Jobs." + jobName + ".Level", 0);
    }

    public void addLevel(String jobName, int amount) {
        config.set("Jobs." + jobName + ".Level", getLevel(jobName) + amount);
        save();
    }

    public double getXp(String jobName) {
        return config.getDouble("Jobs." + jobName + ".Xp");
    }

    public void addXp(String jobName, double amount) {
        config.set("Jobs." + jobName + ".Xp", getXp(jobName) + amount);
        save();
        rankup(jobName);
    }

    public void remXp(String jobName, double amount) {
        config.set("Jobs." + jobName + ".Xp", getXp(jobName) - amount);
        save();
    }

    private void rankup(String jobName) {
        PStats Stats = new PStats(p);
        Colors color = new Colors(p);

        Configs jobs = new Configs("configs/jobs/", jobName.toLowerCase() + ".yml");

        int level = getLevel(jobName);
        int maxLevel = getMaxLevel(jobName);

        if (level < maxLevel && getXp(jobName) >= getXpRequired(jobName)) {
            remXp(jobName, getXpRequired(jobName));
            addLevel(jobName, 1);

            ConfigurationSection perks = jobs.getConfigurationSection(jobName + ".Level." + (level + 1) + ".Perks");
            if (perks != null) {
                for (String key : perks.getKeys(false)) {
                    int value = perks.getInt(key);
                    switch (key) {
                        case "Max-Health":
                            Stats.add(EnumStats.MAX_HEALTH, value);
                            break;
                        case "Defence":
                            Stats.add(EnumStats.DEFENSE, value);
                            break;
                        case "Farming-Fortune":
                            Stats.add(EnumStats.FARMING_FORTUNE, value);
                            break;
                    }
                }
            }

            List<String> messages = jobs.getStringList(jobName + ".Level." + (level + 1) + ".Message");
            for (String msg : messages) {
                p.sendMessage(color.set(msg));
            }
        }
    }

    public int getNextLevel(String jobName) {
        Configs jobs = new Configs("configs/jobs/", jobName.toLowerCase() + ".yml");
        ConfigurationSection levels = jobs.getConfigurationSection(jobName + ".Level");
        if (levels != null) {
            int currentLevel = getLevel(jobName);
            for (String key : levels.getKeys(false)) {
                int level = Integer.parseInt(key);
                if (level > currentLevel) {
                    return level;
                }
            }
            return levels.getKeys(false).size();
        }
        return 0;
    }

    public int getXpRequired(String jobName) {
        Configs jobs = new Configs("configs/jobs/", jobName.toLowerCase() + ".yml");
        ConfigurationSection section = jobs.getConfigurationSection(jobName + ".Level." + getNextLevel(jobName));
        return (section != null) ? section.getInt("Xp-Required", 0) : 0;
    }

    private int getMaxLevel(String jobName) {
        Configs jobs = new Configs("configs/jobs/", jobName.toLowerCase() + ".yml");
        ConfigurationSection levels = jobs.getConfigurationSection(jobName + ".Level");
        return (levels != null) ? levels.getKeys(false).size() : 0;
    }

    private void save() {
        try {
            config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}