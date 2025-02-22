package fr.tathan.nmc.platform.neoforge;

import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.jetbrains.annotations.Nullable;

public class DimensionUtilImpl {

    @Nullable
    public static ServerLevel createPlanet(MinecraftServer server, ResourceLocation location, NoiseBasedChunkGenerator chunkGenerator, Holder<DimensionType> dimensionType) {
        return InfiniverseAPI.get().getOrCreateLevel(server, ResourceKey.create(Registries.DIMENSION, location) , () -> new LevelStem(dimensionType, chunkGenerator));
    }

}
