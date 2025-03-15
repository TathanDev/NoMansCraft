package fr.tathan.nmc.neoforge.datagen;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.world.ModConfiguredFeature;
import com.st0x0ef.stellaris.common.world.ModPlacedFeatures;
import fr.tathan.nmc.NoManCraft;
import fr.tathan.nmc.common.world.NMCConfiguredFeatures;
import fr.tathan.nmc.common.world.NMCPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

@EventBusSubscriber(modid = NoManCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        var generator =  event.getGenerator();


        generator.addProvider(
                event.includeServer(),
                (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
                        output,
                        event.getLookupProvider(),
                        new RegistrySetBuilder()
                                .add(Registries.CONFIGURED_FEATURE, NMCConfiguredFeatures::bootstrap)
                                .add(Registries.PLACED_FEATURE, NMCPlacedFeatures::bootstrap),
                        Set.of(NoManCraft.MODID)
                )
        );


    }
}