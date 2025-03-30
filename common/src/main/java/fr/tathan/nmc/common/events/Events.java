package fr.tathan.nmc.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.events.custom.PlanetSelectionServerEvents;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.data.SystemsData;
import fr.tathan.nmc.common.networks.packets.SyncSystemPacket;
import fr.tathan.nmc.common.utils.Utils;
import net.fabricmc.api.EnvType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelStorageSource;

public class Events {

    public static SystemsContainer SYSTEMS = new SystemsContainer();

    public static void registerEvents() {

        PlanetSelectionServerEvents.LAUNCH_BUTTON.register((player, planet, rocket, context) -> {
            if(player.level().isClientSide) return EventResult.pass();

            if(player instanceof ServerPlayer serverPlayer) NetworkManager.sendToPlayer(serverPlayer, new SyncSystemPacket(SYSTEMS));


            PlanetCreator creator = null;

            for (PlanetCreator planetCreator : SYSTEMS.planets) {
                if (matchesDimension(planetCreator, planet)) {
                    creator = planetCreator;
                    break;
                }
            }

            if(creator == null) return EventResult.pass();
            Utils.generateWorld(context, creator);
            return EventResult.pass();
        });


        LifecycleEvent.SERVER_STARTED.register((server) -> {
            LevelStorageSource.LevelStorageAccess levelStorageSource = server.storageSource;

            SystemsData.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());
        });


        PlayerEvent.PLAYER_JOIN.register((player) -> {
            LevelStorageSource.LevelStorageAccess levelStorageSource = player.server.storageSource;

            SystemsData.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());
            NetworkManager.sendToPlayer(player, new SyncSystemPacket(SYSTEMS));

        });
    }

    public static boolean matchesDimension(PlanetCreator planetCreator, Planet planet) {
        if (planetCreator.planet.dimension().equals(planet.dimension())) {
            return true;
        }

        return planetCreator.moons.stream()
                .anyMatch(moon -> moon.planet.dimension().equals(planet.dimension()));
    }

}



