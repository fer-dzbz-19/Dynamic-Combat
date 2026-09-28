package com.fernando.dynamiccombat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class MaceSpecialAttack {

    private final Logger logger;

    private boolean crushActive = false;

    private int crushTimer = 0;

    private LivingEntity crushTarget;

    public MaceSpecialAttack(Logger logger) {
        this.logger = logger;
    }
    
    public void startCrush(Player attacker, LivingEntity target) {
        crushActive = true;
        crushTarget = target;
        crushTimer = 0;

        logger.info("CRUSH IS EXECUTING!");
    }

    public void updateCrush(Player attacker, double attackDamage) {
        if (!crushActive) {
            return;
        }
        
        if (crushTarget == null || !crushTarget.isAlive()) {
            crushActive = false;
            return;
        }

        crushTimer++;

        if (crushTimer <= 10) {
            //PREPARAÇÃO
        }

        else if (crushTimer == 11) {
            //IMPACTO

            double damage = DamageCalculator.calculateCrushDamage();

            DamageSource damageSource =
                attacker.damageSources().playerAttack(attacker);
            crushTarget.hurt(damageSource, (float) damage);

            logger.info("CRUSH IMPACT !");

        }

        else if (crushTimer <= 15) {
            //FINALIZAÇÃO
        }

        if (crushTimer >= 15) {
            crushActive = false;
            crushTarget = null;
            crushTimer = 0;
            return;
        }
    }

    public boolean isMaceSpecialAttackActive() {
        return crushActive;
    }
}
