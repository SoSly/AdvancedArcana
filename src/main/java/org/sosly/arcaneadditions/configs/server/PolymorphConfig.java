/*
 *   Arcane Additions Copyright (c) 2023, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.arcaneadditions.configs.server;


import net.minecraftforge.common.ForgeConfigSpec;
import org.sosly.arcaneadditions.configs.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolymorphConfig {
    public final ForgeConfigSpec.BooleanValue allowSpellcasting;
    public final ForgeConfigSpec.ConfigValue<List<? extends List<String>>> tiers;

    public PolymorphConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Polymorph settings").push("polymorph");

        allowSpellcasting = builder.comment("If true, players will be able to cast spells while polymorphed.")
                .translation("config.arcaneadditions.polymorph_allow_spellcasting_while_polymorphed")
                .define("allowSpellcastingWhilePolymorphed", false);

        // Minecraft
        ArrayList<ArrayList<String>> minecraft = getMinecraft();
        ArrayList<String> tier1polymorphs = minecraft.get(0);
        ArrayList<String> tier2polymorphs = minecraft.get(1);
        ArrayList<String> tier3polymorphs = minecraft.get(2);
        ArrayList<String> tier4polymorphs = minecraft.get(3);

        // exoticbirds
        // fixme: exotic birds animations do not work with identity, so they are disabled for now

        List<List<String>> morphTiers = Arrays.asList(tier1polymorphs, tier2polymorphs, tier3polymorphs, tier4polymorphs);

        tiers = builder.comment("A list of polymorph tiers, each of which is a list of entity IDs and their corresponding polymorph spell IDs.")
                .translation("config.arcaneadditions.polymorph_tiers")
                .defineList("morphTiers", morphTiers, it -> it instanceof List);

        builder.pop();
    }

    private static ArrayList<ArrayList<String>> getMinecraft() {
        ArrayList<ArrayList<String>> polymorphs = new ArrayList<>();

        ArrayList<String> tier1polymorphs = new ArrayList<>();
        ArrayList<String> tier2polymorphs = new ArrayList<>();
        ArrayList<String> tier3polymorphs = new ArrayList<>();
        ArrayList<String> tier4polymorphs = new ArrayList<>();

        tier1polymorphs.add("minecraft:cat");
        tier1polymorphs.add("minecraft:chicken");
        tier1polymorphs.add("minecraft:cow");
        tier1polymorphs.add("minecraft:donkey");
        tier1polymorphs.add("minecraft:fox");
        tier1polymorphs.add("minecraft:goat");
        tier1polymorphs.add("minecraft:horse");
        tier1polymorphs.add("minecraft:llama");
        tier1polymorphs.add("minecraft:mule");
        tier1polymorphs.add("minecraft:ocelot");
        tier1polymorphs.add("minecraft:panda");
        tier1polymorphs.add("minecraft:pig");
        tier1polymorphs.add("minecraft:rabbit");
        tier1polymorphs.add("minecraft:sheep");
        tier1polymorphs.add("minecraft:spider");
        tier1polymorphs.add("minecraft:wolf");

        tier2polymorphs.add("minecraft:axolotl");
        tier2polymorphs.add("minecraft:bat");
        tier2polymorphs.add("minecraft:bee");
        tier2polymorphs.add("minecraft:cave_spider");
        tier2polymorphs.add("minecraft:cod");
        tier2polymorphs.add("minecraft:dolphin");
        tier2polymorphs.add("minecraft:mooshroom");
        tier2polymorphs.add("minecraft:parrot");
        tier2polymorphs.add("minecraft:pufferfish");
        tier2polymorphs.add("minecraft:salmon");
        tier2polymorphs.add("minecraft:polar_bear");
        tier2polymorphs.add("minecraft:squid");
        tier2polymorphs.add("minecraft:tropical_fish");
        tier2polymorphs.add("minecraft:turtle");

        tier3polymorphs.add("minecraft:creeper");
        tier3polymorphs.add("minecraft:drowned");
        tier3polymorphs.add("minecraft:glow_squid");
        tier3polymorphs.add("minecraft:husk");
        tier3polymorphs.add("minecraft:iron_golem");
        tier3polymorphs.add("minecraft:piglin");
        tier3polymorphs.add("minecraft:ravager");
        tier3polymorphs.add("minecraft:silverfish");
        tier3polymorphs.add("minecraft:skeleton");
        tier3polymorphs.add("minecraft:skeleton_horse");
        tier3polymorphs.add("minecraft:slime");
        tier3polymorphs.add("minecraft:snow_golem");
        tier3polymorphs.add("minecraft:stray");
        tier3polymorphs.add("minecraft:strider");
        tier3polymorphs.add("minecraft:zombie");
        tier3polymorphs.add("minecraft:zombie_horse");
        tier3polymorphs.add("minecraft:zombified_piglin");

        tier4polymorphs.add("minecraft:blaze");
        tier4polymorphs.add("minecraft:enderman");
        tier4polymorphs.add("minecraft:endermite");
        tier4polymorphs.add("minecraft:ghast");
        tier4polymorphs.add("minecraft:giant");
        tier4polymorphs.add("minecraft:guardian");
        tier4polymorphs.add("minecraft:hoglin");
        tier4polymorphs.add("minecraft:magma_cube");
        tier4polymorphs.add("minecraft:phantom");
        tier4polymorphs.add("minecraft:piglin_brute");
        tier4polymorphs.add("minecraft:shulker");
        tier4polymorphs.add("minecraft:vex");
        tier4polymorphs.add("minecraft:wither_skeleton");
        tier4polymorphs.add("minecraft:zoglin");

        polymorphs.add(tier1polymorphs);
        polymorphs.add(tier2polymorphs);
        polymorphs.add(tier3polymorphs);
        polymorphs.add(tier4polymorphs);

        return polymorphs;
    }

    private <T> boolean isValidPolymorphTierList(T list) {
        if (!(list instanceof List)) {
            return false;
        }

        for (Object o : (List<?>) list) {
            if (!Config.isValidEntityList(list)) {
                return false;
            }
        }
        return true;
    }
}