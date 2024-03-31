package org.sosly.arcaneadditions.entities.ai;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.spells.SpellsInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import org.sosly.arcaneadditions.ArcaneAdditions;
import org.sosly.arcaneadditions.capabilities.familiar.IFamiliarCapability;
import org.sosly.arcaneadditions.spells.FamiliarSpell;
import org.sosly.arcaneadditions.spells.SpellsRegistry;
import org.sosly.arcaneadditions.utils.FamiliarHelper;

import java.util.EnumSet;
import java.util.Optional;

public class CastUtilitySpell extends Goal {
    private final Mob familiar;
    private final float maxCastDistance;
    private boolean hasCast;
    private boolean cannotCast;
    private long lastAttempt;
    private long lastCast;
    private Optional<FamiliarSpell> spellToCast = Optional.empty();
    private LivingEntity target;
    private int seeTime;
    private boolean strafingBackwards;
    private boolean strafingClockwise;
    private int strafingTime;

    public CastUtilitySpell(Mob familiar, float maxCastDistance) {
        this.lastAttempt = -1;
        this.lastCast = -1;
        this.strafingTime = -1;
        this.hasCast = false;
        this.familiar = familiar;
        this.maxCastDistance = maxCastDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        IFamiliarCapability cap = FamiliarHelper.getFamiliarCapability(familiar);
        if (cap == null || cap.isOrderedToStay() || cap.getSpellsKnown().isEmpty()) {
            return false;
        }
        long sinceLastCast = familiar.getServer().overworld().getGameTime() - lastCast;
        long sinceLastAttempt = familiar.getServer().overworld().getGameTime() - lastAttempt;
        if (sinceLastCast < 20L || sinceLastAttempt < 20L) {
            return false;
        }
        lastAttempt = familiar.getServer().overworld().getGameTime();
        ArcaneAdditions.LOGGER.error("sinceLastCast: " + sinceLastCast + ", sinceLastAttempt: " + sinceLastAttempt + ", spells known: " + cap.getSpellsKnown().size());
        // todo: sinceLastCast should be per spell
        // todo: possibly we should have a CastUtilitySpell Goal for each spell, with the spell passed in at creation
        //       this would allow us to have a separate lastCast for each spell but would require us to remove the goal
        //       when the spell is unlearned.
        spellToCast = cap.getSpellsKnown().stream()
                .filter(spell -> !spell.isOffensive())
                .filter(spell -> {
                    int possibility = (int) Math.max(spell.getFrequency().getSeconds() - ((sinceLastCast - 20L) / 40), 1L);
                    int random = familiar.getServer().overworld().getRandom().nextInt(possibility);
                    ArcaneAdditions.LOGGER.error("possibility: " + possibility + ", ticks: " + spell.getFrequency().getSeconds() + ", random: " + random);
                    return random == 0;
                })
                .findAny();
        return spellToCast.isPresent();
    }

    public boolean canContinueToUse() {
        return !this.hasCast && !this.cannotCast;
    }


    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        super.start();

        hasCast = false;
        FamiliarSpell spell = spellToCast.get();
        if (spell.getRecipe().getShape() == null) {
            return;
        }
        if (spell.getRecipe().getShape().equals(SpellsRegistry.FAMILIAR)
            || spell.getRecipe().getShape().equals(SpellsRegistry.SHARED)
            || spell.getRecipe().getShape().equals(SpellsInit.SELF)) {

            target = familiar;
        } else {
            IFamiliarCapability cap = FamiliarHelper.getFamiliarCapability(familiar);
            if (cap == null || cap.isOrderedToStay()) {
                return;
            }
            target = cap.getCaster();
        }

        familiar.setItemInHand(InteractionHand.MAIN_HAND, spell.getRecipe().createAsSpell());
    }

    @Override
    public void stop() {
        super.stop();
        this.hasCast = false;
        this.cannotCast = false;
        familiar.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        familiar.stopUsingItem();
    }

    @Override
    public void tick() {
        if (!this.canContinueToUse()) {
            this.stop();
            return;
        }

        IFamiliarCapability cap = FamiliarHelper.getFamiliarCapability(familiar);
        if (cap == null || cap.isOrderedToStay()) {
            this.cannotCast = true;
            return;
        }

        float mana = spellToCast.get().getRecipe().getManaCost();
        if (cap.getCastingResource().getAmount() < mana + 10) {
            this.cannotCast = true;
            return;
        }

        if (target.equals(familiar)) {
            SpellTarget target = new SpellTarget(familiar);
            ManaAndArtificeMod.getSpellHelper()
                    .affect(familiar.getItemInHand(InteractionHand.MAIN_HAND), spellToCast.get().getRecipe(),
                            familiar.level(), new SpellSource(familiar, InteractionHand.MAIN_HAND), target);
            this.hasCast = true;
            this.lastCast = (int) familiar.getServer().overworld().getGameTime();
            return;
        }

        if (target == null || target.isRemoved()) {
            this.cannotCast = true;
            return;
        }

        double distanceToTarget = familiar.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean canSee = familiar.getSensing().hasLineOfSight(target);
        boolean positiveSeeTime = this.seeTime > 0;
        if (canSee != positiveSeeTime) {
            this.seeTime = 0;
        }

        if (canSee) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (!(distanceToTarget > maxCastDistance) && seeTime >= 20) {
            familiar.getNavigation().stop();
            ++this.strafingTime;
        } else {
            familiar.getNavigation().moveTo(target, 1.0D);
            this.strafingTime = -1;
        }

        if (this.strafingTime >= 20) {
            if ((double)familiar.getRandom().nextFloat() < 0.3D) {
                strafingClockwise = !strafingClockwise;
            }

            if ((double)familiar.getRandom().nextFloat() < 0.3D) {
                strafingBackwards = !strafingBackwards;
            }

            strafingTime = 0;
        }

        if (strafingTime >= -1) {
            if (distanceToTarget > maxCastDistance * 0.75) {
                strafingBackwards = false;
            } else if (distanceToTarget < maxCastDistance * 0.25) {
                strafingBackwards = true;
            }

            familiar.getMoveControl().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
            familiar.lookAt(target, 30.0F, 30.0F);
        } else {
            familiar.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }

        if (distanceToTarget <= maxCastDistance) {
            familiar.getNavigation().stop();
            familiar.getLookControl().setLookAt(target, 30.0F, 30.0F);
            ManaAndArtificeMod.getSpellHelper()
                    .affect(familiar.getItemInHand(InteractionHand.MAIN_HAND), spellToCast.get().getRecipe(),
                            familiar.level(), new SpellSource(familiar, InteractionHand.MAIN_HAND), new SpellTarget(target));
            cap.getCastingResource().consume(familiar, mana);
            this.hasCast = true;
            this.lastCast = familiar.getServer().overworld().getGameTime();
            ArcaneAdditions.LOGGER.error("casting spell");
            this.stop();
        }
    }
}
