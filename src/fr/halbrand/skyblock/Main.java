package fr.halbrand.skyblock;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        EventManager.StartUp();
    }

    @Override
    public void onDisable() {
        EventManager.EndUp();
    }
}