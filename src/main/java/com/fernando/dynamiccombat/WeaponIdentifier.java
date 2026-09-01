package com.fernando.dynamiccombat;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WeaponIdentifier {

    public static WeaponType identify(ItemStack weapon) {

        if (weapon.is(Items.WOODEN_SWORD) ||
            weapon.is(Items.STONE_SWORD) ||
            weapon.is(Items.IRON_SWORD) ||
            weapon.is(Items.DIAMOND_SWORD) ||
            weapon.is(Items.NETHERITE_SWORD)) {

            return WeaponType.SWORD;
        }

        if (weapon.is(Items.WOODEN_AXE) ||
            weapon.is(Items.STONE_AXE) ||
            weapon.is(Items.IRON_AXE) ||
            weapon.is(Items.DIAMOND_AXE) ||
            weapon.is(Items.NETHERITE_AXE)) {

            return WeaponType.AXE;
        }

        if (weapon.is(Items.BOW)) {
            return WeaponType.BOW;
        }

        if (weapon.is(Items.CROSSBOW)) {
            return WeaponType.CROSSBOW;
        }

        if (weapon.is(Items.TRIDENT)) {
            return WeaponType.TRIDENT;
        }

        if (weapon.is(Items.MACE)) {
            return WeaponType.MACE;
        }

        return WeaponType.UNKNOWN;
    }
}