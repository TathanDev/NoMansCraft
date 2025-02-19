package fr.tathan.nmc.common.creators;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class SystemsContainer {

    public final List<SystemCreator> systems;
    public List<PlanetCreator> planets;

    public SystemsContainer() {
        this.systems = new ArrayList<>();
        this.planets = new ArrayList<>();;
    }

    public SystemsContainer(List<SystemCreator> systems, List<PlanetCreator> planets) {
        this.systems = systems;
        this.planets = planets;
    }

    public static final Codec<SystemsContainer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            SystemCreator.codec().listOf().fieldOf("systems").forGetter(s -> s.systems),
            PlanetCreator.codec().listOf().fieldOf("planets").forGetter(s -> s.planets)
    ).apply(instance, SystemsContainer::new));

    public static JsonElement toJson(SystemsContainer instance) {
        return CODEC
                .encodeStart(JsonOps.INSTANCE, instance)
                .result()
                .orElseThrow(() -> new IllegalStateException("Failed to encode to JSON"));
    }

    public static RegistryFriendlyByteBuf toNetwork(SystemsContainer container, final RegistryFriendlyByteBuf buffer) {

        buffer.writeInt(container.systems.size());
        container.systems.forEach((systemCreator -> {
            SystemCreator.toNetwork(systemCreator, buffer);
        }));

        buffer.writeInt(container.planets.size());
        container.planets.forEach(((planet) -> {
            PlanetCreator.toNetwork(planet, buffer);
        }));

        return buffer;
    }

    public static SystemsContainer fromNetwork(final RegistryFriendlyByteBuf buffer) {

        List<SystemCreator> systems = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            systems.add(SystemCreator.fromNetwork(buffer));
        }
        int size2 = buffer.readInt();

        List<PlanetCreator> planets = new ArrayList<>();
        for (int i = 0; i < size2; i++) {
            planets.add(PlanetCreator.fromNetwork(buffer));
        }

        return new SystemsContainer(systems, planets);
    }

}
