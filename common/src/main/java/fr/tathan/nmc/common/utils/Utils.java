package fr.tathan.nmc.common.utils;

import com.mojang.datafixers.util.Pair;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import dev.architectury.networking.NetworkManager;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.config.NMConfig;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.events.custom.PlanetsCreationLifecycle;
import fr.tathan.nmc.platform.DimensionUtil;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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


    public static ResourceLocation[] getHotBiomesList() {
        List<ResourceLocation> list = new ArrayList<>(getBiomesFromStrings(NoManCraft.getConfig().hotBiomes));
        return list.toArray(new ResourceLocation[0]);
    }

    public static ResourceLocation[] getVeryHotBiomesList() {
        List<ResourceLocation> list = new ArrayList<>(getBiomesFromStrings(NoManCraft.getConfig().veryHotBiomes));
        return list.toArray(new ResourceLocation[0]);
    }

    public static ResourceLocation[] getVeryColdBiomesList() {
        List<ResourceLocation> list = new ArrayList<>(getBiomesFromStrings(NoManCraft.getConfig().veryColdBiomes));
        return list.toArray(new ResourceLocation[0]);
    }

    public static ResourceLocation[] getColdBiomesList() {
        List<ResourceLocation> list = new ArrayList<>(getBiomesFromStrings(NoManCraft.getConfig().coldBiomes));
        return list.toArray(new ResourceLocation[0]);
    }

    public static ResourceLocation[] getTemperateBiomesList() {
        List<ResourceLocation> list = new ArrayList<>(getBiomesFromStrings(NoManCraft.getConfig().temperateBiomes));
        return list.toArray(new ResourceLocation[0]);
    }


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
        Planet planet = planetInfo.planet;
        Stellaris.LOG.error("Planet when Generating Temp : {}", planetInfo.temperature);

        context.registryAccess().registry(Registries.NOISE_SETTINGS).ifPresent((registry) -> {

            NoiseGeneratorSettings generatorSettings = generatorSettings(context.registryAccess(), planetInfo);
            Climate.ParameterList<Holder<Biome>> parameters = createParameters(context.registryAccess(), planetInfo);
            NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromList(parameters), registry.wrapAsHolder(generatorSettings));

            Registry<DimensionType> dimensionTypes = context.registryAccess().registry(Registries.DIMENSION_TYPE).get();

            Holder<DimensionType> holder;


            if(planetInfo.temperature == PlanetTemperature.VERY_HOT) {
                holder = dimensionTypes.getHolderOrThrow(BuiltinDimensionTypes.NETHER);
            } else {
                holder = dimensionTypes.getHolderOrThrow(BuiltinDimensionTypes.OVERWORLD);
            }

            PlanetsCreationLifecycle.PRE_PLANET_LEVEL_CREATION.invoker().prePlanetLevelCreation(planetInfo, generator, holder, context);

            ServerLevel level = DimensionUtil.createPlanet(context.getPlayer().getServer(), planet.dimension(), generator, holder);
            
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

        PlanetsCreationLifecycle.POST_BIOMES_SELECTION.invoker().postBiomeSelection(planetInfo, planetBiomes);

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
        int biomes = new Random().nextInt(NoManCraft.getConfig().minBiomes, NoManCraft.getConfig().maxBiomes);

        return switch (planetInfo.temperature) {
            case VERY_HOT -> getVeryHotBiomes(biomes);
            case HOT -> getHotBiomes(biomes);
            case COLD -> getColdBiomes(biomes);
            case VERY_COLD -> getVeryColdBiomes(biomes);
            default -> getTemperateBiomes(biomes);
        };
    }

    public static ArrayList<ResourceKey<Biome>> getTemperateBiomes(int biomeCount) {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < biomeCount) {
            int random = new Random().nextInt(getTemperateBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getTemperateBiomesList()[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getVeryColdBiomes(int biomeCount) {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < 3) {
            int random = new Random().nextInt(getColdBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getColdBiomesList()[random]));
        }
        while (biomes.size() < biomeCount) {
            int random = new Random().nextInt(getVeryColdBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getVeryColdBiomesList()[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getVeryHotBiomes(int biomeCount) {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < 3) {
            int random = new Random().nextInt(getHotBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getHotBiomesList()[random]));
        }
        while (biomes.size() < biomeCount) {
            int random = new Random().nextInt(getVeryHotBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getVeryHotBiomesList()[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getHotBiomes(int biomeCount) {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < biomeCount) {
            int random = new Random().nextInt(getHotBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getHotBiomesList()[random]));
        }
        return biomes;
    }

    public static ArrayList<ResourceKey<Biome>> getColdBiomes(int biomeCount) {
        ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();
        while (biomes.size() < biomeCount) {
            int random = new Random().nextInt(getColdBiomesList().length);
            biomes.add(ResourceKey.create(Registries.BIOME, getColdBiomesList()[random]));
        }
        return biomes;
    }


    public static NoiseGeneratorSettings generatorSettings(RegistryAccess registryAccess, PlanetCreator planetInfo) {
        return new NoiseGeneratorSettings(createNoiseSettings(), getDefaultBlock(planetInfo, registryAccess), getDefaultLiquid(planetInfo), NoiseRouterData.overworld(registryAccess.lookupOrThrow(Registries.DENSITY_FUNCTION), registryAccess.lookupOrThrow(Registries.NOISE), Math.random() > (double) NoManCraft.getConfig().largeWorldChance / 100, Math.random() > (double) NoManCraft.getConfig().amplifiedWorldChance / 100), SurfaceRuleData.overworld(), (new OverworldBiomeBuilder()).spawnTarget(), getSeaLevel(), false, true, true, false);
    }

    public static BlockState getDefaultBlock(PlanetCreator creator, RegistryAccess access) {
        return getDefaultBlockState(creator.temperature, access);
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
        } else if(chances < 8) {
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

    public static List<ResourceLocation> getBiomesFromStrings(List<String> biomes) {
        return biomes.stream().map(ResourceLocation::parse).toList();
    }

    public static String getPlanetTexture(PlanetCreator creator) {
        switch (creator.temperature) {
            case HOT -> {
                String[] strArray1 = new String[] {"nmc:textures/sky/hot_planet_1.png"};
                return strArray1[new Random().nextInt(strArray1.length)];
            }
            case VERY_HOT -> {
                String[] strArray2 = new String[] {"nmc:textures/sky/very_hot_planet_1.png"};
                return strArray2[new Random().nextInt(strArray2.length)];
            }
            case TEMPERATE -> {
                String[] strArray2 = new String[] {"nmc:textures/sky/temperate_planet_1.png"};
                return strArray2[new Random().nextInt(strArray2.length)];
            }
            default -> {
                String[] strArray2 = new String[] {"nmc:textures/sky/cold_planet_1.png", "nmc:textures/sky/cold_planet_2.png", "nmc:textures/sky/cold_planet_3.png"};
                return strArray2[new Random().nextInt(strArray2.length)];
            }
        }

    }

    public static BlockState getDefaultBlockState(PlanetTemperature temperature, RegistryAccess access) {
        return switch (temperature) {
            case VERY_COLD -> NMConfig.getRandomDefaultBlockLevel(NoManCraft.getConfig().possibleDefaultPlanetBlock.veryColdPlanetBlocks, access);
            case COLD -> NMConfig.getRandomDefaultBlockLevel(NoManCraft.getConfig().possibleDefaultPlanetBlock.coldPlanetBlocks, access);
            case TEMPERATE -> NMConfig.getRandomDefaultBlockLevel(NoManCraft.getConfig().possibleDefaultPlanetBlock.temperarePlanetBlocks, access);
            case HOT -> NMConfig.getRandomDefaultBlockLevel(NoManCraft.getConfig().possibleDefaultPlanetBlock.hotPlanetBlocks, access);
            case VERY_HOT -> NMConfig.getRandomDefaultBlockLevel(NoManCraft.getConfig().possibleDefaultPlanetBlock.veryHotPlanetBlocks, access);
        };
    }
}

