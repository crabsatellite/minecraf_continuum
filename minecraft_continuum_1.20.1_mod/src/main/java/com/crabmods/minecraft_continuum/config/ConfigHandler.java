package com.crabmods.minecraft_continuum.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.crabmods.minecraft_continuum.MinecraftContinuum;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.List;

@Mod.EventBusSubscriber(modid = MinecraftContinuum.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    // List of world paths
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> worldPaths;

    static {
        BUILDER.push("General");
        worldPaths = BUILDER.comment("List of paths to the world files")
                .defineList("worldPaths", List.of("config/minecraft_continuum/world1", "config/minecraft_continuum/world2"),
                        obj -> obj instanceof String);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, Path path) {
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(path).sync().autosave().writingMode(com.electronwill.nightconfig.core.io.WritingMode.REPLACE).build();
        fileConfig.load();
        config.setConfig(fileConfig);
    }
}
