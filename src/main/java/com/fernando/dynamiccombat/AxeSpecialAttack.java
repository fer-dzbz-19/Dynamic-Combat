package com.fernando.dynamiccombat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class AxeSpecialAttack {

    private final Logger logger;

    private boolean heavyStrikeActive = false;

    private int heavyStrikeTimer = 0;

    private LivingEntity heavyStrikeTarget;

    public AxeSpecialAttack(Logger logger) {
        this.logger = logger;
    }

    public void startHeavyStrike(Player attacker, LivingEntity target) {

        heavyStrikeActive = true;
        heavyStrikeTimer = 0;
        heavyStrikeTarget = target;

        logger.info("HEAVY STRIKE EXECUTING!");
    }

    public void updateHeavyStrike(Player attacker, double attackDamage) {
        
        if (!heavyStrikeActive) {
            return;     
        }

        if (heavyStrikeTarget == null || !heavyStrikeTarget.isAlive() ) {
            heavyStrikeActive = false;
            return;
        }

        heavyStrikeTimer++;

        if (heavyStrikeTimer <= 5) {
            //PREPARAÇÃO
        }

        else if (heavyStrikeTimer == 6) {
            // IMPACTO

            double damage = DamageCalculator.calculateHeavyStrikeDamage();

            DamageSource damageSource =
                attacker.damageSources().playerAttack(attacker);

            heavyStrikeTarget.hurt(damageSource, (float) damage);

            logger.info("HEAVY STRIKE IMPACT!");
        }
        
        else if (heavyStrikeTimer <= 10) {
            //FINALIZAÇÃO
        }

        if (heavyStrikeTimer >= 10) {
        
            heavyStrikeActive = false;
            heavyStrikeTimer = 0;
            heavyStrikeTarget = null;
            return;

        }
    }

    public boolean isAxeSpecialAttackActive() {
        return heavyStrikeActive;
    }
}