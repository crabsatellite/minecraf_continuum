package mod.crabmods.minecraft_continuum.registers;

import java.util.ArrayList;
import java.util.List;
import mod.crabmods.minecraft_continuum.config.ConfigHandler;
import mod.crabmods.minecraft_continuum.items.WorldCrystal;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemRegister {
  // List to store all the registered item
  public static final List<Item> WORLD_CRYSTALS = new ArrayList<>();

  public static void registerItems() {
    // Get the list of script paths from config
    List<String> scriptPaths = ConfigHandler.worldPaths;

    // Set the number of item to register
    int itemsToRegister = 10;

    // Loop to register exactly 10 item
    for (int i = 0; i < itemsToRegister; i++) {
      // If scriptPaths has fewer paths than 10, repeat paths or use a default value
      String path =
          (scriptPaths != null && !scriptPaths.isEmpty() && i < scriptPaths.size())
              ? scriptPaths.get(i)
              : "Empty World"; // Use a default path if the list is too short

      // Create a new item and add it to the list
      WorldCrystal worldCrystal = new WorldCrystal(path + "\\PCL\\LatestLaunch.bat");
      worldCrystal.setRegistryName("minecraft_continuum", "world_crystal_" + i);
      worldCrystal.setUnlocalizedName("minecraft_continuum.world_crystal_" + i);
      worldCrystal.setCreativeTab(ItemGroup.MINECRAFT_CONTINUUM_TAB);
      WORLD_CRYSTALS.add(worldCrystal);

      // Register the item with the Forge registry
      ForgeRegistries.ITEMS.register(worldCrystal);
    }
  }
}
