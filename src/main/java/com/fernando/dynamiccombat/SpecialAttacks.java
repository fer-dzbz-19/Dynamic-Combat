package com.fernando.dynamiccombat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class SpecialAttacks {

    private final Logger logger;
    
    private final SwordSpecialAttack swordSpecialAttack;

    private final AxeSpecialAttack axeSpecialAttack;

    private final TridentSpecialAttack tridentSpecialAttack;

    private final MaceSpecialAttack maceSpecialAttack;

    public SpecialAttacks(Logger logger) {
        this.logger = logger;
        this.swordSpecialAttack = new SwordSpecialAttack(logger);
        this.axeSpecialAttack = new AxeSpecialAttack(logger);
        this.tridentSpecialAttack = new TridentSpecialAttack(logger);
        this.maceSpecialAttack = new MaceSpecialAttack(logger);
    }

    public void startSpecialAttack(
        WeaponType weaponType,
        Player attacker,
        LivingEntity target) {

            if (weaponType == WeaponType.SWORD) {
                swordSpecialAttack.startFlurry(target);
            }
            
            else if (weaponType == WeaponType.AXE) {
                axeSpecialAttack.startHeavyStrike(attacker, target);
            }
            
            else if (weaponType == WeaponType.TRIDENT) {
                tridentSpecialAttack.startRisingImpale(target);
            }

            else if (weaponType == WeaponType.MACE) {
                maceSpecialAttack.startCrush(attacker, target);
            }
        }

    public boolean isSpecialAttackActive() {
        return swordSpecialAttack.isFlurryActive() 
        || axeSpecialAttack.isAxeSpecialAttackActive() 
        || tridentSpecialAttack.isRisingImpaleActive()
        || maceSpecialAttack.isMaceSpecialAttackActive();
    }

    public void updateSpecialAttack(Player attacker, double attackDamage) {
        swordSpecialAttack.updateFlurry(attacker, attackDamage);
        tridentSpecialAttack.updateRisingImpale(attacker, attackDamage);
        axeSpecialAttack.updateHeavyStrike(attacker, attackDamage);
        maceSpecialAttack.updateCrush(attacker, attackDamage);
    }
}