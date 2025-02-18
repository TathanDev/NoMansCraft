package fr.tathan.nmc.common.creators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.utils.SystemBox;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WorldData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SystemCreator {

    public static final Codec<SystemCreator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(s -> s.name),
            Codec.STRING.fieldOf("system").forGetter(s -> s.system),
            CelestialBody.CODEC.fieldOf("celestialBody").forGetter(s -> s.celestialBody),
            PlanetCreator.CODEC.listOf().fieldOf("planets").forGetter(s -> s.planets),
            SystemBox.CODEC.fieldOf("systemBox").forGetter(s -> s.systemBox)

    ).apply(instance, instance.stable(SystemCreator::new)));


    public ArrayList<PlanetCreator> planets = new ArrayList<>();
    public final String name;
    public final String system;
    public final CelestialBody celestialBody;
    public SystemBox systemBox;

    public SystemCreator(String name, String system, CelestialBody celestialBody, List<PlanetCreator> planets, SystemBox systemBox) {
        this.name = name;
        this.system = system;
        this.celestialBody = celestialBody;
        this.planets = (ArrayList<PlanetCreator>) planets;
        this.systemBox = systemBox;
    }

    public SystemCreator() {
        this.name = Utils.generateGalaxyName();
        this.system = Utils.generateResourcelocation(name).getPath();
        this.celestialBody = generateStar();
        generateSystemBox(generatePlanets(), false);
    }

    public int generatePlanets() {
        Random random = new Random();
        int nbPlanets = random.nextInt(4) + 1;
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
                300 + 140 + (Events.SYSTEMS.size() * 100) + (float) (Math.random() * ( Math.random() * 1000 )),
                //Y of Solar System + width of Solar System + Number of Systems + random number between 0 and 1000
                100 + 140 + (Events.SYSTEMS.size() * 100) + (float) (Math.random() * (Math.random() * 1000) + Math.random()),
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

            this.celestialBody.x = 300 + 140 + (Events.SYSTEMS.size() * 100) + (float) (Math.random() * ( Math.random() * 1000 ));
            this.celestialBody.y = 100 + 140 + (Events.SYSTEMS.size() * 100) + (float) (Math.random() * (Math.random() * 1000) + Math.random());
        }

        this.systemBox = new SystemBox((int) this.celestialBody.x, (int) this.celestialBody.y, furtherPlanet);
    }

    public void changeStarPos() {
        for(SystemCreator systemCreator: Events.SYSTEMS) {
            while (systemCreator.systemBox.overlaps(this.systemBox)) {
                generateSystemBox(this.planets.getLast().distanceFromStar,  true);
            }
        }
    }

    public ArrayList<PlanetCreator> getPlanets() {
        return planets;
    }

    public String getName() {
        return name;
    }

    public String getSystem() {
        return system;
    }
}
