/*
 * Scaling Health
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.scalinghealth.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.silentchaos512.lib.util.EntityHelper;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.capability.IPlayerData;
import net.silentchaos512.scalinghealth.client.particles.ModParticles;
import net.silentchaos512.scalinghealth.init.ModSounds;
import net.silentchaos512.scalinghealth.utils.Players;
import net.silentchaos512.utils.MathUtils;

public class HeartCrystal extends StatBoosterItem {
    @Override
    int getLevelCost(EntityPlayer player) {
        return Players.levelCostToUseHeartCrystal(player);
    }

    @Override
    boolean isStatIncreaseAllowed(EntityPlayer player, IPlayerData data) {
        return Players.heartCrystalsIncreaseHealth(player)
                && data.getExtraHearts() < Players.maxHeartCrystals(player);
    }

    @Override
    boolean shouldConsume(EntityPlayer player) {
        return Players.heartCrystalHealthRestored(player) > 0
                && player.getHealth() < player.getMaxHealth();
    }

    @Override
    void extraConsumeEffect(EntityPlayer player) {
        int current = (int) player.getHealth();
        float healAmount = Players.heartCrystalHealthRestored(player);
        EntityHelper.heal(player, healAmount, true);
        int newHealth = (int) player.getHealth();
        if (!MathUtils.doublesEqual(current + healAmount, newHealth)) {
            ScalingHealth.LOGGER.warn("Another mod seems to have canceled healing from a heart container (player {})", player.getName());
        }
    }

    @Override
    void increaseStat(EntityPlayer player, ItemStack stack, IPlayerData data) {
        data.addHeart(player);
    }

    @Override
    ModParticles getParticleType() {
        return ModParticles.HEART_CRYSTAL;
    }

    @Override
    ModSounds getSoundEffect() {
        return ModSounds.HEART_CRYSTAL_USE;
    }
}
