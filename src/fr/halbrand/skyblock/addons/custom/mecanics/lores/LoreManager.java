package fr.halbrand.skyblock.addons.custom.mecanics.lores;

import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.*;
import fr.halbrand.skyblock.utils.tools.Colors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class LoreManager {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    /*
     * Map de tous les lores pouvant déclencher des stats.
     */

    private static final Map<String, EnumStats> PREFIXES = Map.of(
            "&7Health: &a+", EnumStats.MAX_HEALTH,
            "&7Vitality: &a+", EnumStats.VITALITY,
            "&7Defence: &a+", EnumStats.DEFENSE,
            "&7Mana: &a+", EnumStats.MAX_MANA,
            "&7Damage: &c+", EnumStats.DAMAGE,
            "&7Speed: &a+", EnumStats.SPEED,
            "&7Farming Fortune: &a+", EnumStats.FARMING_FORTUNE,
            "&7Mining Speed: &a+", EnumStats.MINING_SPEED
    );

    public static void checkItem(Player p, ItemStack item, boolean remove) {
        PStats stats = new PStats(p);
        Colors color = new Colors(p);

        if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) return;

        ItemMeta meta = item.getItemMeta();

        if (meta.hasEnchant(Enchantment.DIG_SPEED)) {
            int efficiencyLevel = meta.getEnchantLevel(Enchantment.DIG_SPEED);
            int miningSpeedBonus = 10 + (efficiencyLevel * 20);

            if (remove) {
                stats.rem(EnumStats.MINING_SPEED, miningSpeedBonus);
            } else {
                stats.add(EnumStats.MINING_SPEED, miningSpeedBonus);
            }
        }

        List<String> lores = Objects.requireNonNull(meta).getLore();
        if (lores == null) return;

        lores.forEach(lore -> PREFIXES.forEach((prefix, stat) -> {
            String coloredLore = color.set(lore);
            if (coloredLore.startsWith(color.set(prefix))) {
                int val = Integer.parseInt(coloredLore.substring(prefix.length()).trim());

                if (remove) {
                    stats.rem(stat, val);
                } else {
                    stats.add(stat, val);
                }
            }
        }));
    }

    public static void updateItem(Player p, ItemStack oldItem, ItemStack newItem) {
        checkItem(p, oldItem, true);
        checkItem(p, newItem, false);
    }

    public static boolean isSame(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) return true;

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        return item1.getType() != item2.getType() || meta1 == null || !meta1.equals(meta2);
    }

    public static boolean isArmorSlot(int rawSlot) {
        return rawSlot >= 5 && rawSlot <= 8;
    }

    public static int getInventorySlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> 5;
            case CHEST -> 6;
            case LEGS -> 7;
            case FEET -> 8;
            default -> -1;
        };
    }
}