package fr.tathan.nmc.common.world;

import dev.architectury.registry.registries.DeferredRegister;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.registry.FeatureRegistry;
import fr.tathan.nmc.common.world.features.CustomSpireColumnConfig;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class NMCConfiguredFeatures {


    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(NoManCraft.MODID, Registries.CONFIGURED_FEATURE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> NO_LEAVES_OAK = registerKey("no_leaves_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NO_LEAVES_OAK_SPAWN = registerKey("no_leaves_oak_spawn");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_BLOB = registerKey("ice_blob");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);


        register(context, NO_LEAVES_OAK, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_WOOD),
                new FancyTrunkPlacer(4, 7, 3),
                BlockStateProvider.simple(Blocks.AIR),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4),
                new TwoLayersFeatureSize(1, 0, 2)).build());

        register(context, NO_LEAVES_OAK_SPAWN, Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(
                        placedFeatures.getOrThrow(NMCPlacedFeatures.NO_LEAVES_OAK_CHECKED_KEY),
                        0.5F)), placedFeatures.getOrThrow(NMCPlacedFeatures.NO_LEAVES_OAK_CHECKED_KEY)));

        register(context, ICE_BLOB, FeatureRegistry.CUSTOM_SPIRE_COLUMN.get(), new CustomSpireColumnConfig(
                UniformInt.of(2, 4),
                UniformInt.of(3, 6),
                Blocks.BLUE_ICE.defaultBlockState()
        ));


    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(NoManCraft.MODID, name));
    }


    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
