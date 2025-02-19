package fr.tathan.nmc.client;

import fr.tathan.nmc.client.events.ClientEvents;

public class NoManCraftClient {

    public static void init() {
        ClientEvents.registerEvents();
    }
}
