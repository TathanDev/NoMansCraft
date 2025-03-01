package fr.tathan.nmc.platform.fabric;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.jetbrains.annotations.Nullable;
import qouteall.dimlib.api.DimensionAPI;

public class DimensionUtilImpl {

    @Nullable
    public static ServerLevel createPlanet(MinecraftServer server, ResourceLocation location, NoiseBasedChunkGenerator chunkGenerator, Holder<DimensionType> dimensionType) {
        //TODO Add random seed !
        DimensionAPI.addDimensionIfNotExists(
                server,
                location,
                () -> new LevelStem(dimensionType, chunkGenerator)
        );
        return null;
    }


}
