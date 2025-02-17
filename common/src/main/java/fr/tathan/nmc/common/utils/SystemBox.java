package fr.tathan.nmc.common.utils;

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

}
