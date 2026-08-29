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

    public void nextCombo() {
        combo++;

        if (combo > 4) {
            combo = 1;
        }

        comboTimer = 20;
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
}
