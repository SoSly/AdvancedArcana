package org.sosly.arcaneadditions.spells;

import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FamiliarSpell {
    private Component name;
    private SpellRecipe recipe;
    private boolean offensive;
    private Frequency frequency;
    private long lastCast;

    public FamiliarSpell(Component name, SpellRecipe recipe, Frequency frequency, boolean offensive) {
        this.name = name;
        this.recipe = recipe;
        this.frequency = frequency;
        this.offensive = offensive;
        this.lastCast = -1;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public long getLastCast() {
        return lastCast;
    }

    public void setLastCast(long lastCast) {
        this.lastCast = lastCast;
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public SpellRecipe getRecipe() {
        return recipe;
    }

    public boolean isOffensive() {
        return offensive;
    }

    public enum Frequency {
        SUPERHEATED_PURIFIED_VINTEUM(2),
        PURIFIED_VINTEUM(10),
        GOLD(30),
        VINTEUM( 60),
        IRON(180),
        TRANSMUTED_SILVER(300),
        COPPER( 600);

        private final int seconds;
        Frequency(int seconds) {
            this.seconds = seconds;
        }

        public static Frequency ByIngot(ItemStack stack) {
            if (ItemInit.VINTEUM_INGOT_PURIFIED_SUPERHEATED.equals(stack.getItem())) {
                return SUPERHEATED_PURIFIED_VINTEUM;
            } else if (Items.GOLD_INGOT.equals(stack.getItem())) {
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
