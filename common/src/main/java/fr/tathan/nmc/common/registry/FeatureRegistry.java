package fr.tathan.nmc.common.registry;


import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.world.features.CustomSpireColumn;
import fr.tathan.nmc.common.world.features.CustomSpireColumnConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public class FeatureRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(NoManCraft.MODID, Registries.FEATURE);

    public static final RegistrySupplier<Feature<CustomSpireColumnConfig>> CUSTOM_SPIRE_COLUMN = FEATURES.register("custom_spire_column", () -> new CustomSpireColumn(CustomSpireColumnConfig.CODEC));

}
