package fr.tathan.nmc.common.utils;

import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.sky_aesthetics.client.skies.record.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SkyUtils {

    public static ResourceLocation BASIC_SUN = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");
    public static ResourceLocation BASIC_MOON = ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");

    public static SkyProperties createSky(PlanetCreator creator) {
        SkyProperties properties = new SkyProperties(
                ResourceKey.create(Registries.DIMENSION, creator.planet.dimension()),
                Optional.of( creator.planet.dimension()),
                getCloudSettings(creator),
                Optional.of(getFogSettings(creator)),
                isRaining(creator),
                getCustomVanillaObjet(creator),
                getStars(creator),
                Optional.empty(),
                Optional.empty(),
                "NORMAL",
                getSkyColor(creator),
                List.of(),
                Optional.empty(),
                Optional.empty()
        );
        return properties;
    }

    public static CloudSettings getCloudSettings(PlanetCreator creator) {
        boolean hasClouds = creator.planet.oxygen();
        int cloudHeight = Math.random() > 0.5 ? 100 : 200;
        return new CloudSettings(hasClouds, cloudHeight, Optional.empty());
    }

    public static FogSettings getFogSettings(PlanetCreator creator) {
        float before = (float) (Math.random() * 100) +  (float) Math.random() * 50;
        float after = before + 100;

        return new FogSettings(true, Optional.empty(), Optional.of(new Vector2f(before, after)));
    }

    public static boolean isRaining(PlanetCreator creator) {
        return creator.temperature.temperature() < 20;
    }

    public static CustomVanillaObject getCustomVanillaObjet(PlanetCreator creator) {
        return new CustomVanillaObject(true, BASIC_SUN, 450, 200, true, true, BASIC_MOON, 75, 75);
    }

    public static Star getStars(PlanetCreator creator) {
        Vec3 color = new Vec3(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

        return new Star(
            creator.temperature == PlanetTemperature.TEMPERATE,
            Math.random() >= 0.2,
            (int) (Math.random() * 10000 + Math.random() * 10000),
            Math.random() > 0.8 || creator.temperature == PlanetTemperature.VERY_COLD,
            0.05f,
            color,
            Optional.of(new Star.ShootingStars(new Random().nextInt(960, 1000), new Vec2(20, 100), 0.08f,  0.5f, color, Optional.of(0)))
        );
    }

    public static SkyColor getSkyColor(PlanetCreator creator) {
        return new SkyColor(false, new Vector4f());
    }
}
