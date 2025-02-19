package fr.tathan.nmc.client.events;

import com.st0x0ef.stellaris.common.events.custom.PlanetEvents;
import dev.architectury.event.EventResult;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.sky_aesthetics.client.data.SkyPropertiesData;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;

public class ClientEvents {

    public static void registerEvents() {
        PlanetEvents.POST_PLANET_REGISTRY.register((planetRegistry, sync) -> {

            if(sync) return EventResult.pass();

            for (SystemCreator system : Events.SYSTEMS.systems) {
                for(PlanetCreator planetCreator : system.getPlanets()) {
                    planetRegistry.add(planetCreator.planet);
                    SkyPropertiesData.SKY_PROPERTIES.put(planetCreator.planet.dimension(), new PlanetSky(planetCreator.sky));

                    planetCreator.moons.forEach((moon) -> {
                        planetRegistry.add(moon.planet);
                        SkyPropertiesData.SKY_PROPERTIES.put(moon.planet.dimension(), new PlanetSky(moon.sky));
                    });
                }
            }

            return EventResult.pass();
        });
    }
}
