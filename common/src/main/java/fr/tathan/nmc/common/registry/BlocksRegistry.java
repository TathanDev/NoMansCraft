package fr.tathan.nmc.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlocksRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Stellaris.MODID, Registries.BLOCK);

    public static final RegistrySupplier<Block> NO_LEAVE_OAK = BLOCKS.register("no_leaves_oak",
            () -> new SaplingBlock(TreeGrowersRegistry.NO_LEAVES_OAK,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

}
