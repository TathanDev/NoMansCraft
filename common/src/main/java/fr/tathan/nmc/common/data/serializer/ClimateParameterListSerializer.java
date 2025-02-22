package fr.tathan.nmc.common.data.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClimateParameterListSerializer {

    public static JsonElement serialize(Climate.ParameterList<Holder<Biome>> parameterList) {
        JsonArray array = new JsonArray();
        for (Pair<Climate.ParameterPoint, Holder<Biome>> pair : parameterList.values()) { // Assuming values() returns the points
            JsonObject pointObject = new JsonObject();

            Climate.ParameterPoint point = pair.getFirst();

            pointObject.add("temperature", parameterJson(point.temperature()));
            pointObject.add("humidity", parameterJson(point.humidity()));
            pointObject.add("continentalness", parameterJson(point.continentalness()));
            pointObject.add("erosion", parameterJson(point.erosion()));
            pointObject.add("depth", parameterJson(point.depth()));
            pointObject.add("weirdness", parameterJson(point.weirdness()));
            pointObject.addProperty("offset", point.offset());

            // Serialize the biome Holder
            if (pair.getSecond() != null && pair.getSecond().value() != null) {
                pointObject.addProperty("biome_id", pair.getSecond().getRegisteredName()); // Or use a registry key
            }
            array.add(pointObject);
        }
        return array;
    }

    public static JsonElement parameterJson(Climate.Parameter instance) {
        return Climate.Parameter.CODEC
                .encodeStart(JsonOps.INSTANCE, instance)
                .result()
                .orElseThrow(() -> new IllegalStateException("Failed to encode to JSON"));
    }}