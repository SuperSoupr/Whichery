package com.supersouper.whichery.api.ingredientfamilies;

import javax.annotation.Nonnull;

public class IngredientFamily {

    private final String id;
    private IngredientFamily opposite;
    private int order;

    IngredientFamily(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    /**
     * Control priority order this will display in tooltips or NEI handlers. Lower numbers display first. By default,
     * order will be set to (1000 + the order of the previous family) in order to leave space for precise ordering.
     * This means the default ordering is as follows:
     * <ul>
     * <li>Fire (1000)</li>
     * <li>Water (2000)</li>
     * <li>Air (3000)</li>
     * <li>Earth (4000)</li>
     * <li>Life (5000)</li>
     * <li>Death (6000)</li>
     * <li>Spirit (7000)</li>
     * <li>Hell (8000)</li>
     * </ul>
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Nonnull
    public IngredientFamily getOpposite() {
        return opposite;
    }

    void setOpposite(IngredientFamily opposite) {
        this.opposite = opposite;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IngredientFamily && ((IngredientFamily) o).id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final class FamilyStack {

        public final IngredientFamily family;
        public final int amount;

        public FamilyStack(IngredientFamily family, int amount) {
            this.family = family;
            this.amount = amount;
        }
    }
}
