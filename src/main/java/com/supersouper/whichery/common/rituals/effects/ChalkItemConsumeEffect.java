package com.supersouper.whichery.common.rituals.effects;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.api.rituals.RitualEffect;
import com.supersouper.whichery.api.rituals.RunningRitual;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;
import com.supersouper.whichery.common.rituals.matching.ChalkItemSecondaryMatcher;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;
import com.supersouper.whichery.utils.ArrayUtils;
import com.supersouper.whichery.utils.WhicheryUtils;

public class ChalkItemConsumeEffect extends RitualEffect {

    private int consumed = 0;
    private int[] indices;

    public ChalkItemConsumeEffect(TileEntity leader, RunningRitual currentRitual) {
        super(leader, currentRitual);
        int tmp = ChalkItemSecondaryMatcher.class.hashCode();
        int matcherCount;
        if (currentRitual.getRitual().recipe.secondaryMatchersByClass.containsKey(tmp)) {
            matcherCount = currentRitual.getRitual().recipe.secondaryMatchersByClass.get(tmp)
                .size();
        } else {
            matcherCount = 0;
        }
        indices = new int[matcherCount];
        Arrays.fill(indices, -1);
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
        // int nextConsumeStage = currentRitual.getRitual().stages.length - (matchers.size() - consumed);
        int nextConsumeStage = consumed;
        if (stage != nextConsumeStage || matchers.size() <= consumed) {
            return;
        }

        ChalkItemSecondaryMatcher matcher = (ChalkItemSecondaryMatcher) matchers.get(consumed);
        int i = 0;
        for (TileEntity te : currentRitual.getCapturedTileEntities()) {
            if (!(te instanceof ChalkRuneTileEntity cte)) {
                i++;
                continue;
            }
            if (WhicheryUtils.matchIngredient(matcher.stack, cte.getStackInSlot(1), matcher.matchNBT)) {
                cte.decrStackSize(1, matcher.stack.stackSize);
                if (!matcher.onlyPlaceOnComplete) {
                    placeResult(cte, matcher);
                }
                indices[consumed] = i;
                consumed++;
                break;
            }

            i++;
        }
    }

    private void placeResult(ChalkRuneTileEntity cte, ChalkItemSecondaryMatcher matcher) {
        if (cte.hasStorageUpgrade && cte.getStackInSlot(0) == null) {
            cte.setInventorySlotContents(0, matcher.resultStack);
        } else if (cte.getStackInSlot(1) == null) {
            cte.setInventorySlotContents(1, matcher.resultStack);
        } else {
            if (!cte.getWorldObj().isRemote) {
                EntityItem entityItem = new EntityItem(
                    cte.getWorldObj(),
                    cte.xCoord + 0.5,
                    cte.yCoord + 0.6,
                    cte.zCoord + 0.5,
                    matcher.resultStack.copy());
                cte.getWorldObj()
                    .spawnEntityInWorld(entityItem);
                entityItem.delayBeforeCanPickup = 5;
            }
        }
    }

    @Override
    public void complete(int stage) {
        if (currentRitual.leader.getWorldObj().isRemote) return;

        int tmp = ChalkItemSecondaryMatcher.class.hashCode();
        if (!currentRitual.getRitual().recipe.secondaryMatchersByClass.containsKey(tmp)) {
            return;
        }
        ArrayList<ISecondaryMatcher> matchers = currentRitual.getRitual().recipe.secondaryMatchersByClass.get(tmp);
        for (int i = 0; i < matchers.size(); i++) {
            if (matchers.get(i)
                .getClass() != ChalkItemSecondaryMatcher.class) continue;
            ChalkItemSecondaryMatcher matcher = (ChalkItemSecondaryMatcher) matchers.get(i);

            if (matcher.onlyPlaceOnComplete) {
                placeResult(
                    (ChalkRuneTileEntity) currentRitual.getCapturedTileEntities()
                        .get(indices[i]),
                    matcher);
            }
        }
    }

    @Override
    public void end(int stage) {

    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("consumed", consumed);
        tag.setByteArray("indices", ArrayUtils.intArrayToByteArray(indices));
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        consumed = tag.getInteger("consumed");
        indices = ArrayUtils.byteArrayToIntArray(tag.getByteArray("indices"));;
    }
}
