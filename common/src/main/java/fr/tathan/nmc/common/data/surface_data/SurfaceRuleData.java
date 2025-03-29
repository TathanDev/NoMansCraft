package fr.tathan.nmc.common.data.surface_data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import fr.tathan.nmc.NoManCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SurfaceRuleData extends SimpleJsonResourceReloadListener  {

    public static final List<SurfaceRules.RuleSource> SURFACE_RULE_DATA_ADDERS = new ArrayList<>();


    public SurfaceRuleData() {
        super(NoManCraft.gson, "surface_rules");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {

        SURFACE_RULE_DATA_ADDERS.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "surface_rule");
            SurfaceRuleDataAdder surfaceRuleDataAdder = SurfaceRuleDataAdder.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            surfaceRuleDataAdder.id().ifPresent((s) -> NoManCraft.LOG.error("Surface rule {}", s));
            SURFACE_RULE_DATA_ADDERS.add(surfaceRuleDataAdder.source());
        });

    }
}
