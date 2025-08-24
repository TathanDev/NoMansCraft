package fr.tathan.nmc.common.creators;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.MoonInfo;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.PlanetTextures;
import fr.tathan.nmc.common.utils.NetworkHelper;
import fr.tathan.nmc.common.utils.PlanetTemperature;
import fr.tathan.nmc.common.utils.SkyUtils;
import fr.tathan.nmc.common.utils.Utils;
import fr.tathan.sky_aesthetics.client.skies.record.SkyProperties;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Optional;
import java.util.function.Supplier;

public class MoonCreator {

    private static final Supplier<Codec<MoonCreator>> CODEC_SUPPLIER = Suppliers.memoize(() ->
            RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(s -> s.name),
                    Codec.INT.fieldOf("temperature").forGetter(s -> s.temperature.temperature()), // Store system name
                    PlanetInfo.CODEC.fieldOf("orbitCenter").forGetter(s -> s.orbitCenter),
                    Codec.STRING.fieldOf("systemName").forGetter(s -> s.systemName),
                    MoonInfo.CODEC.fieldOf("moonInfo").forGetter(s -> s.moonInfo),
                    SkyProperties.CODEC.fieldOf("sky").forGetter(s -> s.sky),
                    Planet.CODEC.fieldOf("planet").forGetter(s -> s.planet)
            ).apply(instance, MoonCreator::new))
    );

    public final String name;
    public final PlanetTemperature temperature;
    public final MoonInfo moonInfo;
    public final SkyProperties sky;
    private final String systemName;
    private PlanetInfo orbitCenter;
    public Planet planet;

    public MoonCreator(String name, int temperature, PlanetInfo orbitCenter, String systemName, MoonInfo moonInfo, SkyProperties sky, Planet planet) {
        this.name = name;
        this.temperature = PlanetTemperature.fromInt(temperature);
        this.orbitCenter = orbitCenter;
        this.systemName = systemName;
        this.moonInfo = moonInfo;
        this.sky = sky;
        this.planet = planet;
    }

    public static void toNetwork(MoonCreator system, final RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(system.name);
        buffer.writeInt(system.temperature.temperature());
        NetworkHelper.ToNetwork.planetInfo(buffer, system.orbitCenter);
        buffer.writeUtf(system.systemName);
        NetworkHelper.ToNetwork.moonInfo(buffer, system.moonInfo);
        NetworkHelper.ToNetwork.skyProperties(buffer, system.sky);
        NetworkHelper.ToNetwork.planet(buffer, system.planet);
    }

    public static MoonCreator fromNetwork(final RegistryFriendlyByteBuf buffer) {
        String name = buffer.readUtf();
        int temperature = buffer.readInt();
        PlanetInfo orbitCenter = NetworkHelper.FromNetwork.planetInfo(buffer);
        String systemName = buffer.readUtf();
        MoonInfo moonInfo = NetworkHelper.FromNetwork.moonInfo(buffer);
        SkyProperties sky = NetworkHelper.FromNetwork.skyProperties(buffer);
        Planet planet = NetworkHelper.FromNetwork.planet(buffer);
        return new MoonCreator(name, temperature, orbitCenter, systemName, moonInfo, sky, planet);
    }


    public MoonCreator(PlanetCreator mainPlanet) {
        this.name = Utils.generatePlanetName();
        this.orbitCenter = mainPlanet.planetInfo;
        this.temperature = mainPlanet.temperature;
        this.systemName = mainPlanet.system.name; // Store the system name
        this.planet = setPlanetInfo();
        this.moonInfo = this.moonInfo();
        this.sky = SkyUtils.createSky(mainPlanet);
    }

    public Planet setPlanetInfo() {
        boolean oxygen = Math.random() > 0.2;
        float gravity = (float) Mth.clamp((Math.random() + Math.random()) * 10, 0.1, 12);

        return new Planet(this.systemName, "planet.nmc." + Utils.generateResourcelocation(this.name).getPath(), this.name, Utils.generateResourcelocation(this.name), Optional.empty(), Optional.empty(), oxygen, temperature.temperature(), 900000, gravity, Optional.empty(),
                new PlanetTextures(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/planet.png"), ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/planet.png")));
    }

    public MoonInfo moonInfo() {
        return new MoonInfo(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/star.png"),
                this.name,
                30,
                100l,
                10,
                10,
                this.orbitCenter,
                Utils.generateResourcelocation(this.name),
                this.name,
                Utils.generateResourcelocation(this.name).getPath()

        );
    }

    public static Codec<MoonCreator> codec() {
        return CODEC_SUPPLIER.get();
    }


}
