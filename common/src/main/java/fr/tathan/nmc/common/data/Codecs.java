package fr.tathan.nmc.common.data;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import fr.tathan.sky_aesthetics.client.skies.PlanetSky;
import fr.tathan.sky_aesthetics.client.skies.record.SkyProperties;
import net.minecraft.resources.ResourceLocation;

public class Codecs {

    public static final Codec<PlanetSky> SKY = RecordCodecBuilder.create((instance) -> instance.group(
            SkyProperties.CODEC.fieldOf("skyProperties").forGetter(PlanetSky::getProperties)
    ).apply(instance, instance.stable(PlanetSky::new)));

    public static final Codec<CelestialBody> CELESTIAL_BODY = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter((b) -> b.texture),
            Codec.STRING.fieldOf("name").forGetter((b) -> b.name),
            Codec.FLOAT.fieldOf("x").forGetter((b) -> b.x),
            Codec.FLOAT.fieldOf("y").forGetter((b) -> b.y),
            Codec.FLOAT.fieldOf("width").forGetter((b) -> b.width),
            Codec.FLOAT.fieldOf("height").forGetter((b) -> b.height),
            Codec.INT.fieldOf("orbitColor").forGetter((b) -> b.orbitColor),
            ResourceLocation.CODEC.fieldOf("dimension").forGetter((b) -> b.dimension),
            Codec.STRING.fieldOf("translatable").forGetter((b) -> b.translatable),
            Codec.STRING.fieldOf("id").forGetter((b) -> b.id),
            Codec.BOOL.optionalFieldOf("clickable", true).forGetter((b) -> b.clickable)
    ).apply(instance, CelestialBody::new));

}
