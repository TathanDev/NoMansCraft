package fr.tathan.nmc.common.creators;

import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.utils.SystemBox;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SystemCreator {

    public ArrayList<PlanetCreator> planets = new ArrayList<>();
    public final String name;
    public final String system;
    public final CelestialBody celestialBody;
    public SystemBox systemBox;

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
                Component.literal(this.name),
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
}
