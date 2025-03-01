package fr.tathan.nmc.common.utils;

import java.util.Random;

public enum PlanetTemperature {
    VERY_COLD(-100)
    , COLD(-10)
    , TEMPERATE(10)
    , HOT(50)
    , VERY_HOT(100);


    private final int temperature;

    PlanetTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int temperature() {
        return temperature;
    }

    public static PlanetTemperature fromInt(int temperature) {
        if(temperature < -50) {
            return VERY_COLD;
        } else if(temperature < 0) {
            return COLD;
        } else if(temperature < 20) {
            return TEMPERATE;
        } else if(temperature < 75) {
            return HOT;
        } else {
            return VERY_HOT;
        }
    }

    public static PlanetTemperature randomTemperature() {
        var random = new Random().nextInt(0, 5);
        return switch (random) {
            case 0 -> VERY_COLD;
            case 1 -> COLD;
            case 3 -> HOT;
            case 4 -> VERY_HOT;
            default -> TEMPERATE;
        };
    }
}
