package com.crabmods.minecraft_continuum.items;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class WorldCrystal extends Item {

    private final String scriptPath;

    public WorldCrystal(Properties properties, String scriptPath) {
        super(properties);
        this.scriptPath = scriptPath; // Store the script path for this item
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Verify the script path before attempting any teleportation
        if (!isValidWorldDirectory(scriptPath)) {
            if (level.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.minecraft_continuum.invalid_world_path"));
            }
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        if (isServerSide(level)) {
            player.sendSystemMessage(Component.translatable("message.minecraft_continuum.preparing_teleportation"));
        }

        CompletableFuture.runAsync(() -> {
            try {
                if (isServerSide(level)) {
                    String worldDirectory = Paths.get(scriptPath).getParent().getParent().getFileName().toString();
                    player.sendSystemMessage(Component.translatable("message.minecraft_continuum.teleporting_to_world", worldDirectory));
                }
                if (level.isClientSide) {
                    teleportAndRunScript(player);
                }
            } catch (IOException e) {
                player.sendSystemMessage(Component.translatable("message.minecraft_continuum.failed_to_execute_script", e.getMessage()));
                e.printStackTrace();
            }
        });

        // Return interaction result
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public void teleportAndRunScript(Player player) throws IOException {
        Level level = player.level();

        // Handle logic only on the client side
        if (isServerSide(level)) {
            // Display a loading message in the HUD
            player.sendSystemMessage(Component.translatable("message.minecraft_continuum.loading_new_world"));
        }

        if (level.isClientSide) {
            // Run the batch file to load the new world
            runBatchFile(scriptPath);
            // Close current game instance
            Minecraft.getInstance().close();
        }

        if (isServerSide(level)) {
            // Notify the client that the world has been loaded
            player.sendSystemMessage(Component.translatable("message.minecraft_continuum.new_world_loaded"));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        // Extract the world directory from the script path
        Path path = Paths.get(scriptPath);
        String worldDirectory = path.getParent().getParent().getFileName().toString();

        // Return dynamic item name using translation
        return Component.translatable("item.minecraft_continuum.world_crystal", worldDirectory);
    }


    @Override
    public boolean isFoil(ItemStack stack) {
        // Show the item as shiny if the world directory is valid
        return isValidWorldDirectory(scriptPath);
    }

    private void runBatchFile(String batPath) throws IOException {
        // Execute the batch file
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", batPath);
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        builder.redirectError(ProcessBuilder.Redirect.DISCARD);
        builder.start();
    }

    private boolean isServerSide(Level level) {
        return !level.isClientSide && level.getServer() != null;
    }

    private boolean isValidWorldDirectory(String scriptPath) {
        // Check if the path exists and is a directory
        Path path = Paths.get(scriptPath).getParent().getParent();
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return false;
        }

        // Check for the presence of a ".minecraft" folder in the grandparent directory
        if (Files.notExists(path.resolve(".minecraft"))) {
            return false;
        }

        return true;
    }
}
