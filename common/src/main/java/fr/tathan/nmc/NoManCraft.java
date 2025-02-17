package fr.tathan.nmc;

import fr.tathan.nmc.common.events.Events;

public final class NoManCraft {
    public static final String MOD_ID = "nmc";

    public static void init() {
        Events.registerEvents();
    }
}
