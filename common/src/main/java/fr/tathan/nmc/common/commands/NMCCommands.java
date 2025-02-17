package fr.tathan.nmc.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.galacticraft.dynamicdimensions.api.DynamicDimensionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.OptionalLong;

public class NMCCommands {

    public NMCCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("nmc")
                .then(Commands.literal("create")
                        .requires(c -> c.hasPermission(2))
                        .then(Commands.literal("randomPlanet")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    DynamicDimensionRegistry registry = DynamicDimensionRegistry.from(context.getSource().getServer());
                                    RegistryAccess access = context.getSource().registryAccess();
                                    ChunkGenerator generator = new FlatLevelSource(FlatLevelGeneratorSettings.getDefault(access.lookupOrThrow(Registries.BIOME), access.lookupOrThrow(Registries.STRUCTURE_SET), access.lookupOrThrow(Registries.PLACED_FEATURE)));
                                    DimensionType type = new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0D, true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0F, new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0));
                                    return 0;
                                }))

                )


        );
    }
}
