package fr.tathan.nmc.common.config;

import fr.tathan.nmc.NoManCraft;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = NoManCraft.MODID)
public class NMConfig implements ConfigData {

    @ConfigEntry.Category("planets")
    public int minPlanets = 1;
    @ConfigEntry.Category("planets")
    public int maxPlanets = 6;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 10)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int oxygenChance = 2;

    @ConfigEntry.Category("planets")
    public int planetDistanceFromEarth = 900000;


    @ConfigEntry.Category("systems")
    public int minSystems = 3;
    @ConfigEntry.Category("systems")
    public int maxSystems = 6;

    @Override
    public void validatePostLoad() throws ValidationException {

        if(maxSystems < minSystems) {
            maxSystems = minSystems;
            throw new ValidationException("Max systems cannot be less than min systems");
        }
        if(maxPlanets < minPlanets) {
            maxPlanets = minPlanets;
            throw new ValidationException("Max systems cannot be less than min systems");

        }
    }

}
