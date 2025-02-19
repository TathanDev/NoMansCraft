package fr.tathan.nmc.fabric.client;

import fr.tathan.nmc.client.NoManCraftClient;
import net.fabricmc.api.ClientModInitializer;

public final class NoManCraftFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NoManCraftClient.init();
    }
}
