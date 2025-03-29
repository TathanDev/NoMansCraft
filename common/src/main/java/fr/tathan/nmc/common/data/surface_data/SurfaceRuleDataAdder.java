package fr.tathan.nmc.common.data.surface_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.Optional;

public record SurfaceRuleDataAdder(
        Optional<ResourceLocation> id,
        SurfaceRules.RuleSource source) {

    public static final Codec<SurfaceRuleDataAdder> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("id").forGetter(SurfaceRuleDataAdder::id),
            SurfaceRules.RuleSource.CODEC.fieldOf("surface_rule").forGetter(SurfaceRuleDataAdder::source)
    ).apply(instance, SurfaceRuleDataAdder::new));
}
