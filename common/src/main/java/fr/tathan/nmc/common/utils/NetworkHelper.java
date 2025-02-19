package fr.tathan.nmc.common.utils;

import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.client.screens.info.MoonInfo;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.PlanetTextures;
import fr.tathan.sky_aesthetics.client.skies.record.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {

    public static class ToNetwork {
        public static void celestialBody(RegistryFriendlyByteBuf byteBuf, CelestialBody celestialBody) {
            byteBuf.writeResourceLocation(celestialBody.texture);
            byteBuf.writeUtf(celestialBody.name);
            byteBuf.writeFloat(celestialBody.x);
            byteBuf.writeFloat(celestialBody.y);
            byteBuf.writeFloat(celestialBody.width);
            byteBuf.writeFloat(celestialBody.height);
            byteBuf.writeInt(celestialBody.orbitColor);
            byteBuf.writeResourceLocation(celestialBody.dimension);
            byteBuf.writeUtf(celestialBody.translatable);
            byteBuf.writeUtf(celestialBody.id);
            byteBuf.writeBoolean(celestialBody.clickable);
        }

        public static void planetInfo(RegistryFriendlyByteBuf byteBuf, PlanetInfo info) {
            byteBuf.writeResourceLocation(info.texture);
            byteBuf.writeUtf(info.name);
            byteBuf.writeDouble(info.orbitRadius);
            byteBuf.writeLong(info.orbitDuration);
            byteBuf.writeFloat(info.width);
            byteBuf.writeFloat(info.height);
            NetworkHelper.ToNetwork.celestialBody(byteBuf, info.orbitCenter);
            byteBuf.writeResourceLocation(info.dimension);
            byteBuf.writeUtf(info.translatable);
            byteBuf.writeUtf(info.id);
        }

        public static void moonInfo(RegistryFriendlyByteBuf byteBuf, MoonInfo info) {
            byteBuf.writeResourceLocation(info.texture);
            byteBuf.writeUtf(info.name);
            byteBuf.writeDouble(info.orbitRadius);
            byteBuf.writeLong(info.orbitalPeriod);
            byteBuf.writeFloat(info.width);
            byteBuf.writeFloat(info.height);
            NetworkHelper.ToNetwork.planetInfo(byteBuf, info.orbitCenter);
            byteBuf.writeResourceLocation(info.dimension);
            byteBuf.writeUtf(info.translatable);
            byteBuf.writeUtf(info.id);
        }

        public static void planet(RegistryFriendlyByteBuf byteBuf, Planet planet) {
            byteBuf.writeUtf(planet.system());
            byteBuf.writeUtf(planet.translatable());
            byteBuf.writeUtf(planet.name());
            byteBuf.writeResourceLocation(planet.dimension());
            byteBuf.writeBoolean(planet.oxygen());
            byteBuf.writeFloat(planet.temperature());
            byteBuf.writeInt(planet.distanceFromEarth());
            byteBuf.writeFloat(planet.gravity());
            planet.textures().toNetwork(byteBuf);
        }

        public static void skyProperties(RegistryFriendlyByteBuf byteBuf, SkyProperties properties) {
            byteBuf.writeResourceKey(properties.world());
            byteBuf.writeOptional(properties.id(), FriendlyByteBuf::writeResourceLocation);
            cloudSetttings(byteBuf, properties.cloudSettings());
            byteBuf.writeOptional(properties.fogSettings(), (b, f) -> fogSettings(byteBuf, f));
            byteBuf.writeBoolean(properties.rain());
            customVanillaObject(byteBuf, properties.customVanillaObject());
            star(byteBuf, properties.stars());
            byteBuf.writeOptional(properties.sunriseColor(), (b, c) -> byteBuf.writeVec3(c));
            byteBuf.writeOptional(properties.sunriseModifier(), (b, s) -> byteBuf.writeFloat(s));
            byteBuf.writeUtf(properties.skyType());
            skyColor(byteBuf, properties.skyColor());
            byteBuf.writeInt(properties.skyObjects().size());
            properties.skyObjects().forEach((skyObject) -> {
                skyObject(byteBuf, skyObject);
            });
            byteBuf.writeOptional(properties.constellations(), (b, c) -> listString(byteBuf, c));
            byteBuf.writeOptional(properties.renderCondition(), (b, c) -> renderCondition(byteBuf, c));
        }

        public static void cloudSetttings(RegistryFriendlyByteBuf byteBuf, CloudSettings settings) {
            byteBuf.writeBoolean(settings.showCloud());
            byteBuf.writeInt(settings.cloudHeight());
            byteBuf.writeOptional(settings.cloudColor(), (buf, color) -> customCloudColor(byteBuf, color));
        }

        public static void customCloudColor(RegistryFriendlyByteBuf byteBuf, CloudSettings.CustomCloudColor color) {
            byteBuf.writeVec3(color.baseColor());
            byteBuf.writeVec3(color.stormColor());
            byteBuf.writeVec3(color.rainColor());
            byteBuf.writeBoolean(color.alwaysBaseColor());
        }

        public static void fogSettings(RegistryFriendlyByteBuf byteBuf, FogSettings settings) {
            byteBuf.writeBoolean(settings.fog());
            byteBuf.writeOptional(settings.customFogColor(), (b, c) -> byteBuf.writeVector3f(c));
            byteBuf.writeOptional(settings.fogDensity(), (b, c) -> writeVector2f(byteBuf, c));
        }

        public static void writeVector2f(ByteBuf buffer, Vector2f vector2f) {
            buffer.writeFloat(vector2f.x());
            buffer.writeFloat(vector2f.y());
        }

        public static void writeVector3f(ByteBuf buffer, Vector3f vector2f) {
            buffer.writeFloat(vector2f.x());
            buffer.writeFloat(vector2f.y());
            buffer.writeFloat(vector2f.z());
        }
        public static void vec2(ByteBuf buffer, Vec2 vector2f) {
            buffer.writeFloat(vector2f.x);
            buffer.writeFloat(vector2f.y);
        }
        public static void vector4f(ByteBuf buffer, Vector4f vector2f) {
            buffer.writeFloat(vector2f.x);
            buffer.writeFloat(vector2f.y);
            buffer.writeFloat(vector2f.z);
            buffer.writeFloat(vector2f.y);

        }


        public static void customVanillaObject(RegistryFriendlyByteBuf byteBuf, CustomVanillaObject object) {
            byteBuf.writeBoolean(object.sun());
            byteBuf.writeResourceLocation(object.sunTexture());
            byteBuf.writeFloat(object.sunHeight());
            byteBuf.writeFloat(object.sunSize());
            byteBuf.writeBoolean(object.moon());
            byteBuf.writeBoolean(object.moonPhase());
            byteBuf.writeResourceLocation(object.moonTexture());
            byteBuf.writeFloat(object.moonHeight());
            byteBuf.writeFloat(object.moonSize());
        }

        public static void star(RegistryFriendlyByteBuf byteBuf, Star star) {
            byteBuf.writeBoolean(star.vanilla());
            byteBuf.writeBoolean(star.movingStars());
            byteBuf.writeInt(star.count());
            byteBuf.writeBoolean(star.allDaysVisible());
            byteBuf.writeFloat(star.scale());
            byteBuf.writeVec3(star.color());
            byteBuf.writeOptional(star.shootingStars(), (b, c) -> {
                byteBuf.writeInt(c.percentage());
                vec2(byteBuf, c.randomLifetime());
                byteBuf.writeFloat(c.scale());
                byteBuf.writeFloat(c.speed());
                byteBuf.writeVec3(c.color());
                byteBuf.writeOptional(c.rotation(), ByteBuf::writeInt);
            });
        }

        public static void skyColor(RegistryFriendlyByteBuf byteBuf, SkyColor color) {
            byteBuf.writeBoolean(color.customColor());
            vector4f(byteBuf, color.color());
        }

        public static void skyObject(RegistryFriendlyByteBuf buffer, SkyObject obj) {
            buffer.writeResourceLocation(obj.texture());
            buffer.writeBoolean(obj.blend());
            buffer.writeFloat(obj.size());
            buffer.writeVec3(obj.rotation());
            buffer.writeOptional(obj.objectRotation(), (b, r) -> writeVector3f(buffer, r));
            buffer.writeInt(obj.height());
            buffer.writeUtf(obj.rotationType());
        }

        public static void listString(RegistryFriendlyByteBuf buffer, List<String> list) {
            buffer.writeInt(list.size());
            list.forEach(buffer::writeUtf);
        }

        public static void renderCondition(RegistryFriendlyByteBuf buffer, SkyProperties.RenderCondition condition) {
            buffer.writeBoolean(condition.condition());
            buffer.writeOptional(condition.biomes(), (b, c) -> buffer.writeResourceLocation(c.location()));
            buffer.writeOptional(condition.biome(), (b, c) -> buffer.writeResourceKey(c));
        }
    }

    public static class FromNetwork {
        public static CelestialBody celestialBody(RegistryFriendlyByteBuf byteBuf) {
            return new CelestialBody(
                byteBuf.readResourceLocation(),
                byteBuf.readUtf(),
                byteBuf.readFloat(),
                byteBuf.readFloat(),
                byteBuf.readFloat(),
                byteBuf.readFloat(),
                byteBuf.readInt(),
                byteBuf.readResourceLocation(),
                byteBuf.readUtf(),
                byteBuf.readUtf(),
                byteBuf.readBoolean()
            );
        }

        public static PlanetInfo planetInfo(RegistryFriendlyByteBuf byteBuf) {
            return new PlanetInfo(
                    byteBuf.readResourceLocation(),
                    byteBuf.readUtf(),
                    byteBuf.readDouble(),
                    byteBuf.readLong(),
                    byteBuf.readFloat(),
                    byteBuf.readFloat(),
                    NetworkHelper.FromNetwork.celestialBody(byteBuf),
                    byteBuf.readResourceLocation(),
                    byteBuf.readUtf(),
                    byteBuf.readUtf()
            );
        }

        public static MoonInfo moonInfo(RegistryFriendlyByteBuf byteBuf) {
            return new MoonInfo(
                    byteBuf.readResourceLocation(),
                    byteBuf.readUtf(),
                    byteBuf.readDouble(),
                    byteBuf.readLong(),
                    byteBuf.readInt(),
                    byteBuf.readInt(),
                    NetworkHelper.FromNetwork.planetInfo(byteBuf),
                    byteBuf.readResourceLocation(),
                    byteBuf.readUtf(),
                    byteBuf.readUtf()
            );
        }

        public static Planet planet(RegistryFriendlyByteBuf byteBuf) {
           return new Planet(byteBuf.readUtf(), byteBuf.readUtf(), byteBuf.readUtf(), byteBuf.readResourceLocation(), byteBuf.readBoolean(), byteBuf.readFloat(), byteBuf.readInt(), byteBuf.readFloat(), PlanetTextures.fromNetwork(byteBuf));
        }

        public static CloudSettings cloudSettings(RegistryFriendlyByteBuf byteBuf) {
            return new CloudSettings(
                    byteBuf.readBoolean(),
                    byteBuf.readInt(),
                    byteBuf.readOptional((s -> customCloudColor(byteBuf)))
            );
        }

        public static CloudSettings.CustomCloudColor customCloudColor(RegistryFriendlyByteBuf byteBuf) {
            return new CloudSettings.CustomCloudColor(
                    byteBuf.readVec3(),
                    byteBuf.readVec3(),
                    byteBuf.readVec3(),
                    byteBuf.readBoolean()
            );
        }

        public static FogSettings fogSettings(RegistryFriendlyByteBuf byteBuf) {

            return new FogSettings(byteBuf.readBoolean(),
                    byteBuf.readOptional((b -> byteBuf.readVector3f())),
                    byteBuf.readOptional((b -> vector2f(byteBuf)))
                    );
        }

        public static Vector2f vector2f(ByteBuf buffer) {
            return new Vector2f(buffer.readFloat(), buffer.readFloat());
        }

        public static CustomVanillaObject customVanillaObject(RegistryFriendlyByteBuf byteBuf) {
            return new CustomVanillaObject(
                    byteBuf.readBoolean(),
                    byteBuf.readResourceLocation(),
                    byteBuf.readFloat(),
                    byteBuf.readFloat(),
                    byteBuf.readBoolean(),
                    byteBuf.readBoolean(),
                    byteBuf.readResourceLocation(),
                    byteBuf.readFloat(),
                    byteBuf.readFloat()
            );
        }

        public static Star star(RegistryFriendlyByteBuf byteBuf) {
            return new Star(
                    byteBuf.readBoolean(),
                    byteBuf.readBoolean(),
                    byteBuf.readInt(),
                    byteBuf.readBoolean(),
                    byteBuf.readFloat(),
                    byteBuf.readVec3(),
                    byteBuf.readOptional((b -> shootingStars(byteBuf)))
            );
        }

        public static Star.ShootingStars shootingStars(RegistryFriendlyByteBuf byteBuf) {
            return new Star.ShootingStars(
                    byteBuf.readInt(),
                    new Vec2(byteBuf.readFloat(), byteBuf.readFloat()),
                    byteBuf.readFloat(),
                    byteBuf.readFloat(),
                    new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble()),
                    byteBuf.readOptional(ByteBuf::readInt)
            );
        }

        public static SkyColor skyColor(RegistryFriendlyByteBuf byteBuf) {
            return new SkyColor(byteBuf.readBoolean(), new Vector4f(byteBuf.readFloat(), byteBuf.readFloat(), byteBuf.readFloat(), byteBuf.readFloat()));
        }

        public static SkyObject skyObject(RegistryFriendlyByteBuf buffer) {
            return new SkyObject(
                    buffer.readResourceLocation(),
                    buffer.readBoolean(),
                    buffer.readFloat(),
                    buffer.readVec3(),
                    buffer.readOptional((b -> buffer.readVector3f())),
                    buffer.readInt(),
                    buffer.readUtf()
            );
        }

        public static SkyProperties.RenderCondition renderCondition(RegistryFriendlyByteBuf buffer) {
            return new SkyProperties.RenderCondition(buffer.readBoolean(), buffer.readOptional((b -> TagKey.create(Registries.BIOME, buffer.readResourceLocation()))), buffer.readOptional((b -> buffer.readResourceKey(Registries.BIOME))));
        }

        public static SkyProperties skyProperties(RegistryFriendlyByteBuf buffer) {
            return new SkyProperties(
                    buffer.readResourceKey(Registries.DIMENSION),
                    buffer.readOptional(FriendlyByteBuf::readResourceLocation),
                    cloudSettings(buffer),
                    buffer.readOptional((b -> fogSettings(buffer))),
                    buffer.readBoolean(),
                    customVanillaObject(buffer),
                    star(buffer),
                    //Sunrise Color
                    buffer.readOptional((b -> buffer.readVec3())),
                    //Sunrise Modifier
                    buffer.readOptional((b -> buffer.readFloat())),
                    //Sky Type
                    buffer.readUtf(),
                    //Sky Color
                    skyColor(buffer),
                    //Sky Objects
                    skyObjectList(buffer),
                    buffer.readOptional((b -> stringList(buffer))),
                    buffer.readOptional(c -> renderCondition(buffer))
            );
        }

        public static List<String> stringList(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(buffer.readUtf());
            }
            return list;
        }

        public static List<SkyObject> skyObjectList(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            List<SkyObject> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(skyObject(buffer));
            }
            return list;
        }
    }
}
