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


    @ConfigEntry.Category("systems")
    public int minSystems = 3;

    @ConfigEntry.Category("systems")
    public int maxSystems = 6;

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<ResourceLocation> veryColdBiomes =  List.of(
            Biomes.SNOWY_TAIGA.location(),
            Biomes.FROZEN_OCEAN.location(),
            Biomes.FROZEN_RIVER.location(),
            Biomes.DEEP_COLD_OCEAN.location(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mars_ice_spikes"),
            Biomes.ICE_SPIKES.location()
    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<ResourceLocation> coldBiomes =  List.of(
            Biomes.TAIGA.location(),
            Biomes.SNOWY_TAIGA.location(),
            Biomes.FROZEN_OCEAN.location(),
            Biomes.FROZEN_RIVER.location(),
            Biomes.DEEP_COLD_OCEAN.location(),
            Biomes.OCEAN.location(),
            Biomes.FROZEN_OCEAN.location()
    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<ResourceLocation> veryHotBiomes = List.of(
            Biomes.NETHER_WASTES.location(),
            Biomes.BASALT_DELTAS.location(),
            ResourceLocation.fromNamespaceAndPath("stellaris", "mercury"),
            ResourceLocation.fromNamespaceAndPath("stellaris", "infernal_venus_barrens")
    );

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("planets")
    public List<ResourceLocation> hotBiomes = List.of(
            Biomes.DESERT.location(),
            Biomes.SAVANNA_PLATEAU.location(),
            Biomes.BADLANDS.location()
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
