package mod.crabmods.minecraft_continuum;

import java.util.Objects;
import mod.crabmods.minecraft_continuum.config.ConfigHandler;
import mod.crabmods.minecraft_continuum.registers.ItemRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = MinecraftContinuum.MODID,
    name = MinecraftContinuum.NAME,
    version = MinecraftContinuum.VERSION)
public class MinecraftContinuum {
  public static final String MODID = "minecraft_continuum";
  public static final String NAME = "Minecraft Continuum";
  public static final String VERSION = "1.0.2";

  private static Logger logger;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    ConfigHandler.init(event);
    ItemRegister.registerItems();
    // Only register item models on the client side
    if (event.getSide().isClient()) {
      for (Item item : ItemRegister.WORLD_CRYSTALS) {
        registerItemModel(item);
      }
    }

  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    // some crabmods code
    logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
  }

  @SideOnly(Side.CLIENT)
  private void registerItemModel(Item item) {
    ModelLoader.setCustomModelResourceLocation(
        item,
        0,
        new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
  }
}
