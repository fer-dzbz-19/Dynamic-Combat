package com.fernando.dynamiccombat;

import org.slf4j.Logger;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class MaceCombat {

    private final Logger logger;

    public MaceCombat(Logger logger) {
        this.logger = logger;
        }

    public boolean checkCombo(ComboManager comboManager, AttackStrength strength) {

        if (comboManager.isCombo(1) && strength == AttackStrength.FULL) {
            logger.info("MACE COMBO 1 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(2) && strength == AttackStrength.MEDIUM) {
            logger.info("MACE COMBO 2 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(3) && strength == AttackStrength.MEDIUM) {
            logger.info("MACE COMBO 3 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(4) && strength == AttackStrength.MEDIUM) {
            logger.info("MACE COMBO 4 CORRETO!");
            return true;
        }

        else {
            logger.info("MACE COMBO INCORRETO!");
            comboManager.resetCombo();
            return false;
        }
    }
    public void applyEffect(LivingEntity target) {

        target.addEffect(
            new MobEffectInstance(
                MobEffects.WEAKNESS,
                100,
                0
            )
        );
    }
}