package fr.tathan.nmc.neoforge;

import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.client.NoManCraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = NoManCraft.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class NoManCraftNeoForgeClient {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(NoManCraftClient::init);
    }

}
