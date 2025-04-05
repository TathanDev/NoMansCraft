package fr.tathan.nmc.common.config;

import fr.tathan.nmc.NoManCraft;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Config(name = NoManCraft.MODID)
public class NMConfig implements ConfigData {

    @ConfigEntry.Category("systems")
    public int minSystems = 5;

    @ConfigEntry.Category("systems")
    public int maxSystems = 10;

    @ConfigEntry.Category("systems")
    public int minPlanets = 2;

    @ConfigEntry.Category("systems")
    public int maxPlanets = 8;

    @ConfigEntry.Category("systems")
    public int planetDistanceFromEarth = 900000;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int largeWorldChance = 80;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int amplifiedWorldChance = 85;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int oxygenChance = 20;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int stormyPlanetChance = 20;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 1000, max = 30000)
    public int minLightningFrequency = 12000;

    @ConfigEntry.Category("planets")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int minBiomes = 8;

    @ConfigEntry.Category("planets")
    public int maxBiomes = 12;

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> veryColdBiomes =  List.of(
            Biomes.SNOWY_TAIGA.location().toString(),
            Biomes.FROZEN_OCEAN.location().toString(),
            Biomes.FROZEN_RIVER.location().toString(),
            Biomes.DEEP_COLD_OCEAN.location().toString(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mars_ice_spikes").toString(),
            ResourceLocation.fromNamespaceAndPath("nmc", "iced_desert").toString(),
            ResourceLocation.fromNamespaceAndPath("nmc", "big_taiga").toString(),

            Biomes.ICE_SPIKES.location().toString()
    );


    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> coldBiomes =  List.of(
            Biomes.TAIGA.location().toString(),
            Biomes.SNOWY_TAIGA.location().toString(),
            Biomes.FROZEN_OCEAN.location().toString(),
            Biomes.FROZEN_RIVER.location().toString(),
            Biomes.DEEP_COLD_OCEAN.location().toString(),
            Biomes.OCEAN.location().toString(),
            Biomes.FROZEN_OCEAN.location().toString(),
            Biomes.PLAINS.location().toString(),
            Biomes.SNOWY_PLAINS.location().toString(),
            Biomes.SNOWY_BEACH.location().toString(),

            ResourceLocation.fromNamespaceAndPath("nmc", "big_taiga").toString()
    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> veryHotBiomes = List.of(
            Biomes.NETHER_WASTES.location().toString(),
            Biomes.BASALT_DELTAS.location().toString(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mercury").toString(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "infernal_venus_barrens").toString(),
            ResourceLocation.fromNamespaceAndPath("nmc", "wasteland").toString()


    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> hotBiomes = List.of(
            Biomes.DESERT.location().toString(),
            Biomes.SAVANNA_PLATEAU.location().toString(),
            Biomes.BADLANDS.location().toString(),
            ResourceLocation.fromNamespaceAndPath("nmc", "wasteland").toString(),
            Biomes.MUSHROOM_FIELDS.location().toString(),
            Biomes.ERODED_BADLANDS.location().toString()

    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> temperateBiomes = List.of(
            Biomes.DESERT.location().toString(),
            Biomes.PLAINS.location().toString(),
            Biomes.SUNFLOWER_PLAINS.location().toString(),
            Biomes.FLOWER_FOREST.location().toString(),
            Biomes.FOREST.location().toString(),
            Biomes.BIRCH_FOREST.location().toString(),
            Biomes.TAIGA.location().toString(),
            Biomes.SNOWY_TAIGA.location().toString(),
            Biomes.OCEAN.location().toString(),
            Biomes.DESERT.location().toString(),
            Biomes.DRIPSTONE_CAVES.location().toString(),
            Biomes.LUSH_CAVES.location().toString(),
            Biomes.BEACH.location().toString(),
            Biomes.GROVE.location().toString(),
            Biomes.JUNGLE.location().toString(),
            Biomes.SPARSE_JUNGLE.location().toString(),
            Biomes.SWAMP.location().toString(),
            Biomes.SAVANNA_PLATEAU.location().toString(),
            Biomes.BADLANDS.location().toString(),
            ResourceLocation.fromNamespaceAndPath("nmc", "big_taiga").toString()

    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    @Comment("This is a list of colors that are possible for the planet. 0 is the default color. This list is weighted. The higher the weight, the more likely the color will be chosen.")
    public int[][] possibleBiomesColors = new int[][]{
            {0, 20},
            {0x41a4c1, 2},
            {0xff80d9, 2},
            {0xe5ae60, 2},
            {0xf36363, 2}
    };

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    @Comment("This is a list of default blocks that are possible for the planet. This list is weighted. The higher the weight, the more likely the block will be chosen. Order : Temperate, Hot, Very Hot, Cold, Very Cold.")
    public DefaultPlanetsBlock possibleDefaultPlanetBlock = new DefaultPlanetsBlock();

    public static class DefaultPlanetsBlock {
        public HashMap<String, Integer> temperarePlanetBlocks = new HashMap<String, Integer>() {{
            put(Blocks.STONE.arch$registryName().toString(), 20);
        }};
        public HashMap<String, Integer> hotPlanetBlocks = new HashMap<String, Integer>() {{
            put(Blocks.STONE.arch$registryName().toString(), 10);
            put(Blocks.BLACKSTONE.arch$registryName().toString(), 10);
        }};
        public HashMap<String, Integer> veryHotPlanetBlocks = new HashMap<String, Integer>() {{
            put(Blocks.BLACKSTONE.arch$registryName().toString(), 10);
            put(Blocks.STONE.arch$registryName().toString(), 5);
            put(Blocks.NETHERRACK.arch$registryName().toString(), 5);
        }};

        public HashMap<String, Integer> coldPlanetBlocks = new HashMap<String, Integer>() {{
            put(Blocks.ICE.arch$registryName().toString(), 20);
            put(Blocks.STONE.arch$registryName().toString(), 5);

        }};

        public HashMap<String, Integer> veryColdPlanetBlocks = new HashMap<String, Integer>() {{
            put(Blocks.ICE.arch$registryName().toString(), 20);
            put(Blocks.PACKED_ICE.arch$registryName().toString(), 5);
            put(Blocks.BLUE_ICE.arch$registryName().toString(), 5);
        }};

    }


    public static BlockState getRandomDefaultBlockLevel(HashMap<String, Integer> entries, RegistryAccess access) {
        SimpleWeightedRandomList.Builder<BlockState> builder = SimpleWeightedRandomList.builder();

        entries.forEach((str, weight) -> {;
            Block block = access.registry(Registries.BLOCK).get().get(ResourceLocation.parse(str));
            if(block != null) {
                builder.add(block.defaultBlockState(), weight);

            }
        });

        return builder.build().getRandomValue(RandomSource.create()).get();
    }


    public static WeightedListInt getPossibleBiomeColors() {
        int[][] colors = NoManCraft.getConfig().possibleBiomesColors;
        SimpleWeightedRandomList.Builder<IntProvider> builder = SimpleWeightedRandomList.<IntProvider>builder();

        for (int[] color : colors) {
            builder.add(ConstantInt.of(color[0]), color[1]);
        }

        return new WeightedListInt(builder.build());
    }

    @Override
    public void validatePostLoad() throws ValidationException {

        if(maxSystems < minSystems) {
            maxSystems = minSystems;
            throw new ValidationException("Max systems cannot be less than min systems");
        }
        if(maxPlanets < minPlanets) {
            maxPlanets = minPlanets;
            throw new ValidationException("Max systems cannot be less than min systems");

        }
    }


}
