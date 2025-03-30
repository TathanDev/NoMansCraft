package fr.tathan.nmc.common.events.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.common.creators.PlanetCreator;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.ArrayList;

public interface PlanetsCreationLifecycle {

    Event<PostBiomeSelection> POST_BIOMES_SELECTION = EventFactory.createLoop();
    Event<PrePlanetLevelCreation> PRE_PLANET_LEVEL_CREATION = EventFactory.createEventResult();


    interface PrePlanetLevelCreation {
        /**
         * Invoked when a Planet is registered.
         *
         * @param planet    The planet registered.
         * @param holder    The dimension type of the planet.
         * @param generator The noise generator of the level.
         * @param context   The context of the packet.
         */
        void prePlanetLevelCreation(PlanetCreator planet, NoiseBasedChunkGenerator generator, Holder<DimensionType> holder, NetworkManager.PacketContext context);
    }

    interface PostBiomeSelection {
        /**
         * Invoked when a Planet is registered.
         *
         * @param planet The planet registered.
         * @param biomes The list of biomes of the planet.
         */
        void postBiomeSelection(PlanetCreator planet, ArrayList<ResourceKey<Biome>> biomes);
    }

}
