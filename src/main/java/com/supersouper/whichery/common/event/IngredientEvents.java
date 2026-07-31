package com.supersouper.whichery.common.event;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.api.ingredientfamilies.FamilyRegistry;
import com.supersouper.whichery.api.ingredientfamilies.IngredientFamily;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class IngredientEvents {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Set<IngredientFamily.FamilyStack> families = FamilyRegistry.getIngredientFamilies(event.itemStack);

        if (families != null) {
            List<IngredientFamily.FamilyStack> sorted = families.stream()
                .sorted(Comparator.comparing(f -> f.family.getOrder()))
                .collect(Collectors.toList());

            event.toolTip.add("");
            event.toolTip.add(StatCollector.translateToLocal("tooltip.family.families"));
            for (IngredientFamily.FamilyStack stack : sorted) {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.family." + stack.family.getId(),
                        StatCollector.translateToLocalFormatted("tooltip.family.potency", stack.amount)));
            }
        }
    }
}
