/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.mixin.core.event.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.action.SleepingEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerSleepInBedEvent.class, remap = false)
public abstract class MixinPlayerSleepInBedEvent extends MixinEventPlayer implements SleepingEvent.Pre {

    @Shadow public BlockPos pos;
    @Shadow public EnumStatus result;
    private BlockSnapshot bed;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstructed(EntityPlayer player, BlockPos pos, CallbackInfo ci) {
        this.bed = ((World) player.worldObj).createSnapshot(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public BlockSnapshot getBed() {
        return this.bed;
    }

    @Override
    public boolean isCancelled() {
        return this.result != EnumStatus.OK;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.result = cancel ? EnumStatus.OTHER_PROBLEM : EnumStatus.OK;
    }

}
