package fr.tathan.nmc.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.events.custom.PlanetSelectionServerEvents;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.common.creators.MoonCreator;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.data.DimensionData;
import fr.tathan.nmc.common.data.SystemsData;
import fr.tathan.nmc.common.networks.packets.SyncSystemPacket;
import fr.tathan.nmc.common.utils.Utils;
import fr.tathan.nmc.platform.DimensionUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.storage.LevelStorageSource;

public class Events {

    public static SystemsContainer SYSTEMS = new SystemsContainer();

    public static void registerEvents() {

        PlanetSelectionServerEvents.LAUNCH_BUTTON.register((player, planet, rocket, context) -> {
            PlanetCreator creator = null;

            for(PlanetCreator planetCreator : SYSTEMS.planets) {
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
            if(creator == null) return EventResult.pass();
            Utils.generateWorld(context, creator);
            return EventResult.pass();
        });


        LifecycleEvent.SERVER_BEFORE_START.register((server) -> {
            LevelStorageSource.LevelStorageAccess levelStorageSource = server.storageSource;
            SystemsData.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());
        });

        LifecycleEvent.SERVER_STOPPING.register((server) -> {

            LevelStorageSource.LevelStorageAccess levelStorageSource = server.storageSource;
            SystemsData.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());
        });

        PlayerEvent.PLAYER_JOIN.register((player) -> {
            NetworkManager.sendToPlayer(player, new SyncSystemPacket(SYSTEMS));

        });
    }
}
