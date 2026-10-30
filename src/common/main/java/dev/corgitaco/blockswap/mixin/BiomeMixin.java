package dev.corgitaco.blockswap.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Biome.class)
public interface BiomeMixin {

    @Invoker("getTemperature")
    float blockswap$invokeTemperature(BlockPos pos);
}
