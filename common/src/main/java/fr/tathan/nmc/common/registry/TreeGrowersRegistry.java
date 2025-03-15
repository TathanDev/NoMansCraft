package fr.tathan.nmc.common.registry;

import fr.tathan.nmc.common.world.NMCConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class TreeGrowersRegistry {

    public static final TreeGrower NO_LEAVES_OAK = new TreeGrower("no_leaves_oak", Optional.empty(), Optional.of(NMCConfiguredFeatures.NO_LEAVES_OAK), Optional.empty());


}
