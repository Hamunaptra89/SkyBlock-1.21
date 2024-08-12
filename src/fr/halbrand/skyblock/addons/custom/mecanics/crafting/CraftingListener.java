package fr.halbrand.skyblock.addons.custom.mecanics.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public class CraftingListener implements Listener {

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        Recipe recipe = inventory.getRecipe();
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            NamespacedKey recipeKey = shapedRecipe.getKey();
            ItemStack result = shapedRecipe.getResult();
            if (result.hasItemMeta() && result.getItemMeta().hasEnchants()) {
                Map<Character, Integer> ingredientAmounts = RecipeManager.getIngredientAmounts(recipeKey);
                if (ingredientAmounts != null && !ingredientAmounts.isEmpty()) {
                    boolean valid = true;
                    for (int i = 0; i < inventory.getMatrix().length; i++) {
                        ItemStack item = inventory.getMatrix()[i];
                        if (item != null) {
                            char slotChar = getCharForItem(i, shapedRecipe.getShape());
                            if (slotChar != 'X' && ingredientAmounts.containsKey(slotChar)) {
                                if (item.getAmount() < ingredientAmounts.get(slotChar)) {
                                    valid = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!valid) {
                        inventory.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        CraftingInventory inventory = (CraftingInventory) event.getInventory();
        Recipe recipe = inventory.getRecipe();
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            NamespacedKey recipeKey = shapedRecipe.getKey();
            ItemStack result = shapedRecipe.getResult();
            if (result.hasItemMeta() && result.getItemMeta().hasEnchants()) {
                Map<Character, Integer> ingredientAmounts = RecipeManager.getIngredientAmounts(recipeKey);
                if (ingredientAmounts != null && !ingredientAmounts.isEmpty()) {
                    boolean valid = true;
                    for (int i = 0; i < inventory.getMatrix().length; i++) {
                        ItemStack item = inventory.getMatrix()[i];
                        if (item != null) {
                            char slotChar = getCharForItem(i, shapedRecipe.getShape());
                            if (slotChar != 'X' && ingredientAmounts.containsKey(slotChar)) {
                                if (item.getAmount() < ingredientAmounts.get(slotChar)) {
                                    valid = false;
                                    event.setCancelled(true);
                                    break;
                                }
                            }
                        }
                    }
                    if (valid) {
                        for (int i = 0; i < inventory.getMatrix().length; i++) {
                            ItemStack item = inventory.getMatrix()[i];
                            if (item != null) {
                                char slotChar = getCharForItem(i, shapedRecipe.getShape());
                                if (slotChar != 'X' && ingredientAmounts.containsKey(slotChar)) {
                                    item.setAmount(item.getAmount() - ingredientAmounts.get(slotChar) + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private char getCharForItem(int index, String[] shape) {
        int row = index / 3;
        int col = index % 3;
        return shape[row].charAt(col);
    }
}
