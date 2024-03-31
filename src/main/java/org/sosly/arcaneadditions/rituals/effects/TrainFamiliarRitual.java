package org.sosly.arcaneadditions.rituals.effects;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.sosly.arcaneadditions.capabilities.familiar.IFamiliarCapability;
import org.sosly.arcaneadditions.spells.FamiliarSpell;
import org.sosly.arcaneadditions.utils.FamiliarHelper;

public class TrainFamiliarRitual extends RitualEffect {
    public TrainFamiliarRitual(ResourceLocation name) {
        super(name);
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        if (context.getCaster() == null) {
            return Component.literal("No player reference found for ritual, aborting.");
        }
        IPlayerProgression p = context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (p == null || p.getTier() < 3) {
            return Component.literal("You must be at least tier 3 to train a familiar.");
        }
        IFamiliarCapability f = FamiliarHelper.getFamiliarCapability(context.getCaster());
        if (f == null || f.getType() == null) {
            return Component.literal("You must have a familiar to train.");
        }
        return null;
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player player = context.getCaster();
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }

        // Get the familiar capability
        IFamiliarCapability familiar = FamiliarHelper.getFamiliarCapability(player);
        if (familiar == null) {
            return false;
        }

        // Get the spell frequency
        ItemStack ingotItem = context.getCollectedReagents().get(1);
        FamiliarSpell.Frequency frequency = FamiliarSpell.Frequency.ByIngot(ingotItem);
        if (frequency == null) {
            return false;
        }

        // Determine if the spell is offensive
        ItemStack reagent = context.getCollectedReagents().get(3);
        boolean offensive = Items.GUNPOWDER.equals(reagent.getItem());

        // Get the spell recipe to teach the familiar
        ItemStack recipeItem = context.getCollectedReagents().get(6);
        if (!ItemInit.SPELL.get().equals(recipeItem.getItem())) {
            return false;
        }
        SpellRecipe recipe = SpellRecipe.fromNBT(recipeItem.getOrCreateTag());
        if (recipe == null) {
            return false;
        }

        Component name = recipeItem.getDisplayName();

        // Create the familiar spell
        FamiliarSpell spell = new FamiliarSpell(name, recipe, frequency, offensive);
        familiar.addSpellKnown(spell, true);

        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 20;
    }
}
