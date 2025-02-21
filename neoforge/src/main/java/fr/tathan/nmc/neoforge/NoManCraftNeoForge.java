package fr.tathan.nmc.neoforge;

import com.st0x0ef.stellaris.neoforge.StellarisNeoForge;
import fr.tathan.nmc.NoManCraft;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(NoManCraft.MODID)
public final class NoManCraftNeoForge {
    public NoManCraftNeoForge() {
        NoManCraft.init();
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onAddReloadListenerEvent);
    }

    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        NoManCraft.onAddReloadListenerEvent((id, listener) -> event.addListener(listener));
    }

}
