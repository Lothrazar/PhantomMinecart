package com.lothrazar.fixmyminecart.carts;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class MinecartItem extends Item {

  public MinecartItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level world = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    BlockState blockstate = world.getBlockState(blockpos);
    if (!blockstate.is(BlockTags.RAILS)) {
      return InteractionResult.FAIL;
    }
    context.getPlayer().swing(context.getHand());
    ItemStack itemstack = context.getItemInHand();
    if (!world.isClientSide) {
      RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock
          ? ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null)
          : RailShape.NORTH_SOUTH;
      double d0 = 0.0D;
      if (railshape.isAscending()) {
        d0 = 0.5D;
      }
      ReinforcedMinecart cart = new ReinforcedMinecart(world, blockpos.getX() + 0.5D, blockpos.getY() + 0.0625D + d0, blockpos.getZ() + 0.5D);
      if (itemstack.hasCustomHoverName()) {
        cart.setCustomName(itemstack.getHoverName());
      }
      world.addFreshEntity(cart);
      itemstack.shrink(1);
      return InteractionResult.CONSUME;
    }
    return InteractionResult.PASS;
  }
}
