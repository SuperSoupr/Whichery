package com.supersouper.whichery.api.demonology;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;

import org.apache.commons.lang3.Validate;

import com.supersouper.whichery.common.entity.demon.EntityPossessedChicken;

public final class PossessedAnimalRegistry {

    private static final Map<Class<? extends EntityLivingBase>, PossessedAnimalRegistration<?, ?>> possessedAnimalRegistrationByHostClass = new HashMap<>();

    private PossessedAnimalRegistry() {
        // API class cannot be instantiated
    }

    static {
        register(
            new PossessedAnimalRegistration<>(
                EntityChicken.class,
                EntityPossessedChicken.class,
                EntityPossessedChicken::createPossessedChicken));
    }

    public static void register(PossessedAnimalRegistration<?, ?> registration) {
        Objects.requireNonNull(registration);
        Validate.isTrue(
            !possessedAnimalRegistrationByHostClass.containsKey(registration.animal()),
            "An entry for host type %s already exists",
            registration.animal());

        possessedAnimalRegistrationByHostClass.put(registration.animal(), registration);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <A extends EntityLivingBase> PossessedAnimalRegistration<A, ?> get(Class<A> hostType) {
        return (PossessedAnimalRegistration<A, ?>) possessedAnimalRegistrationByHostClass.get(hostType);
    }

    public static boolean isPossessedVariantRegisteredForHost(EntityLivingBase entity) {
        if (entity != null) {
            return get(entity.getClass()) != null;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <A extends EntityLivingBase> Entity createPossessedEntityFromHost(A entity) {
        PossessedAnimalRegistration<A, ? extends IPossessedAnimal> registration = PossessedAnimalRegistry
            .get((Class<A>) entity.getClass());

        if (registration != null) {
            return registration.createPossessedEntityFunction()
                .apply(entity);
        }

        return null;
    }

}
