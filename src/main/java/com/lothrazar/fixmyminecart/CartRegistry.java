package com.lothrazar.fixmyminecart;

import com.lothrazar.fixmyminecart.carts.MinecartItem;
import com.lothrazar.fixmyminecart.carts.PhantomMinecartRenderer;
import com.lothrazar.fixmyminecart.carts.ReinforcedMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//@EventBusSubscriber(modid = ModMain.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CartRegistry {

  static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModMain.MODID);
  static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ModMain.MODID);
  // now for the content
  public static final DeferredHolder<EntityType<?>, EntityType<ReinforcedMinecart>> E_REINFORCED_MINECART = ENTITIES.register(ReinforcedMinecart.ID, () ->
  //actual sub register
  register(ReinforcedMinecart.ID, EntityType.Builder.<ReinforcedMinecart> of(ReinforcedMinecart::new, MobCategory.MISC)
      .sized(0.98F, 0.7F).clientTrackingRange(8)));
  public static final DeferredHolder<Item, MinecartItem> I_REINFORCED_MINECART = ITEMS.registerItem(ReinforcedMinecart.ID, props -> new MinecartItem(props));

  private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ModMain.MODID, id)));
  }

//  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(E_REINFORCED_MINECART.get(), PhantomMinecartRenderer::new);
  }

//  @SubscribeEvent
  public static void buildContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
      event.accept(I_REINFORCED_MINECART.get());
    }
  }

  public static void setupDispenserBehavior(final FMLCommonSetupEvent event) {
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
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Level world = source.level();
        var p = source.pos();
        double d0 = p.getX() + direction.getStepX() * 1.125D;
        double d1 = Math.floor(p.getY()) + direction.getStepY();
        double d2 = p.getZ() + direction.getStepZ() * 1.125D;
        BlockPos blockpos = source.pos().relative(direction);
        BlockState blockstate = world.getBlockState(blockpos);
        RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock
            ? ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null)
            : RailShape.NORTH_SOUTH;
        double d3;
        if (blockstate.is(BlockTags.RAILS)) {
          if (railshape.isSlope()) {
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
          if (direction != Direction.DOWN && railshape1.isSlope()) {
            d3 = -0.4D;
          }
          else {
            d3 = -0.9D;
          }
        }
        ReinforcedMinecart cart = new ReinforcedMinecart(world, d0, d1 + d3, d2);
        if (stack.has(DataComponents.CUSTOM_NAME)) {
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
        source.level().levelEvent(1000, source.pos(), 0);
      }
    };
    DispenserBlock.registerBehavior(CartRegistry.I_REINFORCED_MINECART.get(), dib);
  }
}
