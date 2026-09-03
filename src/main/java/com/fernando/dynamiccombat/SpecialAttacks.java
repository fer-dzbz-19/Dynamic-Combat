package com.fernando.dynamiccombat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class SpecialAttacks {

    private final Logger logger;
    
    public SpecialAttacks(Logger logger) {
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

    private LivingEntity flurryTarget;


    public void startFlurry(LivingEntity target) {
        flurryActive = true;
        flurryHit = 0;
        flurryAttackTimer = 2;
        flurryTarget = target;

    }

   public void updateFlurry(Player attacker) {

        if (!flurryActive) {
            return;
        }

        if (flurryAttackTimer > 0) {
            flurryAttackTimer--;
        }

        if (flurryAttackTimer == 0) {

            logger.info("FLURRY HIT!");

            flurryHit++;

            if (flurryHit >= 4) {

                flurryActive = false;

                logger.info("FLURRY ENDED!");

            } else {

                flurryAttackTimer = 2;
            }
        }
    }
}