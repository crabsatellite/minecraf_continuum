package com.crabmods.minecraft_continuum.items;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class OtherworldTeleportItem extends Item {

    private final String scriptPath;

    public OtherworldTeleportItem(Properties properties, String scriptPath) {
        super(properties);
        this.scriptPath = scriptPath; // Store the script path for this item
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Verify the script path before attempting any teleportation
        if (!isValidWorldDirectory(scriptPath)) {
            if (level.isClientSide) {
                player.sendSystemMessage(Component.literal("The specified world path is not valid or not bound to any world."));
            }
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        if (isServerSide(level)) {
            player.sendSystemMessage(Component.literal("Preparing teleportation..."));
        }

        CompletableFuture.runAsync(() -> {
            try {
                if (isServerSide(level)) {
                    player.sendSystemMessage(Component.literal("Teleporting to World 1..."));
                }
                if (level.isClientSide) {
                    teleportAndRunScript(player);
                }
            } catch (IOException e) {
                player.sendSystemMessage(Component.literal("Failed to execute Python script: " + e.getMessage()));
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
            player.sendSystemMessage(Component.literal("Loading new world..."));
        }

        if (level.isClientSide) {
            // Run the batch file to load the new world
            runBatchFile(scriptPath);
            // Close current game instance
            Minecraft.getInstance().close();
        }

        if (isServerSide(level)) {
            // Notify the client that the world has been loaded
            player.sendSystemMessage(Component.literal("New world loaded successfully!"));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        // Extract the world directory from the script path
        Path path = Paths.get(scriptPath);
        String worldDirectory = path.getParent().getParent().getFileName().toString();

        // Return dynamic item name
        return Component.literal("Teleport to " + worldDirectory);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Make the item appear enchanted
        return true;
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
        Logger logger = LogManager.getLogger();
        Path path = Paths.get(scriptPath).getParent().getParent();
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            logger.error(path + " does not exist or is not a directory.");
            return false;
        }

        // Check for the presence of a ".minecraft" folder in the grandparent directory
        if (Files.notExists(path.resolve(".minecraft"))) {
            logger.error("No .minecraft folder found in " + path);
            return false;
        }

        return true;
    }
}
