package fr.halbrand.skyblock.utils.tools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

import java.util.*;

public class Items {

    /*************************************
     * Rewritten on 12/06/2024 at 14h00. *
     *************************************/

    private final Colors color;
    private ItemStack i;

    public Items(Player p, Material m, int... amount) {
        this.color = new Colors(p);

        if (amount.length == 0) {
            this.i = new ItemStack(m);
        } else {
            int quantity = Math.max(1, amount[0]);
            this.i = new ItemStack(m, quantity);

            if (amount.length > 1) {
                i.setAmount(Math.min(quantity, 64));
            }
        }
    }

    public Items setName(String name) {
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(name);
        i.setItemMeta(m);
        return this;
    }

    public Items setLore(List<String> lore) {
        ItemMeta m = i.getItemMeta();
        List<String> coloredLore = new ArrayList<>();

        for (String line : lore) {
            coloredLore.add(color.set(line));
        }

        m.setLore(coloredLore);
        i.setItemMeta(m);
        return this;
    }

    public Items setLore(String... lore) {
        ItemMeta meta = i.getItemMeta();
        List<String> lores = new ArrayList<>();

        for (String line : lore) {
            lores.add(color.set(line));
        }

        meta.setLore(lores);
        i.setItemMeta(meta);
        return this;
    }

    public Items setGlow() {
        ItemMeta m = i.getItemMeta();
        m.addEnchant(Enchantment.LURE, 1, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(m);
        return this;
    }

    public Items addItemFlag(ItemFlag itemFlag) {
        ItemMeta m = i.getItemMeta();
        m.addItemFlags(itemFlag);
        i.setItemMeta(m);
        return this;
    }

    public Items addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        i.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public Items setPotionType(PotionType type, boolean isSplash) {
        if (i.getType() == Material.POTION || i.getType() == Material.SPLASH_POTION) {
            PotionMeta meta = (PotionMeta) i.getItemMeta();
            PotionData data = new PotionData(type, isSplash, false);
            meta.setBasePotionData(data);
            i.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack im() {
        return i;
    }
}