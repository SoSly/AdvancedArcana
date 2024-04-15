/*
 *   Arcane Additions Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.arcaneadditions.configs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.sosly.arcaneadditions.ArcaneAdditions;
import org.sosly.arcaneadditions.configs.server.FamiliarConfig;
import org.sosly.arcaneadditions.configs.server.PolymorphConfig;

import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> serverSpec = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = serverSpec.getRight();
        SERVER = serverSpec.getLeft();
    }

    public static <T> boolean isValidEntityList(T entry) {
        if (!(entry instanceof List)) {
            return false;
        }

        for (Object i : (List<?>) entry) {
            if (!(i instanceof String)) {
                return false;
            }

            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation((String) i));
            if (type == null) {
                return false;
            }
        }

        return true;
    }

    public static class Server {
        public final FamiliarConfig familiar;
        public final PolymorphConfig polymorph;
        public final SoulSearchersLensConfig soulSearchersLens;

        public Server(ForgeConfigSpec.Builder builder) {
            familiar = new FamiliarConfig(builder);
            polymorph = new PolymorphConfig(builder);
            soulSearchersLens = new SoulSearchersLensConfig(builder);
        }

        public static class SoulSearchersLensConfig {
            public final ForgeConfigSpec.IntValue healthPerLevel;
            public final ForgeConfigSpec.IntValue maxDistance;
            public final ForgeConfigSpec.ConfigValue<List<String>> creatureModifiers;

            public SoulSearchersLensConfig(ForgeConfigSpec.Builder builder) {
                builder.comment("Soul Searcher's Lens settings").push("soulsearchers_lens");

                creatureModifiers = builder.comment("An (optional) list of modifiers for specific creatures health values for determining the XP required to study that creature")
                        .translation("config.arcaneadditions.soulsearchers_lens_creature_modifiers")
                        .define("creatureModifiers", List.of("minecraft:villager,1.5"));
                healthPerLevel = builder.comment("For every multiple of this number that a creature has in max health, the player must spend a level to progress their phylactery progress.")
                        .translation("config.arcaneadditions.soulsearchers_lens_health_per_level")
                        .defineInRange("healthPerLevel", 20, 1, Integer.MAX_VALUE);
                maxDistance = builder.comment("This setting determines how many blocks away the player can be as they continue to study their target.")
                        .translation("config.arcaneadditions.soulsearchers_lens_max_distance")
                        .defineInRange("maxDistance", 5, 1, 32);

                builder.pop();
            }
        }
    }
}