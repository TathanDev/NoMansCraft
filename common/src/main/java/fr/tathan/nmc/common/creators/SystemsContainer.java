package fr.tathan.nmc.common.creators;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class SystemsContainer {

    public final List<SystemCreator> systems;


    public SystemsContainer() {
        this.systems = new ArrayList<>();
    }

    public SystemsContainer(List<SystemCreator> systems) {
        this.systems = systems;
    }

    public static final Codec<SystemsContainer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            SystemCreator.codec().listOf().fieldOf("systems").forGetter(s -> s.systems)
    ).apply(instance, SystemsContainer::new));

    public static JsonElement toJson(SystemsContainer instance) {
        return CODEC
                .encodeStart(JsonOps.INSTANCE, instance)
                .result()
                .orElseThrow(() -> new IllegalStateException("Failed to encode to JSON"));
    }

}
