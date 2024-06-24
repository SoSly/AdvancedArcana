package org.sosly.arcaneadditions.configs.server;

import net.minecraftforge.common.ForgeConfigSpec;
import org.sosly.arcaneadditions.configs.Config;

import java.util.List;

public class FamiliarConfig {
    public final ForgeConfigSpec.ConfigValue<List<String>> familiars;

    public FamiliarConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Familiar settings").push("familiars");

        familiars = builder.comment("A list of entity types that can be bound as familiars.")
                .translation("config.arcaneadditions.familiars")
                .define("familiars", List.of(
                        "minecraft:bat", "minecraft:cat", "minecraft:parrot", "minecraft:fox",
                        "minecraft:rabbit", "minecraft:chicken", "minecraft:allay"
                ), this::isValidFamiliarList);

        builder.pop();
    }

    private <T> boolean isValidFamiliarList(T list) {
        return Config.isValidEntityList(list);
    }
}
