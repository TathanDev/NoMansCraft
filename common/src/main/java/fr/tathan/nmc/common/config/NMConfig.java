package fr.tathan.nmc.common.config;

import fr.tathan.nmc.NoManCraft;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

@Config(name = NoManCraft.MODID)
public class NMConfig implements ConfigData {

    @ConfigEntry.Category("planets")
    public int minPlanets = 1;

    @ConfigEntry.Category("planets")
    public int maxPlanets = 6;

    @ConfigEntry.Category("planets")
    public int planetDistanceFromEarth = 900000;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int largeWorldChance = 80;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int amplifiedWorldChance = 85;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int oxygenChance = 20;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int stormyPlanetChance = 20;

    @ConfigEntry.Category("planets")
    @ConfigEntry.BoundedDiscrete(min = 1000, max = 30000)
    public int minLightningFrequency = 12000;


    @ConfigEntry.Category("systems")
    public int minSystems = 3;

    @ConfigEntry.Category("systems")
    public int maxSystems = 6;

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> veryColdBiomes =  List.of(
            Biomes.SNOWY_TAIGA.location().toString(),
            Biomes.FROZEN_OCEAN.location().toString(),
            Biomes.FROZEN_RIVER.location().toString(),
            Biomes.DEEP_COLD_OCEAN.location().toString(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mars_ice_spikes").toString(),
            Biomes.ICE_SPIKES.location().toString()
    );


    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> coldBiomes =  List.of(
            Biomes.TAIGA.location().toString(),
            Biomes.SNOWY_TAIGA.location().toString(),
            Biomes.FROZEN_OCEAN.location().toString(),
            Biomes.FROZEN_RIVER.location().toString(),
            Biomes.DEEP_COLD_OCEAN.location().toString(),
            Biomes.OCEAN.location().toString(),
            Biomes.FROZEN_OCEAN.location().toString()
    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> veryHotBiomes = List.of(
            Biomes.NETHER_WASTES.location().toString(),
            Biomes.BASALT_DELTAS.location().toString(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mercury").toString(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "infernal_venus_barrens").toString()
    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<String> hotBiomes = List.of(
            Biomes.DESERT.location().toString(),
            Biomes.SAVANNA_PLATEAU.location().toString(),
            Biomes.BADLANDS.location().toString()
    );


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
