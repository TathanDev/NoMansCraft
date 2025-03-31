package fr.tathan.nmc.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CustomSpireColumnConfig(IntProvider reach, IntProvider height,
                                      BlockState block) implements FeatureConfiguration {

    public static final Codec<CustomSpireColumnConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    IntProvider.codec(0, 6).fieldOf("reach").forGetter((columnFeatureConfiguration) -> columnFeatureConfiguration.reach),
                    IntProvider.codec(1, 10).fieldOf("height").forGetter((columnFeatureConfiguration) -> columnFeatureConfiguration.height),
                    BlockState.CODEC.fieldOf("block").forGetter((columnFeatureConfiguration) -> columnFeatureConfiguration.block)
            ).apply(instance, CustomSpireColumnConfig::new));
}
