package fr.tathan.nmc.client;

import dev.architectury.platform.Platform;
import fr.tathan.nmc.client.events.ClientEvents;
import fr.tathan.nmc.common.config.NMConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class NoManCraftClient {

    public static void init() {
        Platform.getMod("nmc").registerConfigurationScreen((p) -> AutoConfig.getConfigScreen(NMConfig.class, p).get());

        ClientEvents.registerEvents();
    }
}
