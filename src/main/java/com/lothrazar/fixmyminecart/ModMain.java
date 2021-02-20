package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartRenderer;
import com.lothrazar.fixmyminecart.carts.ReinforcedMinecart;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModMain.MODID)
public class ModMain {

  public static final String MODID = "fixmyminecart";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModMain() {
    ConfigManager.setup();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    //
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    CartRegistry.ITEMS.register(eventBus);
    CartRegistry.ENTITIES.register(eventBus);
  }

  private void setupClient(final FMLClientSetupEvent event) {
    //for client side only setup
    RenderingRegistry.registerEntityRenderingHandler(CartRegistry.E_REINFORCED_MINECART.get(), MinecartRenderer::new);
  }

  private void setup(final FMLCommonSetupEvent event) {
    /**
     * Thanks to MrBysco https://github.com/Mrbysco/ModJNam-Mod
     */
    IDispenseItemBehavior dib = new DefaultDispenseItemBehavior() {

      private final DefaultDispenseItemBehavior defaultBh = new DefaultDispenseItemBehavior();

      /**
       * Dispense the specified stack, play the dispense sound and spawn particles.
       */
      @SuppressWarnings("deprecation")
      @Override
      public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        Direction direction = source.getBlockState().get(DispenserBlock.FACING);
        World world = source.getWorld();
        double d0 = source.getX() + direction.getXOffset() * 1.125D;
        double d1 = Math.floor(source.getY()) + direction.getYOffset();
        double d2 = source.getZ() + direction.getZOffset() * 1.125D;
        BlockPos blockpos = source.getBlockPos().offset(direction);
        BlockState blockstate = world.getBlockState(blockpos);
        RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock
            ? ((AbstractRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null)
            : RailShape.NORTH_SOUTH;
        double d3;
        if (blockstate.isIn(BlockTags.RAILS)) {
          if (railshape.isAscending()) {
            d3 = 0.6D;
          }
          else {
            d3 = 0.1D;
          }
        }
        else {
          if (!blockstate.isAir() || !world.getBlockState(blockpos.down()).isIn(BlockTags.RAILS)) {
            return this.defaultBh.dispense(source, stack);
          }
          BlockState blockstate1 = world.getBlockState(blockpos.down());
          RailShape railshape1 = blockstate1.getBlock() instanceof AbstractRailBlock
              ? blockstate1.get(((AbstractRailBlock) blockstate1.getBlock()).getShapeProperty())
              : RailShape.NORTH_SOUTH;
          if (direction != Direction.DOWN && railshape1.isAscending()) {
            d3 = -0.4D;
          }
          else {
            d3 = -0.9D;
          }
        }
        ReinforcedMinecart cart = new ReinforcedMinecart(world, d0, d1 + d3, d2);
        if (stack.hasDisplayName()) {
          cart.setCustomName(stack.getDisplayName());
        }
        world.addEntity(cart);
        stack.shrink(1);
        return stack;
      }

      /**
       * Play the dispense sound from the specified block.
       */
      @Override
      protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(1000, source.getBlockPos(), 0);
      }
    };
    DispenserBlock.registerDispenseBehavior(CartRegistry.I_REINFORCED_MINECART.get(), dib);
  }
}
