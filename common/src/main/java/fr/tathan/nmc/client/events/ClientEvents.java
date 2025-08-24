package fr.tathan.nmc.client.events;

import com.st0x0ef.stellaris.client.events.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.common.events.custom.PlanetEvents;
import dev.architectury.event.EventResult;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.sky_aesthetics.client.data.SkyPropertiesData;
import fr.tathan.sky_aesthetics.client.skies.DimensionSky;

public class ClientEvents {

    public static void registerEvents() {
        PlanetEvents.POST_PLANET_REGISTRY.register((planetRegistry, sync) -> {

            if(sync) return EventResult.pass();

            for (SystemCreator system : Events.SYSTEMS.systems) {
                for(PlanetCreator planetCreator : system.getPlanets()) {
                    planetRegistry.add(planetCreator.planet);
                    SkyPropertiesData.SKY_PROPERTIES.put(planetCreator.planet.dimension(), new DimensionSky(planetCreator.sky));

                    planetCreator.moons.forEach((moon) -> {
                        planetRegistry.add(moon.planet);
                        SkyPropertiesData.SKY_PROPERTIES.put(moon.planet.dimension(), new DimensionSky(moon.sky));
                    });
                }
            }

            return EventResult.pass();
        });

        PlanetSelectionClientEvents.POST_PLANET_PACK_REGISTRY.register((planetInfos) -> {
            for (SystemCreator system : Events.SYSTEMS.systems) {
                for(PlanetCreator planetCreator : system.getPlanets()) {
                    planetInfos.add(planetCreator.planetInfo);
                }
            }
            return EventResult.pass();
        });

        PlanetSelectionClientEvents.POST_STAR_PACK_REGISTRY.register((celestialBodies) -> {
            for (SystemCreator system : Events.SYSTEMS.systems) {
                celestialBodies.add(system.celestialBody);
            }
            return EventResult.pass();
        });
    }
}
