package fr.halbrand.skyblock.utils.files.data;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;

import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import java.io.*;

public class PStats {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    /*
     * Méthode servant à charger ou à écrire
     * les données du joueur crée par le plugin.
     */

    private final FileConfiguration data;
    private final File file;

    public PStats(Player p) {
        this.file = new File(Main.getInstance().getDataFolder() + "/data", p.getName() + "/stats.yml");
        this.data = YamlConfiguration.loadConfiguration(file);

        if (!this.file.exists()) {
            data.set("Main.Stats.HEALTH", 100);
            data.set("Main.Stats.MAX_HEALTH", 100);
            data.set("Main.Stats.VITALITY", 0);

            data.set("Main.Stats.MANA", 100);
            data.set("Main.Stats.MAX_MANA", 100);

            data.set("Main.Stats.DEFENCE", 0);
            data.set("Main.Stats.SPEED", 100);

            data.set("Main.Stats.FARMING_FORTUNE", 0);
            data.set("Main.Stats.MINING_SPEED", 0);

            data.set("Main.Stats.DAMAGE", 0);
            data.set("Main.Stats.STRENGTH", 0);

            save();
        }
    }

    public void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Module de santé du joueur dans
     * son fichier de statistique.
     *
     * - Récuperer la valeur (int),
     * - Mettre une valeur (int),
     * - Ajouter une valeur à l'existant (int),
     * - Retirer une valeur à l'existant (int).
     */

    public int get(EnumStats name) {
        return data.getInt("Main.Stats." + EnumStats.valueOf(String.valueOf(name))); }

    public void set(EnumStats name, double amount) {
        data.set("Main.Stats." + EnumStats.valueOf(String.valueOf(name)), amount);
        save(); }

    public void add(EnumStats name, double amount) {
        data.set("Main.Stats." + name, get(EnumStats.valueOf(String.valueOf(name))) + amount);
        save();
    }

    public void rem(EnumStats name, double amount) {
        data.set("Main.Stats." + name, get(EnumStats.valueOf(String.valueOf(name))) - amount);
        save();
    }
}
