package fr.tathan.nmc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.ReloadListenerRegistry;
import fr.tathan.nmc.common.config.NMConfig;
import fr.tathan.nmc.common.data.surface_data.SurfaceRuleData;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.networks.NetworkRegistry;
import fr.tathan.nmc.common.registry.BlocksRegistry;
import fr.tathan.nmc.common.registry.FeatureRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.BiConsumer;

public final class NoManCraft {
    public static final String MODID = "nmc";
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static NMConfig CONFIG = null;


    public static void init() {
        AutoConfig.register(NMConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NMConfig.class).getConfig();
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new SurfaceRuleData());

        BlocksRegistry.BLOCKS.register();
        FeatureRegistry.FEATURES.register();
        NetworkRegistry.init();
        Events.registerEvents();
    }

    public static NMConfig getConfig() {
        if (CONFIG == null) {
            return AutoConfig.getConfigHolder(NMConfig.class).getConfig();
        }
        return CONFIG;
    }

    public static void onAddReloadListenerEvent(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(ResourceLocation.fromNamespaceAndPath(MODID, "surface_rules"), new SurfaceRuleData());
    }
}
