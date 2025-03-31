package fr.tathan.nmc.common.events.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.ArrayList;

public interface SystemsCreationLifecycle {

    Event<PostSystemsCreation> POST_SYSTEMS_CREATION = EventFactory.createEventResult();

    interface PostSystemsCreation {
        /**
         * Invoked when a Planet is registered.
         *
         * @param container  The List of all the Systems and Planets.
         * @param systems    The number of systems created.
         */
        void postSystemsCreation(SystemsContainer container, int systems);
    }




}
