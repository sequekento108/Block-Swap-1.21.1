package dev.corgitaco.blockswap.mixin;

import com.google.common.collect.ImmutableMap;
import corgitaco.corgilib.serialization.jankson.JanksonJsonOps;
import corgitaco.corgilib.serialization.jankson.JanksonUtil;
import dev.corgitaco.blockswap.BlockSwap;
import dev.corgitaco.blockswap.config.BlockSwapConfig;
import dev.corgitaco.blockswap.config.MissingBlockIDsConfig;
import dev.corgitaco.blockswap.swapper.Swapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void blockSwap_loadConfig(CallbackInfo ci) {
        BlockSwapConfig config = BlockSwapConfig.getConfig(true);
        MissingBlockIDsConfig missingBlockIDsConfig = MissingBlockIDsConfig.getConfig(true);

        if (config.generateAllKnownStates()) {
            Map<Block, List<BlockState>> allKnownStates = new TreeMap<>(Comparator.comparing(block -> BuiltInRegistries.BLOCK.getKey(block).toString()));
            for (Block block : BuiltInRegistries.BLOCK) {
                allKnownStates.computeIfAbsent(block, key -> key.getStateDefinition().getPossibleStates());
            }

            allKnownStates.forEach((block, blockStates) -> {
                ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
                JanksonUtil.createConfig(BlockSwap.CONFIG_PATH.resolve("known_states").resolve(blockKey.getNamespace()).resolve(blockKey.getPath() + ".json5"), Swapper.COMMENTED_STATE_CODEC.listOf(), JanksonUtil.HEADER_CLOSED, ImmutableMap.of(), JanksonJsonOps.INSTANCE, blockStates);
            });
        }
    }
}
