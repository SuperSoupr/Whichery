package com.supersouper.whichery.common.entity.demon;

import net.minecraft.entity.EntityLiving;

public interface IPossessedEntity extends IDemon {

    /**
     * A dummy entity representing a healthy, unpossessed host. It may be used in places where the game hides the
     * information
     * that the player deals with a possessed entity.
     */
    EntityLiving createUnpossessedDummyHostEntity();

    void setStaringAtAwarePlayer(boolean value);

    boolean isStaringAtAwarePlayer();

}
