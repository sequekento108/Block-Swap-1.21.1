package dev.corgitaco.blockswap.data;

import dev.corgitaco.blockswap.config.BlockSwapConfig;
import dev.corgitaco.blockswap.swapper.Swapper;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.type.chunk.ChunkBlockStateInterceptor;
import dev.corgitaco.dataanchor.data.type.chunk.LevelChunkTrackedData;
import dev.corgitaco.dataanchor.data.type.chunk.ServerLevelChunkTrackedData;
import dev.corgitaco.dataanchor.data.type.chunk.SyncedLevelChunkTrackedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashSet;
import java.util.Set;

public class BlockSwapLevelChunkData extends ServerLevelChunkTrackedData implements ChunkBlockStateInterceptor {

    private final Set<String> placeHolders = new HashSet<>();


    public BlockSwapLevelChunkData(TrackedDataKey<? extends LevelChunkTrackedData> trackedDataKey, LevelChunk levelChunk) {
        super(trackedDataKey, levelChunk);
    }


    @Override
    public CompoundTag save() {
        return super.save();
    }

    @Override
    public void load(CompoundTag compoundTag) {
    }

    @Override
    public BlockState getNewState(BlockPos blockPos, BlockState originalState, BlockState currentState, boolean b) {
        BlockSwapConfig config = BlockSwapConfig.getConfig(false);
        if (config.contains(originalState)) {
            return Swapper.remapState(originalState);
        }
        return currentState;
    }

    public Set<String> getPlaceHolders() {
        return placeHolders;
    }
}
