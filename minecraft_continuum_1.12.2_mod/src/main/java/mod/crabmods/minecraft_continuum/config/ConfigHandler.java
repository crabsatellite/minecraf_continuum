package mod.crabmods.minecraft_continuum.config;

import mod.crabmods.minecraft_continuum.MinecraftContinuum;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
  public static Configuration config;

  // List of world paths
  public static List<String> worldPaths;

  // Default world paths
  private static final List<String> DEFAULT_WORLD_PATHS = createDefaultPaths();

  public static void init(FMLPreInitializationEvent event) {
    File configFile =
        new File(event.getModConfigurationDirectory(), MinecraftContinuum.MODID + ".cfg");
    config = new Configuration(configFile);

    // Load configuration
    loadConfig();
  }

  private static void loadConfig() {
    // Read the config file
    config.load();

    // Read world paths from config, or use default paths if not set
    String[] paths =
        config.getStringList(
            "worldPaths",
            "General",
            DEFAULT_WORLD_PATHS.toArray(new String[0]),
            "List of paths to the world files\nExample: worldPaths = [\"C:\\\\Users\\\\User\\\\AppData\\\\Roaming\", \"E:\\\\Minecraft\\\\World_1.12.2\"]");

    worldPaths = new ArrayList<>(Arrays.asList(paths));

    // Ensure that there are at least 10 paths
    ensureMinWorldPaths();

    // Save changes to the config
    if (config.hasChanged()) {
      config.save();
    }
  }

  private static void ensureMinWorldPaths() {
    // If there are fewer than 10 paths, add default paths until there are 10
    if (worldPaths.size() < 10) {
      for (int i = worldPaths.size(); i < 10; i++) {
        worldPaths.add(DEFAULT_WORLD_PATHS.get(i));
      }
    }
  }

  private static List<String> createDefaultPaths() {
    List<String> defaultPaths = new ArrayList<>();
    // Use a placeholder for "Empty world"
    for (int i = 0; i < 10; i++) {
      defaultPaths.add("Empty World");
    }
    return defaultPaths;
  }
}
