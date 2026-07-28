package com.supersouper.whichery.common.event;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.common.logic.FamilyRegistry;
import com.supersouper.whichery.common.logic.IngredientFamily;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class IngredientEvents {

    // TODO: consider whether we want to move this off tooltips
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Set<IngredientFamily.IngredientFamilyStack> families = FamilyRegistry.getIngredientFamilies(event.itemStack);

        if (families != null) {

            List<IngredientFamily.IngredientFamilyStack> sorted = families.stream()
                .sorted(Comparator.comparing(f -> f.family.ordinal()))
                .collect(Collectors.toList());

            event.toolTip.add("");
            event.toolTip.add(StatCollector.translateToLocal("tooltip.family.families"));
            for (IngredientFamily.IngredientFamilyStack stack : sorted) {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.family." + stack.family.id,
                        StatCollector.translateToLocalFormatted("tooltip.family.potency", stack.amount)));
            }
        }
    }
}
