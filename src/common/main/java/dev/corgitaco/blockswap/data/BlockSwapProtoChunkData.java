package dev.corgitaco.blockswap.data;

import dev.corgitaco.blockswap.swapper.Swapper;
import dev.corgitaco.blockswap.util.ChunkSectionBlockStateSwapper;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.type.chunk.ChunkTrackedData;
import dev.corgitaco.dataanchor.data.type.chunk.ProtoChunkTrackedData;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;

import java.util.HashSet;
import java.util.Set;

public class BlockSwapProtoChunkData extends ProtoChunkTrackedData {

    private final Set<String> placeHolders = new HashSet<>();

    public BlockSwapProtoChunkData(TrackedDataKey<? extends ChunkTrackedData> trackedDataKey, ProtoChunk protoChunk) {
        super(trackedDataKey, protoChunk);

        Int2IntMap swapper = Swapper.getIdSwapper();
        for (LevelChunkSection section : protoChunk.getSections()) {
            if (section instanceof ChunkSectionBlockStateSwapper chunkSectionBlockStateSwapper) {
                chunkSectionBlockStateSwapper.blockSwap$createSwapper(swapper.isEmpty() ? null : swapper);
            }
        }
    }

    public Set<String> getPlaceHolders() {
        return placeHolders;
    }
}
