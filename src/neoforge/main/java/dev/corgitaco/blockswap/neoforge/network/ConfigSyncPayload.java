package dev.corgitaco.blockswap.neoforge.network;

import dev.corgitaco.blockswap.BlockSwap;
import dev.corgitaco.blockswap.config.BlockSwapConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ConfigSyncPayload(BlockSwapConfig config) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ConfigSyncPayload> TYPE = new CustomPacketPayload.Type<>(BlockSwap.id("config_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ConfigSyncPayload decode(RegistryFriendlyByteBuf buf) {
            return new ConfigSyncPayload(buf.readJsonWithCodec(BlockSwapConfig.CODEC));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ConfigSyncPayload payload) {
            buf.writeJsonWithCodec(BlockSwapConfig.CODEC, payload.config());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
