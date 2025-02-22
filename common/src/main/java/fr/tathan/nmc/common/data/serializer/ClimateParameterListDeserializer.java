package fr.tathan.nmc.common.data.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public class ClimateParameterListDeserializer {

    public static Climate.ParameterList<Holder<Biome>> deserialize(
            JsonElement jsonElement,
            RegistryAccess registryAccess
    ) throws JsonParseException {
        if (!jsonElement.isJsonArray()) {
            throw new JsonParseException("Expected a JSON array");
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> parameterList = new ArrayList<>();

        Registry<Biome> biomeRegistry = registryAccess
                .registryOrThrow(Registries.BIOME);
        HolderGetter<Biome> biomeHolderGetter = biomeRegistry.asLookup();

        for (JsonElement element : jsonArray) {
            if (!element.isJsonObject()) {
                throw new JsonParseException("Expected a JSON object within the array");
            }

            JsonObject pointObject = element.getAsJsonObject();

            Climate.Parameter temperature = parameterFromJson(
                    pointObject.get("temperature")
            );
            Climate.Parameter humidity = parameterFromJson(pointObject.get("humidity"));
            Climate.Parameter continentalness = parameterFromJson(
                    pointObject.get("continentalness")
            );
            Climate.Parameter erosion = parameterFromJson(pointObject.get("erosion"));
            Climate.Parameter depth = parameterFromJson(pointObject.get("depth"));
            Climate.Parameter weirdness = parameterFromJson(pointObject.get("weirdness"));
            long offset = pointObject.get("offset").getAsLong();

            ResourceLocation biomeId = ResourceLocation.parse(
                    pointObject.get("biome_id").getAsString()
            );
            ResourceKey<Biome> biomeKey = ResourceKey.create(
                    Registries.BIOME,
                    biomeId
            );

            Holder<Biome> biomeHolder = biomeHolderGetter
                    .getOrThrow(biomeKey);

            Climate.ParameterPoint point = new Climate.ParameterPoint(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    depth,
                    weirdness,
                    offset
            );

            parameterList.add(Pair.of(point, biomeHolder));
        }

        return new Climate.ParameterList<>(parameterList);
    }

    public static Climate.Parameter parameterFromJson(JsonElement jsonElement) {
        return Climate.Parameter.CODEC
                .decode(JsonOps.INSTANCE, jsonElement)
                .getOrThrow(e -> {
                    throw new JsonParseException("Failed to decode parameter: " + e);
                })
                .getFirst();
    }
}