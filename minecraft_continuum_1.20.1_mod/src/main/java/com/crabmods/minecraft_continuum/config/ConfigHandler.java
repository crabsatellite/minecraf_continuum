package com.crabmods.minecraft_continuum.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.crabmods.minecraft_continuum.MinecraftContinuum;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MinecraftContinuum.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    // List of world paths
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> worldPaths;

    // Default world paths
    private static final List<String> DEFAULT_WORLD_PATHS = createDefaultPaths();

    static {
        BUILDER.push("General");
        worldPaths = BUILDER.comment("List of paths to the world files\n" + "Example: worldPaths = [\"C:\\\\Users\\\\User\\\\AppData\\\\Roaming\", \"E:\\\\Minecraft\\\\World_1.12.2\"]")
                .defineList("worldPaths", DEFAULT_WORLD_PATHS, obj -> obj instanceof String);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, Path path) {
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(path).sync().autosave().writingMode(com.electronwill.nightconfig.core.io.WritingMode.REPLACE).build();
        fileConfig.load();
        config.setConfig(fileConfig);

        // Check if there are fewer than 10 paths and add missing ones
        ensureMinWorldPaths(config);
    }

    private static void ensureMinWorldPaths(ForgeConfigSpec config) {
        // Get the current list of paths from the config
        List<? extends String> currentPaths = worldPaths.get();

        // If there are fewer than 10 paths, add default paths until there are 10
        if (currentPaths.size() < 10) {
            List<String> updatedPaths = new ArrayList<>(currentPaths);

            // Start adding missing default paths without modifying existing ones
            for (int i = currentPaths.size(); i < 10; i++) {
                updatedPaths.add(DEFAULT_WORLD_PATHS.get(i));
            }

            // Update the config value
            worldPaths.set(updatedPaths);
        }
    }

    private static List<String> createDefaultPaths() {
        List<String> defaultPaths = new ArrayList<>();
        // Use a placeholder for "Empty world"
        for (int i = 1; i <= 10; i++) {
            defaultPaths.add("Empty World");
        }
        return defaultPaths;
    }
}
