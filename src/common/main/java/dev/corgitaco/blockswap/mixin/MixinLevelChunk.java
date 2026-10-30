package dev.corgitaco.blockswap.mixin;

import dev.corgitaco.blockswap.util.TickHelper;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelChunk.class)
public abstract class MixinLevelChunk implements TickHelper {
    boolean isTickDirty;

    @Override
    public boolean markTickDirty() {
        return isTickDirty;
    }

    @Override
    public void setTickDirty() {
        this.isTickDirty = true;
    }
}
