package fr.tathan.nmc.mixin.client;

import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.events.Events;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class CustomGrassColor {

    @Inject(method = "getAverageGrassColor", at = @At("HEAD"), cancellable = true)
    private static void get(BlockAndTintGetter level, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player == null || minecraft.level == null) return;

        for(PlanetCreator creator : Events.SYSTEMS.planets) {
            if (creator.planet.dimension().equals(minecraft.level.dimension().location())) {
                if (creator.grassColor != 0) {
                    cir.setReturnValue(creator.grassColor);
                }
            }
        }
    }
}
