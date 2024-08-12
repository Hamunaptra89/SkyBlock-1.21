package fr.halbrand.skyblock.addons.custom.mecanics.lores;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import java.util.*;

public class ItemsManager {

    /*************************************
     * Rewritten on 16/06/2024 at 16h00. *
     *************************************/

    private static final Map<Player, Map<EquipmentSlot, ItemStack>> oldItemInArmor = new HashMap<>();
    private static final Map<Player, ItemStack> oldItemInHand = new HashMap<>();

    public static void UpdateHandItemsStats(Player p) {
        ItemStack newItem = p.getInventory().getItemInMainHand();
        ItemStack oldItem = oldItemInHand.get(p);

        if (LoreManager.isSame(oldItem, newItem)) {
            LoreManager.updateItem(p, oldItem, newItem);
            oldItemInHand.put(p, newItem.clone());
        }
    }

    public static void UpdateArmorItemsStats(Player p) {
        Map<EquipmentSlot, ItemStack> playerArmorItem = oldItemInArmor.computeIfAbsent(p, k -> new HashMap<>());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (LoreManager.isArmorSlot(LoreManager.getInventorySlot(slot))) {
                ItemStack newItem = p.getInventory().getItem(slot);
                ItemStack oldItem = playerArmorItem.getOrDefault(slot, new ItemStack(Material.AIR));

                if (LoreManager.isSame(oldItem, newItem)) {
                    LoreManager.updateItem(p, oldItem, newItem);
                    playerArmorItem.put(slot, newItem.clone());
                }
            }
        }

        oldItemInArmor.put(p, playerArmorItem);
    }
}
