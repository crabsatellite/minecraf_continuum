package com.crabmods.minecraft_continuum.items;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.io.IOException;
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
        // Directly teleport and run the script without confirmation
        if (!level.isClientSide) {
            // Display loading message
            player.sendSystemMessage(Component.literal("Preparing teleportation..."));

            // Run the script asynchronously
            CompletableFuture.runAsync(() -> {
                try {
                    // Notify that teleportation has started
                    player.sendSystemMessage(Component.literal("Teleporting to World 1..."));

                    // Run the script and teleport
                    teleportAndRunScript(player);
                } catch (IOException e) {
                    player.sendSystemMessage(Component.literal("Failed to execute Python script: " + e.getMessage()));
                    e.printStackTrace();
                }
            });
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public void teleportAndRunScript(Player player) throws IOException {
        Level level = player.level();

        // Check if the player is on a server and not on the client side.
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            // Display loading message in the HUD
            player.sendSystemMessage(Component.literal("Loading new world..."));

            // Run the Python script
            runExecutable();

            // Monitor the script's status to detect when the new world is loaded
            checkForNewMinecraftInstance(player);
        }
    }

    private void runExecutable() throws IOException {
        // Use the executable path for this specific item
        runBatchFile(scriptPath);
    }

    private void checkForNewMinecraftInstance(Player player) {
        // You might want to introduce a waiting mechanism here
        // e.g., polling or checking for certain conditions that indicate the new world is loaded

        new Thread(() -> {
            player.sendSystemMessage(Component.literal("New world loaded successfully!"));

            // Close current instance if needed
            closeGame();

        }).start();
    }

    @Override
    public Component getName(ItemStack stack) {
        // Get the path of the script
        Path path = Paths.get(scriptPath);
        // Get the directory
        String worldDirectory = path.getParent().getParent().getFileName().toString();
        // Return a translatable component with the dynamic content
        return Component.literal("Teleport to " + worldDirectory);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Return true to make the item have the enchanted glint effect
        return true;
    }

    private void closeGame() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().close();
        });
    }

    private void runBatchFile(String batPath) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", batPath);
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        builder.redirectError(ProcessBuilder.Redirect.DISCARD);
        builder.start();
    }
}
