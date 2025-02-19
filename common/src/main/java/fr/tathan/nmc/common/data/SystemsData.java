package fr.tathan.nmc.common.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.common.network.packets.SyncWidgetsTanksPacket;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SystemsData {

    public static void loadOrGenerateDefaults(Path worldPath) {
        Path systemsFile = worldPath.resolve("systems.json");

        Stellaris.LOG.error("Loading systems from file {}", systemsFile.toUri());
        try {
            BufferedReader reader = Files.newBufferedReader(systemsFile);
            JsonElement jsonElement = GsonHelper.parse(reader);
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "systems");
            SystemsContainer creator = SystemsContainer.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

            for(SystemCreator systemCreator : creator.systems ) {
                systemCreator.setPlanets(Utils.getPlanetsInSystem(systemCreator.name, creator));
            }
            Events.SYSTEMS = creator;
            Stellaris.LOG.error("Loaded systems from file {}", creator.systems.size());

        } catch (Exception e) {

            // This is expected first run, so don't throw then
            if (!(e instanceof NoSuchFileException))
                e.printStackTrace();


            try {
                File folder = systemsFile.toFile().getParentFile();
                if (!folder.exists())
                    folder.mkdirs();

                SystemsContainer defaults = createSystems();
                JsonElement jsonElement = SystemsContainer.toJson(defaults);
                String systemFile = NoManCraft.gson.toJson(jsonElement);

                var systemWrite = Files.newBufferedWriter(systemsFile);
                systemWrite.write(systemFile);
                systemWrite.close();

                for(SystemCreator systemCreator : defaults.systems ) {
                    systemCreator.setPlanets(Utils.getPlanetsInSystem(systemCreator.name, defaults));
                }

                for(SystemCreator systemCreator : defaults.systems ) {
                    systemCreator.setPlanets(Utils.getPlanetsInSystem(systemCreator.name, defaults));
                }
                Events.SYSTEMS = defaults;

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    public static List<PlanetCreator> getPlanets(SystemsContainer container) {
        List<PlanetCreator> planets = new ArrayList<>();
        container.systems.forEach((system) -> {
            planets.addAll(system.getPlanets());
        });
        return planets;
    }

    public static SystemsContainer createSystems() {
        SystemsContainer container = new SystemsContainer();

        Random random = new Random();
        for (int i = 0; i < random.nextInt(2, 10) + 1; i++) {
            SystemCreator creator = new SystemCreator();
            creator.changeStarPos(container.systems);
            container.systems.add(creator);
        }
        container.planets = getPlanets(container);
        return container;
    }

}
