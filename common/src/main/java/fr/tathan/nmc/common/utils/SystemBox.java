package fr.tathan.nmc.common.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class SystemBox {

    int x, y, size;

    public SystemBox(int centerX, int centerY, int size) {
        this.size = size;
        this.x = centerX - size / 2; // Calculate top-left corner
        this.y = centerY - size / 2;
    }

    public boolean overlaps(SystemBox other) {
        return this.x < other.x + other.size &&
                this.x + this.size > other.x &&
                this.y < other.y + other.size &&
                this.y + this.size > other.y;
    }

    public static final Codec<SystemBox> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("x").forGetter(s -> s.x),
            Codec.INT.fieldOf("y").forGetter(s -> s.y),
            Codec.INT.fieldOf("size").forGetter(s -> s.size)
    ).apply(instance, SystemBox::new));

    public static void toNetwork(SystemBox system, final RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(system.x);
        buffer.writeInt(system.y);
        buffer.writeInt(system.size);
    }

    public static SystemBox fromNetwork(final RegistryFriendlyByteBuf buffer) {
        return new SystemBox(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }


}
