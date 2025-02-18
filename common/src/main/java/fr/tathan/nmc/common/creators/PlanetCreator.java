package fr.tathan.nmc.common.creators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.PlanetTextures;
import fr.tathan.nmc.common.data.Codecs;
import fr.tathan.nmc.common.utils.PlanetTemperature;
import fr.tathan.nmc.common.utils.SkyUtils;
import fr.tathan.nmc.common.utils.SystemBox;
import fr.tathan.nmc.common.utils.Utils;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlanetCreator {

    public static final Codec<PlanetCreator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("canHaveMoon").forGetter(s -> s.canHaveMoon),
            SystemCreator.CODEC.fieldOf("name").forGetter(s -> s.system),
            Codec.INT.fieldOf("distanceFromStar").forGetter(s -> s.distanceFromStar),
            Codec.STRING.fieldOf("name").forGetter(s -> s.name),
            Planet.CODEC.fieldOf("planet").forGetter(s -> s.planet),

            PlanetInfo.CODEC.fieldOf("planetInfo").forGetter(s -> s.planetInfo),
            Codec.list(PlanetCreator.CODEC).fieldOf("moons").forGetter(s -> s.moons),
            Codecs.SKY.fieldOf("sky").forGetter(s -> s.sky)
    ).apply(instance, PlanetCreator::new));


    public final boolean canHaveMoon;
    public ArrayList<PlanetCreator> moons = new ArrayList<>();
    public final Planet planet;
    public final SystemCreator system;
    public final String name;
    public final PlanetTemperature temperature = PlanetTemperature.randomTemperature();
    public final PlanetInfo planetInfo;
    public final int distanceFromStar;
    public final PlanetSky sky;


    public PlanetCreator(boolean canHaveMoon, SystemCreator system, int distanceFromStar, String name, Planet planet, PlanetInfo planetInfo, List<PlanetCreator> moons, PlanetSky sky) {
        this.canHaveMoon = canHaveMoon;
        this.system = system;
        this.distanceFromStar = distanceFromStar;
        this.name = name;
        this.planet = planet;
        this.planetInfo = planetInfo;
        this.moons = (ArrayList<PlanetCreator>) moons;
        this.sky = sky;
    }

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
        this.moons = createMoons();
        this.sky = SkyUtils.createSky(this);
    }




    public ArrayList<PlanetCreator> createMoons() {
        ArrayList<PlanetCreator> moons = new ArrayList<>();
        if(canHaveMoon) {
            Random random = new Random();
            for(int i = 0; i < random.nextInt(3); i++) {
                moons.add(new PlanetCreator(false, this.system, 10 + i * 10));
            }
        }
        return moons;
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
                Utils.generateResourcelocation(this.name),
                this.name,
                this.name
        );
    }



}
