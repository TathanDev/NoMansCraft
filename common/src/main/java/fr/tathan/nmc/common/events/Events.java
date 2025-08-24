package fr.tathan.nmc.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.events.custom.PlanetSelectionServerEvents;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.common.commands.NoManCommand;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.data.SystemsData;
import fr.tathan.nmc.common.networks.packets.SyncSystemPacket;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelStorageSource;

public class Events {

    public static SystemsContainer SYSTEMS = new SystemsContainer();

    public static void registerEvents() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> new NoManCommand(dispatcher));

        PlanetSelectionServerEvents.LAUNCH_BUTTON.register((player, planet, rocket, context) -> {
            if (player.level().isClientSide) return EventResult.pass();

            if (player instanceof ServerPlayer serverPlayer) {
                NetworkManager.sendToPlayer(serverPlayer, new SyncSystemPacket(SYSTEMS));

                return createPlanet(planet, serverPlayer, context);
            }

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


    public static boolean matchesDimension(PlanetCreator planetCreator, ResourceLocation dimension) {
        if (planetCreator.planet.dimension().equals(dimension)) {
            return true;
        }

        return planetCreator.moons.stream()
                .anyMatch(moon -> moon.planet.dimension().equals(dimension));
    }


    public static EventResult createPlanet(Planet planet, ServerPlayer player, NetworkManager.PacketContext context) {
        PlanetCreator creator = null;

        for (PlanetCreator planetCreator : SYSTEMS.planets) {
            if (matchesDimension(planetCreator, planet.dimension())) {
                creator = planetCreator;
                break;
            }
        }

        if(creator == null) return EventResult.pass();
        Utils.generateWorld(context, creator);
        return EventResult.pass();
    }

}



