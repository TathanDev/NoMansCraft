package fr.tathan.nmc.common.creators;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.data.Codecs;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.utils.NetworkHelper;
import fr.tathan.nmc.common.utils.SystemBox;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class SystemCreator {

    private static final Supplier<Codec<SystemCreator>> CODEC_SUPPLIER = Suppliers.memoize(() ->
            RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(s -> s.name),
                    Codec.STRING.fieldOf("system").forGetter(s -> s.system),
                    Codecs.CELESTIAL_BODY.fieldOf("celestialBody").forGetter(s -> s.celestialBody),
                    Codec.STRING.listOf().fieldOf("planets").forGetter(s -> {
                        List<String> planetNames = new ArrayList<>();
                        for (PlanetCreator planet : s.planets) {
                            planetNames.add(planet.name);
                        }
                        return planetNames;
                    }),
                    SystemBox.CODEC.fieldOf("systemBox").forGetter(s -> s.systemBox)
            ).apply(instance, (name, system, celestialBody, planetNames, systemBox) -> {
                // Need to find the PlanetCreator objects based on the planetNames during deserialization
                // This requires access to the list of planets (e.g., from the SystemsData)
                // For now, we'll pass an empty list and fix it later (see deserialization notes below)
                return new SystemCreator(name, system, celestialBody, new ArrayList<>(), systemBox, planetNames);
            }))
    );

    public static Codec<SystemCreator> codec() {
        return CODEC_SUPPLIER.get();
    }

    public ArrayList<PlanetCreator> planets = new ArrayList<>();
    public final String name;
    public final String system;
    public final CelestialBody celestialBody;
    public SystemBox systemBox;
    private final List<String> planetNames;

    public SystemCreator(String name, String system, CelestialBody celestialBody, List<PlanetCreator> planets, SystemBox systemBox, List<String> planetNames) {
        this.name = name;
        this.system = system;
        this.celestialBody = celestialBody;
        this.planets = (ArrayList<PlanetCreator>) planets;
        this.systemBox = systemBox;
        this.planetNames = planetNames;

    }

    public SystemCreator() {
        this.name = Utils.generateGalaxyName();
        this.system = Utils.generateResourcelocation(name).getPath();
        this.celestialBody = generateStar();
        generateSystemBox(generatePlanets(), false);
        this.planetNames = new ArrayList<>();

    }

    public int generatePlanets() {
        Random random = new Random();
        int nbPlanets = random.nextInt(NoManCraft.getConfig().minPlanets, NoManCraft.getConfig().maxPlanets);
        AtomicInteger distance = new AtomicInteger(38);
        for (int i = 0; i < nbPlanets; i++) {
            planets.add(new PlanetCreator(this, distance.get()));
            distance.set(distance.get() + 38);
        }
        return distance.get();
    }

    public CelestialBody generateStar() {
        return new CelestialBody(ResourceLocation.fromNamespaceAndPath("nmc", "textures/planets/star.png"),
                this.name,
                //X of Solar System + width of Solar System + Number of Systems + random number between 0 and 1000
                300 + 140 + (Events.SYSTEMS.systems.size() * 100) + (float) (Math.random() * ( Math.random() * 1000 )),
                //Y of Solar System + width of Solar System + Number of Systems + random number between 0 and 1000
                100 + 140 + (Events.SYSTEMS.systems.size() * 100) + (float) (Math.random() * (Math.random() * 1000) + Math.random()),
                30,
                30,
                Utils.getRandomColor(),
                Utils.generateResourcelocation(this.name),
                this.name,
                this.system
        );
    }

    public void generateSystemBox(int furtherPlanet, boolean changeStarPos) {
        if(changeStarPos) {
            this.celestialBody.x = 300 + 140 + (Events.SYSTEMS.systems.size() * 100) + (float) (Math.random() * ( Math.random() * 1000 ));
            this.celestialBody.y = 100 + 140 + (Events.SYSTEMS.systems.size() * 100) + (float) (Math.random() * (Math.random() * 1000) + Math.random());
        }
        this.systemBox = new SystemBox((int) this.celestialBody.x, (int) this.celestialBody.y, furtherPlanet * 2);
    }

    public void changeStarPos() {
        this.changeStarPos(Events.SYSTEMS.systems);
    }

    public void changeStarPos(List<SystemCreator> systems) {
        for(SystemCreator systemCreator: systems) {
            while (systemCreator.systemBox.overlaps(this.systemBox)) {
                generateSystemBox(this.planets.getLast().distanceFromStar,  true);
            }
        }
    }

    public static void toNetwork(SystemCreator system, final RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(system.name);
        buffer.writeUtf(system.system);
        NetworkHelper.ToNetwork.celestialBody(buffer, system.celestialBody);
        buffer.writeInt(system.planetNames.size());
        system.planetNames.forEach(buffer::writeUtf);
        SystemBox.toNetwork(system.systemBox, buffer);
    }

    public static SystemCreator fromNetwork(final RegistryFriendlyByteBuf buffer) {
        var name = buffer.readUtf();
        var system = buffer.readUtf();
        var celestialBody = NetworkHelper.FromNetwork.celestialBody(buffer);

        int size = buffer.readInt();
        ArrayList<String> planets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            planets.add(buffer.readUtf());
        }
        var systemBox = SystemBox.fromNetwork(buffer);

        return new SystemCreator(
                name,
                system,
                celestialBody,
                new ArrayList<>(),
                systemBox,
                planets
        );
    }

    public ArrayList<PlanetCreator> getPlanets() {
        return planets;
    }

    public void setPlanets(ArrayList<PlanetCreator> planets) {
        this.planets = planets;
    }
}
