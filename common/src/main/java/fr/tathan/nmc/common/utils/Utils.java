package fr.tathan.nmc.common.utils;

import com.mojang.datafixers.util.Pair;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import dev.architectury.networking.NetworkManager;
import dev.galacticraft.dynamicdimensions.api.DynamicDimensionRegistry;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.*;

public class Utils {

    public static final String[] PlANETS_NAME_PART_1 = {
            "Nova", "Vera", "Zephyr", "Orion", "Sol", "Erebus", "Vanta",
            "Lyra", "Zenith", "Nexus", "Astra", "Draconis", "Horizon",
            "Celestis", "Pyra"
    };

    public static final String[] PlANETS_NAME_PART_2 = {
            "Prime", "Lux", "Vex", "Crest", "Vanta", "Core", "Helix",
            "Echo", "Aros", "Arc", "Vox", "Vale", "Theta", "Umbra", "Solis"
    };

    public static final String[] GALAXY_PART_1 = {
            "Andara", "Zenith", "Nebula", "Celest", "Vortex", "Eclipse", "Lyra",
            "Orionis", "Nexus", "Aether", "Draconis", "Solara", "Hyperion", "Vega", "Quasar"
    };

    public static final String[] GALAXY_PART_2 = {
            "Cluster", "Expanse", "Void", "Spiral", "Nebula", "Belt", "Dominion",
            "Arm", "Drift", "Halo", "Core", "Horizon", "Sphere", "Frontier", "Sanctum"
    };

    public static ResourceLocation[] VERY_COLD_BIOMES = {
            Biomes.SNOWY_TAIGA.location(),
            Biomes.FROZEN_OCEAN.location(),
            Biomes.FROZEN_RIVER.location(),
            Biomes.DEEP_COLD_OCEAN.location(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mars_ice_spikes"),
            Biomes.ICE_SPIKES.location()
    };

    public static ResourceLocation[] COLD_BIOMES = {
            Biomes.TAIGA.location(),
            Biomes.SNOWY_TAIGA.location(),
            Biomes.FROZEN_OCEAN.location(),
            Biomes.FROZEN_RIVER.location(),
            Biomes.DEEP_COLD_OCEAN.location(),
            Biomes.OCEAN.location(),
            Biomes.FROZEN_OCEAN.location()
    };

    public static ResourceLocation[] VERY_HOT_BIOMES = {
            Biomes.NETHER_WASTES.location(),
            Biomes.BASALT_DELTAS.location(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mercury"),
            ResourceLocation.fromNamespaceAndPath("stellaris", "infernal_venus_barrens")
    };

    public static ResourceLocation[] HOT_BIOMES = {
            Biomes.DESERT.location(),
            Biomes.SAVANNA_PLATEAU.location(),
            Biomes.BADLANDS.location(),
    };


    public static String generatePlanetName() {
        return PlANETS_NAME_PART_1[new Random().nextInt(PlANETS_NAME_PART_1.length)] + " " + PlANETS_NAME_PART_2[new Random().nextInt(PlANETS_NAME_PART_2.length)];

    }

    public static String generateGalaxyName() {
        return GALAXY_PART_1[new Random().nextInt(GALAXY_PART_1.length)] + " " + GALAXY_PART_2[new Random().nextInt(GALAXY_PART_2.length)];
    }

    public static ResourceLocation generateResourcelocation(String name) {
        name = name.toLowerCase(Locale.ROOT);
        name = name.replace(" ", "_");
        return ResourceLocation.fromNamespaceAndPath("nmc", name);
    }

    public static void generateWorld(NetworkManager.PacketContext context, PlanetCreator planetInfo) {
        DynamicDimensionRegistry dimRegistry = DynamicDimensionRegistry.from(context.getPlayer().getServer());
        Planet planet = planetInfo.planet;

        context.registryAccess().registry(Registries.NOISE_SETTINGS).ifPresent((registry) -> {

            NoiseGeneratorSettings generatorSettings = generatorSettings(context.registryAccess(), planetInfo);
            DimensionType type = new DimensionType(OptionalLong.empty(), true, false, planet.temperature() >= 50, true, 1.0D, true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, (float) Mth.clamp(Math.random(), 0f, 0.2f), new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0));


            NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromList(createParameters(context.registryAccess(), planetInfo)), registry.wrapAsHolder(generatorSettings));

            ServerLevel level = dimRegistry.createDynamicDimension(planet.dimension(), generator, type);

        });
    }

