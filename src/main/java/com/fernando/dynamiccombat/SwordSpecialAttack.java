package com.fernando.dynamiccombat;

import org.slf4j.Logger;    
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SwordSpecialAttack {

    private final Logger logger;
    
    public SwordSpecialAttack(Logger logger) {
        this.logger = logger;
    }

    // FLURRY - primeiro ataque especial do Dynamic Combat

    // O Flurry é ativado quando se completa o 4º combo
    // de ataques consecutivos com a espada.

    // O Flurry é uma sequência de ataques rápidos,
    // composta por vários golpes de baixo dano.

    private boolean flurryActive = false;

    private int flurryAttackTimer = 2;

    private int flurryHit = 0;

    private double flurryTotalDamage = 0;

    private LivingEntity flurryTarget;


    public void startFlurry(LivingEntity target) {
        flurryActive = true;
        flurryHit = 0;
        flurryTotalDamage = 0;
        flurryAttackTimer = 0;
        flurryTarget = target;

    }

   public void updateFlurry(Player attacker, double attackDamage) {

        if (!flurryActive) {
            return;
        }

        if (flurryTarget == null || !flurryTarget.isAlive()) {
            flurryActive = false;
            return;
        }

        if (flurryAttackTimer > 0) {
            flurryAttackTimer--;
        }

        if (flurryAttackTimer == 0) {

            logger.info("FLURRY HIT!");

            flurryHit++;
            if (flurryTarget != null && flurryTarget.isAlive() ) {
                logger.info("FLURRY TARGET: {}", flurryTarget.getName().getString());

                DamageSource damageSource = attacker.damageSources().playerAttack(attacker);

                logger.info("FLURRY DAMAGE SOURCE CREATED!");

                double damage = DamageCalculator.calculateFlurryDamage();

                flurryTotalDamage += damage;

                flurryTarget.hurt(damageSource, (float) damage);
            }

            if (flurryHit >= 4) {

                flurryActive = false;

                double allDamage = attackDamage + flurryTotalDamage;

                logger.info("FLURRY TOTAL DAMAGE: {}", flurryTotalDamage);

                logger.info("ALL DAMAGE: {}", allDamage);

                logger.info("FLURRY ENDED!");

                flurryTarget = null;
                flurryTotalDamage = 0;
                flurryAttackTimer = 0;

            } else {

                flurryAttackTimer = 2;
            }
        }
    }

    public double getFlurryTotalDamage() {
        return flurryTotalDamage;
    }

    public boolean isFlurryActive() {
        return flurryActive;
    }
}
