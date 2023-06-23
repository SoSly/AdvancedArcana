/*
 *   Arcane Additions Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.arcaneadditions.effects.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

@Deprecated
public class DoomedEffect extends MobEffect {
    public DoomedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }
}