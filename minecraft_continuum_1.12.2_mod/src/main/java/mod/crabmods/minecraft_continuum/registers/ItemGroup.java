package mod.crabmods.minecraft_continuum.registers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items; // Import Items class for item references
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGroup {

  // Create the custom creative tab
  public static final CreativeTabs MINECRAFT_CONTINUUM_TAB =
      new CreativeTabs("minecraft_continuum_tab") {

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
          // Set the tab icon to the first registered item or a fallback item
          return ItemRegister.WORLD_CRYSTALS.isEmpty()
              ? new ItemStack(Items.AIR)
              : new ItemStack(ItemRegister.WORLD_CRYSTALS.get(0));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(net.minecraft.util.NonNullList<ItemStack> items) {
          // Add all item in WORLD_CRYSTALS to the tab
          ItemRegister.WORLD_CRYSTALS.forEach(item -> items.add(new ItemStack(item)));
        }
      };
}
