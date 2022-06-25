package com.lothrazar.fixmyminecart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.fixmyminecart.carts.ReinforcedMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModMain.MODID)
public class ModMain {

  public static final String MODID = "fixmyminecart";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModMain() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    CartRegistry.ITEMS.register(eventBus);
    CartRegistry.ENTITIES.register(eventBus);
  }

  private void setup(final FMLCommonSetupEvent event) {
    /**
     * Thanks to MrBysco https://github.com/Mrbysco/ModJNam-Mod
     */
    DispenseItemBehavior dib = new DefaultDispenseItemBehavior() {

      private final DefaultDispenseItemBehavior defaultBh = new DefaultDispenseItemBehavior();

      /**
       * Dispense the specified stack, play the dispense sound and spawn particles.
       */
      @SuppressWarnings("deprecation")
      @Override
      public ItemStack execute(BlockSource source, ItemStack stack) {
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        Level world = source.getLevel();
        double d0 = source.x() + direction.getStepX() * 1.125D;
        double d1 = Math.floor(source.y()) + direction.getStepY();
        double d2 = source.z() + direction.getStepZ() * 1.125D;
        BlockPos blockpos = source.getPos().relative(direction);
        BlockState blockstate = world.getBlockState(blockpos);
        RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock
            ? ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null)
            : RailShape.NORTH_SOUTH;
        double d3;
        if (blockstate.is(BlockTags.RAILS)) {
          if (railshape.isAscending()) {
            d3 = 0.6D;
          }
          else {
            d3 = 0.1D;
          }
        }
        else {
          if (!blockstate.isAir() || !world.getBlockState(blockpos.below()).is(BlockTags.RAILS)) {
            return this.defaultBh.dispense(source, stack);
          }
          BlockState blockstate1 = world.getBlockState(blockpos.below());
          RailShape railshape1 = blockstate1.getBlock() instanceof BaseRailBlock
              ? blockstate1.getValue(((BaseRailBlock) blockstate1.getBlock()).getShapeProperty())
              : RailShape.NORTH_SOUTH;
          if (direction != Direction.DOWN && railshape1.isAscending()) {
            d3 = -0.4D;
          }
          else {
            d3 = -0.9D;
          }
        }
        ReinforcedMinecart cart = new ReinforcedMinecart(world, d0, d1 + d3, d2);
        if (stack.hasCustomHoverName()) {
          cart.setCustomName(stack.getHoverName());
        }
        world.addFreshEntity(cart);
        stack.shrink(1);
        return stack;
      }

      /**
       * Play the dispense sound from the specified block.
       */
      @Override
      protected void playSound(BlockSource source) {
        source.getLevel().levelEvent(1000, source.getPos(), 0);
      }
    };
    DispenserBlock.registerBehavior(CartRegistry.I_REINFORCED_MINECART.get(), dib);
  }
}
