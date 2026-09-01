package com.fernando.dynamiccombat;

public class DamageCalculator {

    public static double calculateDamage(
            WeaponType weaponType,
            int combo,
            AttackStrength strength) {

        double weaponDamage = getWeaponDamage(weaponType);
        double comboMultiplier = getComboMultiplier(combo);
        double strengthMultiplier = getStrengthMultiplier(strength);

        return weaponDamage * comboMultiplier * strengthMultiplier;
    }


    public static double getWeaponDamage(WeaponType weaponType) {

        switch (weaponType) {

            case SWORD:
                return 7.0;

            case AXE:
                return 9.0;

            case MACE:
                return 10.0;

            case TRIDENT:
                return 8.0;

            case BOW:
                return 6.0;

            case CROSSBOW:
                return 7.0;

            default:
                return 1.0;
        }
    }


    public static double getComboMultiplier(int combo) {

        switch (combo) {

            case 1:
                return 1.0;

            case 2:
                return 1.1;

            case 3:
                return 1.25;

            case 4:
                return 1.5;

            default:
                return 1.0;
        }
    }


    public static double getStrengthMultiplier(AttackStrength strength) {

        switch (strength) {

            case WEAK:
                return 0.75;

            case MEDIUM:
                return 1.0;

            case STRONG:
                return 1.15;

            case FULL:
                return 1.25;

            default:
                return 1.0;
        }
    }
}