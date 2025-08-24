package fr.tathan.nmc.mixin;

import com.google.gson.JsonElement;
import com.st0x0ef.stellaris.Stellaris;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.sky_aesthetics.client.data.SkyPropertiesData;
import fr.tathan.sky_aesthetics.client.skies.DimensionSky;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SkyPropertiesData.class)
public class SkyRegistryMixin {

    @Inject(method = "apply*", at = @At("TAIL"))
    protected void addCustomSky(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        Stellaris.LOG.info("Adding custom sky properties {}", Events.SYSTEMS.systems.size());
        for (SystemCreator system : Events.SYSTEMS.systems) {
            for(PlanetCreator planetCreator : system.getPlanets()) {
                SkyPropertiesData.SKY_PROPERTIES.put(planetCreator.planet.dimension(), new DimensionSky(planetCreator.sky));
                planetCreator.moons.forEach((moon) -> {
                    SkyPropertiesData.SKY_PROPERTIES.put(moon.planet.dimension(), new DimensionSky(moon.sky));
                });

            }
        }
    }
}
