package fr.tathan.nmc.common.data;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import fr.tathan.nmc.common.creators.PlanetCreator;
import fr.tathan.nmc.common.creators.SystemCreator;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;
import fr.tathan.sky_aesthetics.client.skies.record.SkyProperties;

public class Codecs {

    public static final Codec<PlanetSky> SKY = RecordCodecBuilder.create((instance) -> instance.group(
            SkyProperties.CODEC.fieldOf("skyProperties").forGetter(PlanetSky::getProperties)
    ).apply(instance, instance.stable(PlanetSky::new)));

}
