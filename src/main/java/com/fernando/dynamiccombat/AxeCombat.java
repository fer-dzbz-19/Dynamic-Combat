package com.fernando.dynamiccombat;

import org.slf4j.Logger;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AxeCombat {
    private final Logger logger;

    public AxeCombat(Logger logger) {
        this.logger = logger;
    }

    public void applySpecialAttack(
        Player attacker,
        LivingEntity target,
        ComboManager comboManager,
        boolean validCombo) {

    if (comboManager.getCombo() == 4 && validCombo) {

        logger.info("================================");
        logger.info("AXE HEAVY ATTACK!");
        logger.info("AXE SPECIAL ATTACK ACTIVATED!");

        // Aqui ficará o comportamento do golpe especial
    }
}


    public static boolean checkCombo(
            ComboManager comboManager,
            AttackStrength strength) {

        DynamicCombat.LOGGER.info(
            "DEBUG AXE -> COMBO: {}, STRENGTH: {}",
            comboManager.getCombo(),
            strength
        );
        
        if (comboManager.isCombo(1) && strength == AttackStrength.FULL) {
            DynamicCombat.LOGGER.info("AXE COMBO 1 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(2) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("AXE COMBO 2 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(3) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("AXE COMBO 3 CORRETO!");
            return true;
        }

        else if (comboManager.isCombo(4) && strength == AttackStrength.MEDIUM) {
            DynamicCombat.LOGGER.info("AXE COMBO 4 CORRETO!");
            return true;
        }

        else {
            DynamicCombat.LOGGER.info("AXE COMBO INCORRETO!");
            comboManager.resetCombo();
            return false;
        }
    }

    public static void heavyAttack(
            Player attacker,
            LivingEntity target) {

        DamageSource source =
                target.damageSources().playerAttack(attacker);

        DynamicCombat.LOGGER.info("================================");
        DynamicCombat.LOGGER.info("AXE HEAVY ATTACK!");
        DynamicCombat.LOGGER.info("AXE SPECIAL ATTACK ACTIVATED!");

        target.hurt(source, 5.0F);

        target.knockback(
                1.5,
                attacker.getX() - target.getX(),
                attacker.getZ() - target.getZ()
        );
    }
}