package com.fernando.dynamiccombat;

import org.slf4j.Logger;

public class SwordCombat {

    private final Logger logger;

    public SwordCombat(Logger logger) {
        this.logger = logger;
    }


    public static boolean checkCombo(ComboManager comboManager, AttackStrength strength) {

        if (comboManager.isCombo(1) && strength == AttackStrength.FULL) {
            DynamicCombat.LOGGER.info("SWORD COMBO 1 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(2) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("SWORD COMBO 2 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(3) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("SWORD COMBO 3 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(4) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("SWORD COMBO 4 CORRETO!");
            return true;
        }
        else {
            DynamicCombat.LOGGER.info("SWORD COMBO INCORRETO!");
            comboManager.resetCombo();
            return false;
        }
    }
    
}
