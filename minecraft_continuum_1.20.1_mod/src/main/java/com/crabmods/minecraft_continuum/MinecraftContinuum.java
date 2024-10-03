package com.crabmods.minecraft_continuum;

import com.crabmods.minecraft_continuum.config.ConfigHandler;
import com.crabmods.minecraft_continuum.registers.BlocksRegister;
import com.crabmods.minecraft_continuum.registers.ItemGroup;
import com.crabmods.minecraft_continuum.registers.ItemRegister;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Path;

@Mod(MinecraftContinuum.MODID)
public class MinecraftContinuum {
    public static final String MODID = "minecraft_continuum";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MinecraftContinuum() {
        @SuppressWarnings("removal")
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Load the configuration before item registration
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml");
        ConfigHandler.loadConfig(ConfigHandler.CONFIG, configPath);

        // Register blocks, creative tabs, and items
        BlocksRegister.register(modEventBus);
        ItemGroup.register(modEventBus);
        ItemRegister.register(modEventBus);

        // Register event listeners on the Forge event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup complete.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting...");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Client setup completed.");
        }
    }
}
