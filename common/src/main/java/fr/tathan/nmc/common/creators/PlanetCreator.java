package fr.tathan.nmc.common.creators;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.PlanetTextures;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.utils.*;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;
import fr.tathan.sky_aesthetics.client.skies.record.SkyProperties;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PlanetCreator {

    private static final Supplier<Codec<PlanetCreator>> CODEC_SUPPLIER = Suppliers.memoize(() ->
            RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.BOOL.fieldOf("canHaveMoon").forGetter(s -> s.canHaveMoon),
                    Codec.STRING.fieldOf("systemName").forGetter(s -> s.system.name), // Store system name
                    Codec.INT.fieldOf("distanceFromStar").forGetter(s -> s.distanceFromStar),
                    Codec.STRING.fieldOf("name").forGetter(s -> s.name),
                    Planet.CODEC.fieldOf("planet").forGetter(s -> s.planet),
                    PlanetInfo.CODEC.fieldOf("planetInfo").forGetter(s -> s.planetInfo),
                    MoonCreator.codec().listOf().fieldOf("moons").forGetter(s -> s.moons), // Recursive call
                    SkyProperties.CODEC.fieldOf("sky").forGetter(s -> s.sky)
            ).apply(instance, (canHaveMoon, systemName, distanceFromStar, name, planet, planetInfo, moons, sky) -> {
                // Need to find the SystemCreator based on the systemName during deserialization
                // This requires access to the list of systems (e.g., Events.SYSTEMS)
                // For now, we'll pass null and fix it later (see deserialization notes below)
                return new PlanetCreator(canHaveMoon, null, distanceFromStar, name, planet, planetInfo, moons, sky, systemName);
            }))
    );

    public static Codec<PlanetCreator> codec() {
        return CODEC_SUPPLIER.get();
    }

    public final boolean canHaveMoon;
    public List<MoonCreator> moons = new ArrayList<>();
    public final Planet planet;
    public SystemCreator system;
    public final String name;
    public final PlanetTemperature temperature = PlanetTemperature.randomTemperature();
    public final PlanetInfo planetInfo;
    public final int distanceFromStar;
    public final SkyProperties sky;
    private final String systemName;


    public PlanetCreator(boolean canHaveMoon, SystemCreator system, int distanceFromStar, String name, Planet planet, PlanetInfo planetInfo, List<MoonCreator> moons, SkyProperties sky, String systemName) {
        this.canHaveMoon = canHaveMoon;
        this.system = system;
        this.distanceFromStar = distanceFromStar;
        this.name = name;
        this.planet = planet;
        this.planetInfo = planetInfo;
        this.moons = moons;
        this.sky = sky;
        this.systemName = systemName;

    }

    public static void toNetwork(PlanetCreator system, final RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(system.canHaveMoon);
        //Null because we pass a null instance of SystemCreator
        buffer.writeInt(system.distanceFromStar);
        buffer.writeUtf(system.name);
        NetworkHelper.ToNetwork.planet(buffer, system.planet);
        NetworkHelper.ToNetwork.planetInfo(buffer, system.planetInfo);
        buffer.writeInt(system.moons.size());
        system.moons.forEach((moon) -> {
            MoonCreator.toNetwork(moon, buffer);
        });
        NetworkHelper.ToNetwork.skyProperties(buffer, system.sky);
        buffer.writeUtf(system.systemName);
    }

    public static PlanetCreator fromNetwork(final RegistryFriendlyByteBuf buffer) {
        var canHaveMoon = buffer.readBoolean();
        var distanceFromStar = buffer.readInt();
        var name = buffer.readUtf();
        var planet = NetworkHelper.FromNetwork.planet(buffer);
        var planetInfo = NetworkHelper.FromNetwork.planetInfo(buffer);
        var moons = new ArrayList<MoonCreator>();
        for (int i = 0; i < buffer.readInt(); i++) {
            moons.add(MoonCreator.fromNetwork(buffer));
        }
        var sky = NetworkHelper.FromNetwork.skyProperties(buffer);
        var systemName = buffer.readUtf();
        return new PlanetCreator(canHaveMoon, null, distanceFromStar, name, planet, planetInfo, moons, sky, systemName);
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
        this.systemName = system.name; // Store the system name

    }

    public ArrayList<MoonCreator> createMoons() {
        ArrayList<MoonCreator> moons = new ArrayList<>();
        if(canHaveMoon) {
            moons.add(new MoonCreator(this));
        }
        return moons;
    }

    public Planet setPlanetInfo() {
        boolean oxygen = Math.random() > (double) NoManCraft.getConfig().oxygenChance / 100;
        float gravity = (float) Mth.clamp((Math.random() + Math.random()) * 10, 0.1, 12);

        return new Planet(this.system.system, "planet.nmc." + Utils.generateResourcelocation(this.name).getPath(), this.name, Utils.generateResourcelocation(this.name), oxygen, temperature.temperature(), NoManCraft.getConfig().planetDistanceFromEarth, gravity,
                 new PlanetTextures(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/planet.png"), ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/planet.png")));
    }

    public PlanetInfo planetInfo() {
        return new PlanetInfo(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/star.png"),
                this.name,
                this.distanceFromStar,
                (int) Math.clamp(((Math.random() * 10000) - distanceFromStar * 100L) + this.distanceFromStar * 2, 1000, 20000),
                10,
                10,
                this.system.celestialBody,
                Utils.generateResourcelocation(this.name),
                this.name,
                Utils.generateResourcelocation(this.name).getPath()
        );
    }
    public void setSystem(SystemCreator system) {
        this.system = system;
    }

    public String getSystemName() {
        return systemName;
    }

    public boolean isCanHaveMoon() {
        return canHaveMoon;
    }

    public int getDistanceFromStar() {
        return distanceFromStar;
    }

    public String getName() {
        return name;
    }

    public Planet getPlanet() {
        return planet;
    }

    public PlanetInfo getPlanetInfo() {
        return planetInfo;
    }


    public PlanetSky getSky() {
        return new PlanetSky(sky);
    }


}
