package fr.tathan.nmc.common.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import dev.architectury.platform.Platform;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Random;

public class SystemsData {

    public static void loadOrGenerateDefaults() {
        Path path = Platform.getConfigFolder().resolve(NoManCraft.MODID).resolve("systems.json");

        try {
            BufferedReader reader = Files.newBufferedReader(path);
            JsonElement jsonElement = GsonHelper.parse(reader);
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "planets");
            SystemsContainer creator = SystemsContainer.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            Stellaris.LOG.error("Loaded systems from file {}", creator.systems.size());

        } catch (Exception e) {

            // This is expected first run, so don't throw then
            if (!(e instanceof NoSuchFileException))
                e.printStackTrace();


            try {
                File folder = path.toFile().getParentFile();
                if (!folder.exists())
                    folder.mkdirs();

                SystemsContainer defaults = createSystems();

                JsonElement jsonElement = SystemsContainer.toJson(defaults);
                String json = NoManCraft.gson.toJson(jsonElement);

                var writer = Files.newBufferedWriter(path);
                writer.write(json);
                writer.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    public static SystemsContainer createSystems() {
        SystemsContainer container = new SystemsContainer();

        Random random = new Random();
        for (int i = 0; i < random.nextInt(2, 10) + 1; i++) {
            SystemCreator creator = new SystemCreator();
            Stellaris.LOG.error("Creating system {}", creator.name);
            PlanetSelectionScreen.STARS.add(creator.celestialBody);

            creator.getPlanets().forEach((planet) -> {
                PlanetSelectionScreen.PLANETS.add(planet.planetInfo);

                planet.moons.forEach((moon) -> {
                    //PlanetSelectionScreen.MOONS.add(new MoonInfo())
                });
            });
            creator.changeStarPos();
            container.systems.add(creator);
        }
        return container;
    }

}
