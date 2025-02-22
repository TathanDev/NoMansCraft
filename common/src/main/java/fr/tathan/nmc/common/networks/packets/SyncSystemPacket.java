package fr.tathan.nmc.common.networks.packets;

import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import dev.architectury.networking.NetworkManager;
import fr.tathan.SkyAesthetics;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.nmc.common.creators.SystemsContainer;
import fr.tathan.nmc.common.events.Events;
import fr.tathan.nmc.common.networks.NetworkRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SyncSystemPacket implements CustomPacketPayload {

    private final SystemsContainer container;

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSystemPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SyncSystemPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncSystemPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncSystemPacket packet) {
            SystemsContainer.toNetwork(packet.container, buf);
        }
    };

    public SyncSystemPacket(RegistryFriendlyByteBuf buffer) {
        this.container = SystemsContainer.fromNetwork(buffer);
    }

    public SyncSystemPacket(SystemsContainer planets) {
        this.container = planets;
    }


    public static void handle(SyncSystemPacket packet, NetworkManager.PacketContext context) {
        Events.SYSTEMS = packet.container;
        ArrayList<Planet> planets = new ArrayList<>();
        for(SystemCreator system : packet.container.systems) {
            PlanetSelectionScreen.STARS.add(system.celestialBody);
        }
        for(PlanetCreator planet : packet.container.planets) {
            PlanetSelectionScreen.PLANETS.add(planet.planetInfo);
            planets.add(planet.planet);
            //TODO add skies
            planet.moons.forEach((moon) -> {
                PlanetSelectionScreen.MOONS.add(moon.moonInfo);
                planets.add(planet.planet);
            });
        }
        StellarisData.addPlanets(planets);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.SYNC_SYSTEMS;
    }
}
