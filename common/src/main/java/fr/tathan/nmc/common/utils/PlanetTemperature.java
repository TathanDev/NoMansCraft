package fr.tathan.nmc.common.utils;

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


    public static PlanetTemperature randomTemperature() {
        var random = Math.random();
        if(random < 0.2) {
            return VERY_COLD;
        } else if(random < 0.4) {
            return COLD;
        } else if(random < 0.6) {
            return TEMPERATE;
        } else if(random < 0.8) {
            return HOT;
        } else {
            return VERY_HOT;
        }
    }
}
