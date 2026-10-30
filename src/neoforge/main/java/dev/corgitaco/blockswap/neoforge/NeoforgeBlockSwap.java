package dev.corgitaco.blockswap.neoforge;

import dev.corgitaco.blockswap.BlockSwap;
import dev.corgitaco.blockswap.config.BlockSwapConfig;
import dev.corgitaco.blockswap.neoforge.network.ConfigSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.Mod;

@Mod(BlockSwap.MOD_ID)
public class NeoforgeBlockSwap {
    public NeoforgeBlockSwap(IEventBus modEventBus) {
        BlockSwap.init(FMLPaths.CONFIGDIR.get().resolve(BlockSwap.MOD_ID));
        BlockSwap.initialize();

        modEventBus.addListener(this::registerPayloads);

        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(this::onPlayerRespawn);
        NeoForge.EVENT_BUS.addListener(this::onPlayerChangedDimension);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(
                ConfigSyncPayload.TYPE,
                ConfigSyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> BlockSwapConfig.getConfig(payload.config()))
        );
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new ConfigSyncPayload(BlockSwapConfig.getConfig(true)));
        }
    }

    private void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new ConfigSyncPayload(BlockSwapConfig.getConfig(true)));
        }
    }

    private void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new ConfigSyncPayload(BlockSwapConfig.getConfig(true)));
        }
    }
}
