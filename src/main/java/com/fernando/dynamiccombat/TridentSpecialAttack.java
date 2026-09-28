package com.fernando.dynamiccombat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import net.minecraft.world.phys.Vec3;

public class TridentSpecialAttack {
    private final Logger logger;

    public TridentSpecialAttack(Logger logger) {
        this.logger = logger;
    }

    private boolean risingimpaleAttackActive = false;

    private int risingimpaleAttackTimer = 0;

    private LivingEntity risingimpaleTarget;

    public void startRisingImpale(LivingEntity target) {
        risingimpaleAttackActive = true;
        risingimpaleAttackTimer = 0;
        risingimpaleTarget = target;
    }

    public void updateRisingImpale(Player attacker, double attackDamage) {
        if (!risingimpaleAttackActive) {
            return;
        }

        if (risingimpaleTarget == null || !risingimpaleTarget.isAlive()) {
            risingimpaleAttackActive = false;
            return;
        }

        risingimpaleAttackTimer++;

        if (risingimpaleAttackTimer <= 10) {
            //PREPARAÇÃO
        }
        
        else if (risingimpaleAttackTimer <= 15) {
            //ESTOCADA
            
            if (risingimpaleAttackTimer == 15 && risingimpaleTarget != null && risingimpaleTarget.isAlive() ) {
                logger.info("Trident Special Attack Rising Impale has triggered!");
                
                DamageSource damageSource = attacker.damageSources().playerAttack(attacker);
                
                double damage = DamageCalculator.calculateRisingImpaleDamage();
            
                risingimpaleTarget.hurt(damageSource, (float) damage);
            }
        
        }
        
        else if (risingimpaleAttackTimer <= 17) {
            // LEVANTAR

            Vec3 movement = risingimpaleTarget.getDeltaMovement();

            risingimpaleTarget.setDeltaMovement(
                movement.x,
                0.5,
                movement.z
            );
 
        }
        
        else if (risingimpaleAttackTimer <= 22) {
            // MANTER LEVANTADI
            Vec3 movement = risingimpaleTarget.getDeltaMovement();

            risingimpaleTarget.setDeltaMovement(
                movement.x,
                0.0,
                movement.z
            );
        
        }

        else if (risingimpaleAttackTimer <= 27) {
            //DERRUBAR
            Vec3 movement = risingimpaleTarget.getDeltaMovement();

            risingimpaleTarget.setDeltaMovement(
                movement.x,
                -1.0,
                movement.z
            );
        }

        if (risingimpaleAttackTimer >= 27) {
            risingimpaleAttackActive = false;
            risingimpaleAttackTimer = 0;
            risingimpaleTarget = null;
            return;
        }

    }

    public boolean isRisingImpaleActive() {
        return risingimpaleAttackActive ;
    }
}
