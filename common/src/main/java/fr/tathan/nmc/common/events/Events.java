package fr.tathan.nmc.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.event.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.events.custom.PlanetEvents;
import com.st0x0ef.stellaris.common.events.custom.PlanetSelectionServerEvents;
import com.st0x0ef.stellaris.common.network.packets.SyncPlanetsDatapackPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.common.creators.MoonCreator;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.data.SystemsData;
import fr.tathan.nmc.common.networks.packets.SyncSystemPacket;
import fr.tathan.nmc.common.utils.Utils;
import fr.tathan.sky_aesthetics.client.data.SkyPropertiesData;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.util.ArrayList;

public class Events {

    public static SystemsContainer SYSTEMS = new SystemsContainer();

    public static void registerEvents() {

        PlanetSelectionServerEvents.LAUNCH_BUTTON.register((player, planet, rocket, context) -> {
            PlanetCreator creator = null;

            for(SystemCreator system : SYSTEMS.systems) {
                for(PlanetCreator planetCreator : system.getPlanets()) {
                    if(planetCreator.planet.dimension().equals(planet.dimension())) {
                        creator = planetCreator;
                        break;
                    }
                    for(MoonCreator moon : planetCreator.moons) {
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



        LifecycleEvent.SERVER_BEFORE_START.register((server) -> {

            LevelStorageSource.LevelStorageAccess levelStorageSource = server.storageSource;

            SystemsData.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());

        });

        PlayerEvent.PLAYER_JOIN.register((player) -> {
            Stellaris.LOG.error("Sending systems to player");
            NetworkManager.sendToPlayer(player, new SyncSystemPacket(SYSTEMS));
        });
    }
}
