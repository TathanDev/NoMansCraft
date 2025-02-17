package fr.tathan.nmc.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.event.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.MoonInfo;
import com.st0x0ef.stellaris.common.events.custom.PlanetEvents;
import com.st0x0ef.stellaris.common.events.custom.PlanetSelectionServerEvents;
import dev.architectury.event.EventResult;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.utils.Utils;
import fr.tathan.sky_aesthetics.client.data.SkyPropertiesData;

import java.util.ArrayList;
import java.util.Random;

public class Events {

    public static ArrayList<SystemCreator> SYSTEMS = new ArrayList<>();

    public static void registerEvents() {

        PlanetEvents.POST_PLANET_REGISTRY.register((planetRegistry, sync) -> {

            if(sync) return EventResult.pass();

            for (SystemCreator system : SYSTEMS) {
                for(PlanetCreator planetCreator : system.getPlanets()) {
                    planetRegistry.add(planetCreator.planet);
                    SkyPropertiesData.SKY_PROPERTIES.put(planetCreator.planet.dimension(), planetCreator.sky);

                    planetCreator.moons.forEach((moon) -> {
                        planetRegistry.add(moon.planet);
                        SkyPropertiesData.SKY_PROPERTIES.put(moon.planet.dimension(), moon.sky);
                    });
                }
            }

            return EventResult.pass();
        });

        PlanetSelectionServerEvents.LAUNCH_BUTTON.register((player, planet, rocket, context) -> {
            PlanetCreator creator = null;

            for(SystemCreator system : SYSTEMS) {
                for(PlanetCreator planetCreator : system.getPlanets()) {
                    if(planetCreator.planet.dimension().equals(planet.dimension())) {
                        creator = planetCreator;
                        break;
                    }
                    for(PlanetCreator moon : planetCreator.moons) {
                        if(moon.planet.dimension().equals(planet.dimension())) {
                            creator = planetCreator;
                            break;
                        }
                    }
                }
                if (creator != null) break;
            }
            if(creator == null) return EventResult.pass();
            Utils.generateWorld(context, creator);

            Stellaris.LOG.error("Launching to {}", creator.name);

            return EventResult.pass();
        });

        /**
         * We create the random systems and planets
         */
        PlanetSelectionClientEvents.POST_STAR_PACK_REGISTRY.register((stars) -> {

            if(!SYSTEMS.isEmpty()) return EventResult.pass();

            Random random = new Random();
            for (int i = 0; i < random.nextInt(2, 10) + 1; i++) {
                SystemCreator creator = new SystemCreator();

                PlanetSelectionScreen.STARS.add(creator.celestialBody);

                creator.getPlanets().forEach((planet) -> {
                    PlanetSelectionScreen.PLANETS.add(planet.planetInfo);

                    planet.moons.forEach((moon) -> {
                        //PlanetSelectionScreen.MOONS.add(new MoonInfo())
                    });
                });
                creator.changeStarPos();
                SYSTEMS.add(creator);
            }

            return EventResult.pass();
        });

    }
}
