package com.fernando.dynamiccombat;

public class ComboManager {

    private int combo = 0;
    private int comboTimer = 0;

    public int getCombo() {
        return combo;
    }

    public int getComboTimer() {
        return comboTimer;
    }

    public void nextCombo(WeaponType weaponType) {
        combo++;

        if (combo > 4) {
            combo = 1;
        }

        comboTimer = getComboTime(weaponType, combo);
    }

    public void tick() {

        if (comboTimer > 0) {
            comboTimer--;

            if (comboTimer == 0) {
                resetCombo();
            }
        }
    }

    public void resetCombo() {
        combo = 0;
        comboTimer = 0;
    }

    public boolean isCombo(int number) {
        return combo == number;
    }

    public int getComboTime(WeaponType weaponType, int combo) {

        switch (weaponType) {
            case SWORD:
                switch (combo) {
                    case 1:
                        return 20;

                    case 2:
                        return 25;

                    case 3:
                        return 30;

                    case 4:
                        return 35;

                    default:
                        return 25;
                }

            case AXE:
                switch (combo) {
                    case 1:
                        return 30;

                    case 2:
                        return 35;

                    case 3:
                        return 40;

                    case 4:
                        return 45;

                    default:
                        return 35;
                }

            case MACE:

                switch (combo) {
                    case 1:
                        return 35;

                    case 2:
                        return 40;

                    case 3:
                        return 45;

                    case 4:
                        return 50;

                    default:
                        return 40;
                }
            
            case TRIDENT:
                switch (combo) {
                    case 1:
                        return 30;

                    case 2:
                        return 35;

                    case 3:
                        return 40;

                    case 4:
                        return 45;

                    default:
                        return 30;
                }
                
            default:
                return 20;
        }
    }
}
