package com.lothrazar.fixmyminecart.carts;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MinecartItem extends Item {

  public MinecartItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    World world = context.getWorld();
    BlockPos blockpos = context.getPos();
    BlockState blockstate = world.getBlockState(blockpos);
    if (!blockstate.isIn(BlockTags.RAILS)) {
      return ActionResultType.FAIL;
    }
    ItemStack itemstack = context.getItem();
    if (!world.isRemote) {
      RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
      double d0 = 0.0D;
      if (railshape.isAscending()) {
        d0 = 0.5D;
      }
      ReinforcedMinecart cart = new ReinforcedMinecart(world, blockpos.getX() + 0.5D, blockpos.getY() + 0.0625D + d0, blockpos.getZ() + 0.5D);
      if (itemstack.hasDisplayName()) {
        cart.setCustomName(itemstack.getDisplayName());
      }
      world.addEntity(cart);
    }
    return ActionResultType.CONSUME;
  }
}