    public static Climate.ParameterList<Holder<Biome>> createParameters(RegistryAccess registryAccess, PlanetCreator planetInfo) {
        Climate.Parameter fullRange = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter temps1 = Climate.Parameter.span(-1.0F, -0.8F);
        Climate.Parameter temps2 = Climate.Parameter.span(-0.8F, 0.0F);
        Climate.Parameter temps3 = Climate.Parameter.span(0.0F, 0.4F);
        Climate.Parameter temps4 = Climate.Parameter.span(0.4F, 0.93F);
        Climate.Parameter temps5 = Climate.Parameter.span(0.93F, 0.94F);
        Climate.Parameter temps6 = Climate.Parameter.span(0.94F, 1.0F);

        HolderGetter<Biome> biomes = registryAccess.lookupOrThrow(Registries.BIOME);
        ArrayList<ResourceKey<Biome>> planetBiomes = getBiomes(planetInfo);

        return new Climate.ParameterList<>(List.of(
                // Row 1
                Pair.of(new Climate.ParameterPoint(temps1, fullRange, fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                // Row 2
                Pair.of(new Climate.ParameterPoint(temps2, Climate.Parameter.span(-1.0F, 0.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps2, Climate.Parameter.span(0.0F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                // Row 3
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(-1.0F, 0.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(0.0F, 0.8F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(0.8F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                // Row 4
                Pair.of(new Climate.ParameterPoint(temps4, Climate.Parameter.span(-1.0F, -0.1F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps4, Climate.Parameter.span(-0.1F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                // Row 5
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-1.0F, -0.6F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-0.6F, -0.3F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-0.3F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                // Row 6
                Pair.of(new Climate.ParameterPoint(temps6, Climate.Parameter.span(-1.0F, -0.1F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps6, Climate.Parameter.span(-0.1F, 0.8F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes))),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(0.8F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(getRandomBiome(planetBiomes)))
        ));
    }

    public static ResourceKey<Biome> getRandomBiome(ArrayList<ResourceKey<Biome>> biomes) {
        return biomes.get(new Random().nextInt(biomes.size()));
    }

    public static ArrayList<ResourceKey<Biome>> getBiomes(PlanetCreator planetInfo) {
        return switch (planetInfo.temperature) {
            case VERY_HOT -> getVeryHotBiomes();
            case VERY_COLD -> getVeryColdBiomes();
            case HOT -> getHotBiomes();
            case COLD -> getColdBiomes();
            default -> getVeryColdBiomes();
        };
    }


    public static ArrayList<ResourceKey<Biome>> getVeryColdBiomes() {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < 7) {
            int random = new Random().nextInt(VERY_COLD_BIOMES.length);
            biomes.add(ResourceKey.create(Registries.BIOME, VERY_COLD_BIOMES[random]));
        }
        while (biomes.size() < 10) {
            int random = new Random().nextInt(COLD_BIOMES.length);
            biomes.add(ResourceKey.create(Registries.BIOME, COLD_BIOMES[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getVeryHotBiomes() {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < 7) {
            int random = new Random().nextInt(VERY_HOT_BIOMES.length);
            biomes.add(ResourceKey.create(Registries.BIOME, VERY_HOT_BIOMES[random]));
        }
        while (biomes.size() < 10) {
            int random = new Random().nextInt(HOT_BIOMES.length);
            biomes.add(ResourceKey.create(Registries.BIOME, HOT_BIOMES[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getHotBiomes() {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < 10) {
            int random = new Random().nextInt(HOT_BIOMES.length);
            biomes.add(ResourceKey.create(Registries.BIOME, HOT_BIOMES[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getColdBiomes() {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < 10) {
            int random = new Random().nextInt(COLD_BIOMES.length);
            biomes.add(ResourceKey.create(Registries.BIOME, COLD_BIOMES[random]));
        }
        return biomes;
    }


    public static NoiseGeneratorSettings generatorSettings(RegistryAccess registryAccess, PlanetCreator planetInfo) {
        return new NoiseGeneratorSettings(createNoiseSettings(), getDefaultBlock(planetInfo), getDefaultLiquid(planetInfo), NoiseRouterData.overworld(registryAccess.lookupOrThrow(Registries.DENSITY_FUNCTION), registryAccess.lookupOrThrow(Registries.NOISE), Math.random() > 0.8, Math.random() > 0.85), SurfaceRuleData.overworld(), (new OverworldBiomeBuilder()).spawnTarget(), getSeaLevel(), false, true, true, false);
    }

    public static BlockState getDefaultBlock(PlanetCreator creator) {
        if( creator.temperature == PlanetTemperature.VERY_HOT) {
            return Blocks.MAGMA_BLOCK.defaultBlockState();
        }
        return creator.temperature == PlanetTemperature.VERY_COLD ? Blocks.PACKED_ICE.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }

    public static BlockState getDefaultLiquid(PlanetCreator creator) {
        if(creator.temperature == PlanetTemperature.VERY_HOT) {
            return Blocks.LAVA.defaultBlockState();
        }
        return Blocks.WATER.defaultBlockState();
    }

    public static NoiseSettings createNoiseSettings() {
        return NoiseSettings.create(-64 - (new Random().nextInt(-1, 3) * 16), 384 + new Random().nextInt(-2, 2) * 16, 1, 2);
    }

    public static int getSeaLevel() {
        int chances = new Random().nextInt(10);
        if(chances < 2) {
            return new Random().nextInt(100);
        } else if(chances < 7) {
            return 64;
        } else if (chances < 9) {
            return new Random().nextInt(40, 90);
        } else {
            return 0;
        }
    }

    public static int getRandomColor() {
        return new Random().nextInt(0xffffff + 1);
    }

    public static ArrayList<PlanetCreator> getPlanetsInSystem(String name, SystemsContainer container) {
        ArrayList<PlanetCreator> planets = new ArrayList<>();
        container.planets.forEach((planetCreator -> {
            if(planetCreator.getSystemName().equals(name)) {
                planets.add(planetCreator);
            }
        }));
        return planets;
    }
}
