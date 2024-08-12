package fr.halbrand.skyblock.addons.custom.mecanics.crafting;

import fr.halbrand.skyblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeManager {

    private static final Map<NamespacedKey, Map<Character, Integer>> recipeIngredientAmounts = new HashMap<>();

    public static void loadRecipes() {
        File craftsFile = new File(Main.getInstance().getDataFolder(), "crafts.yml");
        if (!craftsFile.exists()) {
            Main.getInstance().saveResource("crafts.yml", false);
        }

        FileConfiguration craftsConfig = YamlConfiguration.loadConfiguration(craftsFile);
        ConfigurationSection craftSection = craftsConfig.getConfigurationSection("Craft");
        if (craftSection == null) {
            Main.getInstance().getLogger().warning("No crafts found in crafts.yml");
            return;
        }

        Set<String> craftKeys = craftSection.getKeys(false);

        for (String key : craftKeys) {
            String path = "Craft." + key;

            ConfigurationSection resultSection = craftsConfig.getConfigurationSection(path + ".Result");
            if (resultSection == null) {
                Main.getInstance().getLogger().warning("No result section found for " + key);
                continue;
            }

            ItemStack result = parseItem(resultSection);
            if (result == null) {
                Main.getInstance().getLogger().warning("Invalid result item for " + key);
                continue;
            }

            List<String> grid = craftsConfig.getStringList(path + ".Ingredient.Grid");
            if (grid.size() != 3) {
                Main.getInstance().getLogger().warning("Invalid ingredient grid size for " + key);
                continue;
            }

            NamespacedKey recipeKey = new NamespacedKey(Main.getInstance(), key.toLowerCase());
            ShapedRecipe recipe = new ShapedRecipe(recipeKey, result);
            recipe.shape(grid.get(0), grid.get(1), grid.get(2));

            ConfigurationSection ingredientSection = craftsConfig.getConfigurationSection(path + ".Ingredient");

            if (ingredientSection == null) {
                Main.getInstance().getLogger().warning("No ingredient section found for " + key);
                continue;
            }

            HashMap<Character, ItemStack> ingredientMap = new HashMap<>();
            for (String ingredientKey : ingredientSection.getKeys(false)) {
                if (!ingredientKey.equals("Grid")) {
                    ConfigurationSection ingredientConfig = ingredientSection.getConfigurationSection(ingredientKey);
                    if (ingredientConfig == null) {
                        Main.getInstance().getLogger().warning("No ingredient config found for " + ingredientKey + " in " + key);
                        continue;
                    }
                    ItemStack item = parseItem(ingredientConfig);
                    if (item == null) {
                        Main.getInstance().getLogger().warning("Invalid item specified for " + ingredientKey + " in " + key);
                        continue;
                    }
                    ingredientMap.put(ingredientKey.charAt(0), item);
                }
            }

            for (String line : grid) {
                for (int col = 0; col < line.length(); col++) {
                    char slotChar = line.charAt(col);
                    if (slotChar != 'X') { // Treat 'X' as empty slot
                        ItemStack item = ingredientMap.get(slotChar);
                        if (item != null) {
                            recipe.setIngredient(slotChar, item.getType());
                        }
                    }
                }
            }

            Bukkit.addRecipe(recipe);

            // Store the ingredient amounts for this recipe
            Map<Character, Integer> ingredientAmounts = new HashMap<>();
            for (Map.Entry<Character, ItemStack> entry : ingredientMap.entrySet()) {
                ingredientAmounts.put(entry.getKey(), entry.getValue().getAmount());
            }
            recipeIngredientAmounts.put(recipeKey, ingredientAmounts);
        }
    }

    private static ItemStack parseItem(ConfigurationSection config) {
        String materialName = config.getString("Material");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            Main.getInstance().getLogger().warning("Invalid material specified in config: " + materialName);
            return null;
        }

        int amount = config.getInt("Amount", 1);
        ItemStack itemStack = new ItemStack(material, amount);

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (config.contains("Name")) {
                meta.setDisplayName(config.getString("Name"));
            }
            if (config.contains("Lore")) {
                meta.setLore(config.getStringList("Lore"));
            }
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public static Map<Character, Integer> getIngredientAmounts(NamespacedKey recipeKey) {
        return recipeIngredientAmounts.get(recipeKey);
    }
}
