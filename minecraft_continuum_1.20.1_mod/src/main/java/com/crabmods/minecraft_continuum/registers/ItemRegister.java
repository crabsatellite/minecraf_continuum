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
        registerFixedNumberOfItems();
    }

    // Register items based on a fixed number of 10
    private static void registerFixedNumberOfItems() {
        // Get the list of script paths from config
        List<? extends String> scriptPaths = ConfigHandler.worldPaths.get();

        // Set the number of items to register
        int itemsToRegister = 10;

        // Loop to register exactly 10 items
        for (int i = 0; i < itemsToRegister; i++) {
            // If scriptPaths has fewer paths than 10, repeat paths or use a default value
            String path = (scriptPaths != null && !scriptPaths.isEmpty() && i < scriptPaths.size())
                    ? scriptPaths.get(i)
                    : "Empty World"; // Use a default path if the list is too short

            // Register each item with a unique name and associate it with the corresponding path
            TELEPORT_ITEMS.add(ITEMS.register("otherworld_teleport_item_" + i,
                    () -> new OtherworldTeleportItem(new Item.Properties(), path + "\\PCL\\LatestLaunch.bat")));
        }
    }

}
