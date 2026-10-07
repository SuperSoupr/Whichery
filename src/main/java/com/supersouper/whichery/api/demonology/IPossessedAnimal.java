package com.supersouper.whichery.api.demonology;

import net.minecraft.entity.EntityLivingBase;

import com.supersouper.whichery.common.entity.demon.EntityDemonicShadow;

public interface IPossessedAnimal extends IPossessedEntity {

    static boolean canBePossessed(EntityLivingBase entityLivingBase) {
        return canBePossessedByDemonicShadow(entityLivingBase, null);
    }

    /**
     * Check whether the supplied entity can be possessed by the supplied demonic shadow (which may already ride the
     * entity).
     */
    static boolean canBePossessedByDemonicShadow(EntityLivingBase entityLivingBase, EntityDemonicShadow demonicShadow) {
        if (entityLivingBase != null) {
            return entityLivingBase.isEntityAlive() && !(entityLivingBase instanceof IDemon)
                && (!(entityLivingBase.riddenByEntity instanceof EntityDemonicShadow shadow) || demonicShadow == shadow)
                && PossessedAnimalRegistry.isPossessedVariantRegisteredForHost(entityLivingBase);
        }

        return false;
    }

}
