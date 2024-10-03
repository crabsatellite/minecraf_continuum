package com.crabmods.minecraft_continuum.registers;

import com.crabmods.minecraft_continuum.MinecraftContinuum;
import com.crabmods.minecraft_continuum.config.ConfigHandler;
import com.crabmods.minecraft_continuum.items.OtherworldTeleportItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftContinuum.MODID);

    // List to store dynamically generated item references
    public static final List<RegistryObject<Item>> TELEPORT_ITEMS = new ArrayList<>();

    // Register the deferred register to the event bus
    public static void register(IEventBus eventBus) {
        // First, ensure the deferred register is registered
        ITEMS.register(eventBus);

        // Then, add items based on the config
        registerItemsFromConfig();
    }

    // Register items based on the number of paths in the config
    private static void registerItemsFromConfig() {
        // Get the list of script paths from config
        List<? extends String> scriptPaths = ConfigHandler.worldPaths.get();

        // Check if the config contains any paths
        if (scriptPaths == null || scriptPaths.isEmpty()) {
            // If no paths are present in the config, don't register any items
            System.out.println("No script paths found in config, skipping item registration.");
            return;
        }

        // Loop through each script path to create a corresponding item
        for (int i = 0; i < scriptPaths.size(); i++) {
            String path = scriptPaths.get(i);

            // Register each item with a unique name and associate it with the corresponding path
            TELEPORT_ITEMS.add(ITEMS.register("otherworld_teleport_item_" + i,
                    () -> new OtherworldTeleportItem(new Item.Properties(), path + "\\PCL\\LatestLaunch.bat")));
        }
    }
}
