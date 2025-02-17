package fr.tathan.nmc.common.creators;

import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.PlanetTextures;
import fr.tathan.nmc.common.utils.PlanetTemperature;
import fr.tathan.nmc.common.utils.SkyUtils;
import fr.tathan.nmc.common.utils.Utils;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Random;

public class PlanetCreator {

    public final boolean canHaveMoon;
    public ArrayList<PlanetCreator> moons = new ArrayList<>();
    public final Planet planet;
    public final SystemCreator system;
    public final String name;
    public final PlanetTemperature temperature = PlanetTemperature.randomTemperature();
    public final PlanetInfo planetInfo;
    public final int distanceFromStar;
    public final PlanetSky sky;

    public PlanetCreator(SystemCreator system) {
        this(false, system, 38);
    }

    public PlanetCreator(SystemCreator system, int distanceFromStar) {
        this(false, system, distanceFromStar);
    }


    public PlanetCreator(boolean canHaveMoon, SystemCreator system, int distanceFromStar) {
        this.canHaveMoon = canHaveMoon;
        this.system = system;
        this.distanceFromStar = distanceFromStar;
        this.name = Utils.generatePlanetName();
        this.planet = setPlanetInfo();
        this.planetInfo = planetInfo();
        createMoons();
        this.sky = SkyUtils.createSky(this);
    }

    public void createMoons() {
        if(canHaveMoon) {
            Random random = new Random();
            for(int i = 0; i < random.nextInt(3); i++) {
                moons.add(new PlanetCreator(false, this.system, 10 + i * 10));
            }
        }
    }

    public Planet setPlanetInfo() {
        boolean oxygen = Math.random() > 0.2;
        float gravity = (float) Mth.clamp((Math.random() + Math.random()) * 10, 0.1, 12);

        return new Planet(this.system.system, "planet.nmc." + Utils.generateResourcelocation(this.name).getPath(), this.name, Utils.generateResourcelocation(this.name), oxygen, temperature.temperature(), 900000, gravity,
                 new PlanetTextures(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/planet.png"), ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/planet.png")));
    }

    public PlanetInfo planetInfo() {
        return new PlanetInfo(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/star.png"),
                this.name,
                this.distanceFromStar,
                (int) (Math.random() * 10000) - distanceFromStar * 100L,
                10,
                10,
                this.system.celestialBody,
                ResourceKey.create(Registries.DIMENSION, Utils.generateResourcelocation(this.name)),
                Component.translatable(this.name),
                this.name
        );
    }



}
