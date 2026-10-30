package dev.corgitaco.blockswap.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.corgitaco.blockswap.mixin.BiomeMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BiomeAccess {

    private static final Map<Biome, Float> DOWNFALL_CACHE = new ConcurrentHashMap<>();

    private BiomeAccess() {
    }

    public static float getTemperature(Biome biome, BlockPos pos) {
        return ((BiomeMixin) (Object) biome).blockswap$invokeTemperature(pos);
    }

    public static float getDownfall(Biome biome) {
        Float cached = DOWNFALL_CACHE.get(biome);
        if (cached != null) {
            return cached;
        }
        float downfall = readDownfall(biome);
        DOWNFALL_CACHE.put(biome, downfall);
        return downfall;
    }

    private static float readDownfall(Biome biome) {
        JsonElement json = Biome.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, biome).getOrThrow();
        if (json.isJsonObject() && json.getAsJsonObject().has("downfall")) {
            return json.getAsJsonObject().get("downfall").getAsFloat();
        }
        return 0.0F;
    }
}
