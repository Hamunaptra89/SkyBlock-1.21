package fr.halbrand.skyblock;

import fr.halbrand.skyblock.addons.custom.mecanics.crafting.CraftingListener;
import fr.halbrand.skyblock.addons.custom.mecanics.crafting.RecipeManager;
import fr.halbrand.skyblock.addons.custom.mecanics.mining.MiningManager;
import fr.halbrand.skyblock.addons.guis.event.*;
import fr.halbrand.skyblock.addons.UpdateListener;
import fr.halbrand.skyblock.addons.stats.*;

import fr.halbrand.skyblock.addons.custom.items.swords.*;
import fr.halbrand.skyblock.addons.custom.tools.PumpkinDicer;

import fr.halbrand.skyblock.addons.custom.mecanics.healthbar.EntityHealthBar;

import fr.halbrand.skyblock.addons.jobs.farmer.FarmingListener;

import fr.halbrand.skyblock.addons.player.*;
import fr.halbrand.skyblock.addons.commands.*;

import fr.halbrand.skyblock.utils.files.Configs;
import fr.halbrand.skyblock.utils.api.PlaceHolderAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import java.util.logging.Level;

public class EventManager {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    public static void StartUp() {
        Bukkit.getLogger().log(Level.INFO, "----------------[HypixelSB - Enable]----------------");
        Bukkit.getLogger().log(Level.INFO, "   Version : " + Main.getInstance().getDescription().getVersion() + " | Author : " + Main.getInstance().getDescription().getAuthors());
        Bukkit.getLogger().log(Level.INFO, "-------------------------------------------------------");
        long start = System.currentTimeMillis();

        Bukkit.getLogger().log(Level.INFO, " Loading HypixelSB/config.yml configuration file.");
        Main.getInstance().saveResource("config.yml", false);
        Configs.copy("config.yml", "", "config.yml");

        Bukkit.getLogger().log(Level.INFO, " Loading HypixelSB/crafts.yml configuration file.");
        Main.getInstance().saveResource("crafts.yml", false);
        Configs.copy("crafts.yml", "", "crafts.yml");

        Bukkit.getLogger().log(Level.INFO, " Loading HypixelSB/configs/jobs/farmer.yml configuration file.");
        Main.getInstance().saveResource("farmer.yml", false);
        Configs.copy("farmer.yml", "configs/jobs/", "farmer.yml");


        Bukkit.getLogger().log(Level.INFO, " Loading - PlaceHolderAPI module.");
        new PlaceHolderAPI().register();

        Bukkit.getLogger().log(Level.INFO, "-------------------------------------------------------");

        Bukkit.getLogger().log(Level.INFO, " Loading - JoinListener module (Listener).");
        Rl(new JoinEvent());
        Bukkit.getLogger().log(Level.INFO, " Loading - LeaveListener module (Listener).");
        Rl(new LeaveEvent());

        Bukkit.getLogger().log(Level.INFO, " Loading - UpdateManager module (UpdatePlayer).");
        UpdateListener.UpdatePlayer();

        Bukkit.getLogger().log(Level.INFO, "-------------------------------------------------------");

        Bukkit.getLogger().log(Level.INFO, " Loading - Health module (Listener).");
        Rl(new HealthListener());
        Bukkit.getLogger().log(Level.INFO, " Loading - Defence module (Listener).");
        Rl(new DefenceListener());
        Bukkit.getLogger().log(Level.INFO, " Loading - Damage module (Listener).");
        Rl(new DamageListener());

        Bukkit.getLogger().log(Level.INFO, "-------------------------------------------------------");

        Bukkit.getLogger().log(Level.INFO, " Loading - Jobs module (Listener).");
        Rl(new FarmingListener());

        Bukkit.getLogger().log(Level.INFO, " Loading - Custom Mechanics module (Listener).");
        Rl(new EntityHealthBar());


        Rl(new MiningManager());


        Bukkit.getLogger().log(Level.INFO, " Loading - Items Mechanics module (Listener).");
        Rl(new PumpkinDicer());
        Rl(new AspectOfTheEnd());
        Rl(new AspectOfTheVoid());


        Rl(new GuiClickEvent());
        Rl(new InvItemEvent());
        Rc("jobs", new JobsCmd());
        Rc("menu", new MainCmd());
        Rc("debug", new DebugCmd());

        Rl(new CraftingListener());
        RecipeManager.loadRecipes();

        long end = System.currentTimeMillis();
        long time = end - start;
        Bukkit.getLogger().log(Level.INFO, "");
        Bukkit.getLogger().log(Level.INFO, " Loaded in " + time + " ms.");
        Bukkit.getLogger().log(Level.INFO, "----------------[HypixelSB - Enable]----------------");
    }


    public static void EndUp() {
        Bukkit.getLogger().log(Level.INFO, "----------------[HypixelSB - Disable]----------------");
        Bukkit.getLogger().log(Level.INFO, "");
        long start = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UpdateListener.RemoveStats(player);
        }

        long end = System.currentTimeMillis();
        long time = end - start;
        Bukkit.getLogger().log(Level.INFO, "[ArthaNia-Core] Disabled in " + time + " ms.");
        Bukkit.getLogger().log(Level.INFO, "");
        Bukkit.getLogger().log(Level.INFO, "----------------[HypixelSB - Disable]----------------");
    }


    private static void Rl(Listener l) {
        Bukkit.getPluginManager().registerEvents(l, Main.getInstance());
    }

    private static void Rc(String cmd, CommandExecutor exe) {
        Main.getInstance().getCommand(cmd).setExecutor(exe);
    }

}