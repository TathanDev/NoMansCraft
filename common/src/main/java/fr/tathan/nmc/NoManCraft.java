package fr.tathan.nmc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.tathan.nmc.common.data.SystemsData;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.networks.NetworkRegistry;

public final class NoManCraft {
    public static final String MODID = "nmc";
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        NetworkRegistry.init();
        Events.registerEvents();
    }
}
