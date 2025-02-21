package fr.tathan.nmc.common.data.surface_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.SurfaceRules;

public record SurfaceRuleDataAdder(SurfaceRules.RuleSource source) {

    public static final Codec<SurfaceRuleDataAdder> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            SurfaceRules.RuleSource.CODEC.fieldOf("surface_rule").forGetter(SurfaceRuleDataAdder::source)
    ).apply(instance, SurfaceRuleDataAdder::new));
}
