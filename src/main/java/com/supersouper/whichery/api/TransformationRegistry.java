package com.supersouper.whichery.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.supersouper.whichery.Whichery;

public class TransformationRegistry {

    private static final Map<String, ITransformation> TRANSFORMATION_MAP = new HashMap<>();

    public static void init() {
        registerTransformation(new BatTransformation());
    }

    @Nullable
    public static ITransformation getTransformation(String id) {
        return TRANSFORMATION_MAP.get(id);
    }

    public static void registerTransformation(@Nonnull ITransformation transformation) {
        String id = transformation.getId();
        if (TRANSFORMATION_MAP.containsKey(id)) {
            Whichery.LOG.error(
                "Transformation ID {} is already taken. It is recommended to prefix transformation IDs with your mod ID.",
                id);
        }
        TRANSFORMATION_MAP.put(transformation.getId(), transformation);
    }
}
