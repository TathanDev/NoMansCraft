package fr.tathan.nmc.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.tathan.nmc.NoManCraft;
import net.minecraft.data.worldgen.SurfaceRuleData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SurfaceRuleData.class)
public class AddCustomSurfaceRuleMixin {

    @WrapOperation(
            method = "overworldLike",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;builder()Lcom/google/common/collect/ImmutableList$Builder;")
    )
    private static <RuleSource> ImmutableList.Builder<RuleSource> addCustomRuleSequence(Operation<ImmutableList.Builder<RuleSource>> original) {
        ImmutableList.Builder<RuleSource> builder = original.call();

        fr.tathan.nmc.common.data.surface_data.SurfaceRuleData.SURFACE_RULE_DATA_ADDERS.forEach(rule -> {
            builder.add((RuleSource) rule);
        });
        return builder;
    }
}
