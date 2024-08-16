package fr.halbrand.skyblock.addons.custom.mecanics;

import fr.halbrand.skyblock.utils.files.data.PStats;
import fr.halbrand.skyblock.utils.tools.Colors;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class CustomItemsStats {

    /*************************************
     * Rewritten on 15/08/2024 at 19h00. *
     *************************************/

    private static final Map<Player, Map<EquipmentSlot, ItemStack>> oldItemInArmor = new HashMap<>();
    private static final Map<Player, ItemStack> oldItemInHand = new HashMap<>();

    public static void UpdateHandItemsStats(Player p) {
        ItemStack newItem = p.getInventory().getItemInMainHand();
        ItemStack oldItem = oldItemInHand.get(p);

        if (CustomItemsStats.isSame(oldItem, newItem)) {
            CustomItemsStats.updateItem(p, oldItem, newItem);
            oldItemInHand.put(p, newItem.clone());
        }
    }

    public static void UpdateArmorItemsStats(Player p) {
        Map<EquipmentSlot, ItemStack> playerArmorItem = oldItemInArmor.computeIfAbsent(p, k -> new HashMap<>());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (CustomItemsStats.isArmorSlot(CustomItemsStats.getInventorySlot(slot))) {
                ItemStack newItem = p.getInventory().getItem(slot);
                ItemStack oldItem = playerArmorItem.getOrDefault(slot, new ItemStack(Material.AIR));

                if (CustomItemsStats.isSame(oldItem, newItem)) {
                    CustomItemsStats.updateItem(p, oldItem, newItem);
                    playerArmorItem.put(slot, newItem.clone());
                }
            }
        }

        oldItemInArmor.put(p, playerArmorItem);
    }

    /*
     * Enumération des lores pouvant permettre
     * d'ajouter ou retirer des stats d'un object.
     */

    private static final Map<String, EnumStats> PREFIXES = Map.of(
            "&7Health: &a", EnumStats.MAX_HEALTH,
            "&7Vitality: &a", EnumStats.VITALITY,
            "&7Defence: &a", EnumStats.DEFENSE,
            "&7Mana: &a", EnumStats.MAX_MANA,
            "&7Damage: &c", EnumStats.DAMAGE,
            "&7Strength: &c", EnumStats.STRENGTH,
            "&7Speed: &a", EnumStats.SPEED,
            "&7Farming Fortune: &a", EnumStats.FARMING_FORTUNE,
            "&7Mining Speed: &a", EnumStats.MINING_SPEED
    );

    public static void checkItem(Player p, ItemStack item, boolean remove) {
        PStats stats = new PStats(p);
        Colors color = new Colors(p);

        if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) return;

        ItemMeta meta = item.getItemMeta();

        if (meta.hasEnchant(Enchantment.EFFICIENCY)) {
            int efficiencyLevel = meta.getEnchantLevel(Enchantment.EFFICIENCY);
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
                String valueStr = coloredLore.substring(prefix.length()).trim().replace(",", "");
                int val = Integer.parseInt(valueStr);

                if (remove) {
                    stats.rem(stat, val);
                } else {
                    if (val < 0) {
                        stats.rem(stat, Math.abs(val)); // Remove when negative
                    } else {
                        stats.add(stat, val); // Add when positive
                    }
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