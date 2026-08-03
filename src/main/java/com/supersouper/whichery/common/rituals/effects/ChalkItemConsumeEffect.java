package com.supersouper.whichery.common.rituals.effects;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.api.rituals.IRitualLeader;
import com.supersouper.whichery.api.rituals.RitualEffect;
import com.supersouper.whichery.api.rituals.RunningRitual;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;
import com.supersouper.whichery.common.rituals.matching.ChalkItemSecondaryMatcher;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

public class ChalkItemConsumeEffect extends RitualEffect {

    private int consumed = 0;

    public ChalkItemConsumeEffect(TileEntity leader, RunningRitual currentRitual) {
        super(leader, currentRitual);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void transitionToStage(int stage) {
        int tmp = ChalkItemSecondaryMatcher.class.hashCode();
        if (!currentRitual.getRitual().recipe.secondaryMatchersByClass.containsKey(tmp)) {
            return;
        }
        ArrayList<ISecondaryMatcher> matchers = currentRitual.getRitual().recipe.secondaryMatchersByClass.get(tmp);
        int nextConsumeStage = currentRitual.getRitual().stages.length - (matchers.size() - consumed);
        if (nextConsumeStage < 0) {
            throw new IllegalArgumentException(
                "Ritual '" + currentRitual.getRitual().name
                    + "' requires "
                    + matchers.size()
                    + " items but only has "
                    + currentRitual.getRitual().stages.length
                    + " stages.");
        }
        if (stage != nextConsumeStage) {
            return;
        }

        ChalkItemSecondaryMatcher matcher = (ChalkItemSecondaryMatcher) matchers.get(consumed);
        for (TileEntity te : ((IRitualLeader) currentRitual.leader).getCapturedTileEntities()) {
            if (!(te instanceof ChalkTileEntity cte)) {
                continue;
            }
            if (WhicheryUtils.matchIngredient(matcher.getStack(), cte.getStackInSlot(0), matcher.matchNBT)) {
                cte.decrStackSize(0, matcher.getStack().stackSize);
                break;
            }
        }
    }

    @Override
    public void end(int stage) {

    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("consumed", consumed);
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        consumed = tag.getInteger("consumed");
    }
}
