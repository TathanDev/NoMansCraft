package fr.tathan.nmc.common.world;

import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.registry.BlocksRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class NMCPlacedFeatures {

    public static final ResourceKey<PlacedFeature> NO_LEAVES_OAK_CHECKED_KEY = createKey("no_leaves_oak_checked");
    public static final ResourceKey<PlacedFeature> IRRADIATED_OAK_PLACED_KEY = createKey("no_leaves_oak_placed");
    public static final ResourceKey<PlacedFeature> ICE_BLOB = createKey("ice_blob");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);


        register(context, NO_LEAVES_OAK_CHECKED_KEY, configuredFeatures.getOrThrow(NMCConfiguredFeatures.NO_LEAVES_OAK),
                List.of(PlacementUtils.filteredByBlockSurvival(BlocksRegistry.NO_LEAVE_OAK.get())));

        register(context, IRRADIATED_OAK_PLACED_KEY, configuredFeatures.getOrThrow(NMCConfiguredFeatures.NO_LEAVES_OAK),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2)));

        register(context, ICE_BLOB, configuredFeatures.getOrThrow(NMCConfiguredFeatures.ICE_BLOB),
                CountPlacement.of(UniformInt.of(1, 3)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }



    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(NoManCraft.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }

}
