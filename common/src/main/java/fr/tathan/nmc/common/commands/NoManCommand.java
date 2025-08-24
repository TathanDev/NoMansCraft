package fr.tathan.nmc.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

// Got it ? No Man Command = NMC
public class NoManCommand {

    public NoManCommand(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("nmc")
                .then(Commands.literal("createPlanet")
                    .requires(c -> c.hasPermission(2))
                    .then(Commands.argument("planet", ResourceLocationArgument.id())
                        .executes((context) -> {

                            ResourceLocation planetDim = ResourceLocationArgument.getId(context, "planet");

                            for (PlanetCreator planetCreator : Events.SYSTEMS.planets) {
                                if (Events.matchesDimension(planetCreator, planetDim)) {
                                    Utils.generateWorld(context.getSource().getServer(), context.getSource().getPlayer(), planetCreator);

                                    context.getSource().sendSuccess(() -> Component.literal("Planet " + planetDim + " created !").withStyle(ChatFormatting.GREEN), true);
                                    return 1;
                                }
                            }
                            context.getSource().sendFailure(Component.literal("No Planet with name " + planetDim + " founded.").withStyle(ChatFormatting.RED));
                            return 0;
                        })
                    )
                )
        );
    }


}
