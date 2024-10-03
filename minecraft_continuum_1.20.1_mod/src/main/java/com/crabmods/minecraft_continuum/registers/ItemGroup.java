package com.crabmods.minecraft_continuum.registers;

import com.crabmods.minecraft_continuum.MinecraftContinuum;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MinecraftContinuum.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemGroup {
    public static final DeferredRegister<CreativeModeTab> MINECRAFT_CONTINUUM_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinecraftContinuum.MODID);

    public static final RegistryObject<CreativeModeTab> MINECRAFT_CONTINUUM_TAB =
            MINECRAFT_CONTINUUM_TABS.register(
                    "minecraft_continuum_tab",
                    () ->
                            CreativeModeTab.builder()
                                    // Set the tab icon to one of your items (e.g., the first teleport item)
                                    .icon(() -> new ItemStack(ItemRegister.TELEPORT_ITEMS.isEmpty() ? net.minecraft.world.item.Items.BARRIER : ItemRegister.TELEPORT_ITEMS.get(0).get()))
                                    .title(Component.translatable("itemGroup.minecraft_continuum_tab"))
                                    .displayItems(
                                            (pParameters, pOutput) -> {
                                                // Add all items in TELEPORT_ITEMS to the tab
                                                ItemRegister.TELEPORT_ITEMS.forEach(item -> pOutput.accept(item.get()));
                                            })
                                    .build());

    public static void register(IEventBus eventBus) {
        MINECRAFT_CONTINUUM_TABS.register(eventBus);
    }
}
