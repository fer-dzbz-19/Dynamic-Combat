package com.fernando.dynamiccombat;

import org.slf4j.Logger;

public class MaceCombat {

    private final Logger logger;

    public MaceCombat(Logger logger) {
        this.logger = logger;
        }

    public static  boolean checkCombo(ComboManager comboManager, AttackStrength strength) {
        
        DynamicCombat.LOGGER.info(
            "DEBUG MACE -> COMBO: {}, STRENGTH: {}",
            comboManager.getCombo(),
            strength
        );

        if (comboManager.isCombo(1) && strength == AttackStrength.FULL) {
            DynamicCombat.LOGGER.info("MACE COMBO 1 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(2) && strength == AttackStrength.STRONG) {
            DynamicCombat.LOGGER.info("MACE COMBO 2 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(3) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("MACE COMBO 3 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(4) && strength == AttackStrength.FULL) {
            DynamicCombat.LOGGER.info("MACE COMBO 4 CORRETO!");
            return true;
        }

        else {
            DynamicCombat.LOGGER.info("MACE COMBO INCORRETO!");
            comboManager.resetCombo();
            return false;
        }
    }

}