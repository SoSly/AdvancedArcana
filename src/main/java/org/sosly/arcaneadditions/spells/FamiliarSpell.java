package org.sosly.arcaneadditions.spells;

import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FamiliarSpell {
    private SpellRecipe recipe;
    private boolean offensive;
    private Frequency frequency;

    public FamiliarSpell(SpellRecipe recipe, Frequency frequency, boolean offensive) {
        this.recipe = recipe;
        this.frequency = frequency;
        this.offensive = offensive;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public SpellRecipe getRecipe() {
        return recipe;
    }

    public boolean isOffensive() {
        return offensive;
    }

    public enum Frequency {
        PURIFIED_VINTEUM(2),
        GOLD(10),
        VINTEUM( 60),
        IRON(180),
        TRANSMUTED_SILVER(300),
        COPPER( 600);

        private final int seconds;
        Frequency(int seconds) {
            this.seconds = seconds;
        }

        public static Frequency ByIngot(ItemStack stack) {
            if (Items.GOLD_INGOT.equals(stack.getItem())) {
                return GOLD;
            } else if (Items.IRON_INGOT.equals(stack.getItem())) {
                return IRON;
            } else if (Items.COPPER_INGOT.equals(stack.getItem())) {
                return COPPER;
            } else if (ItemInit.VINTEUM_INGOT.get().equals(stack.getItem())) {
                return VINTEUM;
            } else if (ItemInit.PURIFIED_VINTEUM_INGOT.get().equals(stack.getItem())) {
                return PURIFIED_VINTEUM;
            } else if (ItemInit.TRANSMUTED_SILVER.get() == stack.getItem()) {
                return TRANSMUTED_SILVER;
            } else {
                return null;
            }
        }

        public int getSeconds() {
            return seconds;
        }
    }
}
