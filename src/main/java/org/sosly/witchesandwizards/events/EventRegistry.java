package org.sosly.witchesandwizards.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sosly.witchesandwizards.WitchesAndWizards;
import org.sosly.witchesandwizards.effects.beneficial.IceBlockEffect;

public class EventRegistry {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    @Mod.EventBusSubscriber(modid = WitchesAndWizards.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class EventHandler {
        @SubscribeEvent
        public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onBreakingBlock(PlayerEvent.BreakSpeed event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onDamage(LivingDamageEvent event) {
            IceBlockEffect.handleDamageEvents(event);
        }

        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onHarvest(PlayerEvent.HarvestCheck event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onItemInteract(PlayerInteractEvent.RightClickItem event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onJump(LivingEvent.LivingJumpEvent event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onLivingAttack(LivingAttackEvent event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onKnockback(LivingKnockBackEvent event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onPickup(PlayerEvent.ItemPickupEvent event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @SubscribeEvent
        public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
            IceBlockEffect.onEffectAdded(event);
        }

        @SubscribeEvent
        public static void onPotionExpired(PotionEvent.PotionExpiryEvent event) {
            IceBlockEffect.onEffectRemoved(event);
        }

        @SubscribeEvent
        public static void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
            IceBlockEffect.onEffectRemoved(event);
        }

        @SubscribeEvent
        public static void onPlayerAttack(AttackEntityEvent event) {
            IceBlockEffect.handleRestrictedActions(event);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onPostRenderLiving(RenderLivingEvent.Post event) {
            IceBlockEffect.handleRenderEvent(event);
        }
    }
}
