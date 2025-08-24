package fr.tathan.nmc.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.events.Events;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class CustomAmbientParticle extends Level {

    protected CustomAmbientParticle(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @WrapOperation(
            method = "doAnimateTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getAmbientParticle()Ljava/util/Optional;")
    )
    private Optional<AmbientParticleSettings> addCustomAmbientParticle(Biome instance, Operation<Optional<AmbientParticleSettings>> original) {
        Optional<PlanetCreator> planetCreator = Events.SYSTEMS.planets.stream().filter((planet) -> planet.planet.dimension().equals(this.dimension().location())).findFirst();

        AtomicReference<Optional<AmbientParticleSettings>> ambientParticleSettings = new AtomicReference<>(original.call(instance));

        planetCreator.ifPresent(creator -> {
            if(creator.particleLocation.isPresent()) {
                ParticleType<?> particleType = this.registryAccess().registry(Registries.PARTICLE_TYPE).get().get(creator.particleLocation.get());

                if(particleType instanceof ParticleOptions options) {
                    AmbientParticleSettings settings = new AmbientParticleSettings(options, 0.05f);

                    ambientParticleSettings.set(Optional.of(settings));
                }
            }
        });

        return ambientParticleSettings.get();
    }


}
