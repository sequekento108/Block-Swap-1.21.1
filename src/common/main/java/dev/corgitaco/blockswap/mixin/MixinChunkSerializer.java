package dev.corgitaco.blockswap.mixin;

import dev.corgitaco.blockswap.config.MissingBlockIDsConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ChunkSerializer.class)
public class MixinChunkSerializer {

    @Inject(method = "read", at = @At("HEAD"))
    private static void blockSwap_repairStates(ServerLevel level, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir) {
        Map<String, Block> idRemapper = MissingBlockIDsConfig.getConfig(false).idRemapper();
        if (idRemapper.isEmpty()) {
            return;
        }

        if (tag.contains("sections", Tag.TAG_LIST)) {
            for (Tag sectionTag : tag.getList("sections", Tag.TAG_COMPOUND)) {
                CompoundTag section = (CompoundTag) sectionTag;
                if (section.contains("block_states", Tag.TAG_COMPOUND)) {
                    ListTag palette = section.getCompound("block_states").getList("palette", Tag.TAG_COMPOUND);
                    for (Tag paletteEntry : palette) {
                        CompoundTag entry = (CompoundTag) paletteEntry;
                        String name = entry.getString("Name");
                        if (idRemapper.containsKey(name)) {
                            entry.putString("Name", BuiltInRegistries.BLOCK.getKey(idRemapper.get(name)).toString());
                        }
                    }
                }
            }
        }
    }
}
