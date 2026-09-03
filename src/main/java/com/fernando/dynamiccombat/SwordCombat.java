package com.fernando.dynamiccombat;

import org.slf4j.Logger;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SwordCombat {

    private final Logger logger;

    public SwordCombat(Logger logger) {
        this.logger = logger;
    }


    public boolean checkCombo(ComboManager comboManager, AttackStrength strength) {

        if (comboManager.isCombo(1) && strength == AttackStrength.FULL) {
            logger.info("SWORD COMBO 1 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(2) && strength == AttackStrength.MEDIUM) {
            logger.info("SWORD COMBO 2 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(3) && strength == AttackStrength.MEDIUM) {
            logger.info("SWORD COMBO 3 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(4) && strength == AttackStrength.MEDIUM) {
            logger.info("SWORD COMBO 4 CORRETO!");
            return true;
        }
        else {
            logger.info("SWORD COMBO INCORRETO!");
            comboManager.resetCombo();
            return false;
        }
    }
    
    public void applyFlurry(ComboManager comboManager) {
        if (comboManager.isCombo(4)) {
            logger.info("Flurry effect applied!");
        }
    }

    public void applyEffect(LivingEntity target) {

        target.addEffect(
            new MobEffectInstance(
                MobEffects.GLOWING,
                100,
                0
            )
        );
    }


}
