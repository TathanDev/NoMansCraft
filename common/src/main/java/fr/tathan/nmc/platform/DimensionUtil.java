package fr.tathan.nmc.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.jetbrains.annotations.Nullable;

public class DimensionUtil {

    @Nullable
    @ExpectPlatform
    public static ServerLevel createPlanet(MinecraftServer server, ResourceLocation location, NoiseBasedChunkGenerator chunkGenerator, Holder<DimensionType> dimensionType) {
        throw new AssertionError();
    }

    @Nullable
    @ExpectPlatform
    public static ServerLevel getPlanet(MinecraftServer server, ResourceLocation location, NoiseBasedChunkGenerator chunkGenerator, Holder<DimensionType> dimensionType) {
        throw new AssertionError();
    }

}
