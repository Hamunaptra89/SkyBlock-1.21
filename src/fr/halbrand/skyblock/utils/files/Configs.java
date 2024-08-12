package fr.halbrand.skyblock.utils.files;

import fr.halbrand.skyblock.Main;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

public class Configs {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    private final YamlConfiguration config;
    private final File file;

    public Configs(String path, String fileName) {
        this.file = new File(Main.getInstance().getDataFolder(), path + fileName);

        if (!this.file.exists()) {
            Main.getInstance().saveResource(fileName, false);
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public static Configs load(String path, String fileName) {
        return new Configs(path, fileName);
    }

    public static void copy(String source, String path, String target) {
        File sourceFile = new File(Main.getInstance().getDataFolder(), source);
        File targetFile = new File(Main.getInstance().getDataFolder(), path + target);

        if (targetFile.exists() || sourceFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
            return;
        }

        if (!targetFile.exists()) {
            try {
                FileUtils.copyFile(sourceFile, targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sourceFile.delete();
    }

    public void reload() {
        YamlConfiguration.loadConfiguration(this.file);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }
}
