package com.supersouper.whichery.api.demonology;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record PossessedAnimalRegistration<A extends EntityLivingBase, P extends Entity & IPossessedAnimal> (
    Class<A> animal, Class<P> possessedAnimal, Function<A, P> createPossessedEntityFunction) {}
