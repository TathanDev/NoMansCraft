package fr.tathan.nmc.common.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.data.serializer.ClimateParameterListDeserializer;
import fr.tathan.nmc.common.data.serializer.ClimateParameterListSerializer;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class DimensionData {

    public static void saveDimensionData(ServerLevel level, String dimType, Holder<NoiseGeneratorSettings> settings, Climate.ParameterList<Holder<Biome>> parameters) {
        Path systemsFile = level.getServer().storageSource.getDimensionPath(level.dimension()).resolve("dimension-infos.json");
        try {
            File folder = systemsFile.toFile().getParentFile();
            if (!folder.exists())
                folder.mkdirs();

            JsonObject json = new JsonObject();
            json.addProperty("dimType", dimType);
            json.add("noise", serializeNoise(settings));
            json.add("parameters", ClimateParameterListSerializer.serialize(parameters));

            String file = NoManCraft.gson.toJson(json);

            var dimensionFile = Files.newBufferedWriter(systemsFile);
            dimensionFile.write(file);
            dimensionFile.close();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static DimensionData.DimensionInfos readDimensionData(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        Path systemsFile = server.storageSource.getDimensionPath(levelResourceKey).resolve("dimension-infos.json");
        try {

            BufferedReader reader = Files.newBufferedReader(systemsFile);
            JsonElement jsonElement = GsonHelper.parse(reader);
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "dimension");

            Holder<NoiseGeneratorSettings> noise = NoiseGeneratorSettings.CODEC.decode(JsonOps.INSTANCE, json.get("noise")).getOrThrow().getFirst();
            Climate.ParameterList<Holder<Biome>> parameters = ClimateParameterListDeserializer.deserialize(json.get("parameters"), server.registryAccess());

            return new DimensionData.DimensionInfos(
                    json.get("dimType").getAsString(),
                    noise,
                    parameters
            );
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }


    public static JsonElement serializeNoise(Holder<NoiseGeneratorSettings> settings) {
        return NoiseGeneratorSettings.CODEC
                .encodeStart(JsonOps.INSTANCE, settings)
                .result().orElseThrow(RuntimeException::new);
    }

    public record DimensionInfos(String dimType, Holder<NoiseGeneratorSettings> noise, Climate.ParameterList<Holder<Biome>> parameters) {
    }

}
