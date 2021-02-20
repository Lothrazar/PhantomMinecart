package com.lothrazar.fixmyminecart.carts;

import com.lothrazar.fixmyminecart.CartRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReinforcedMinecart extends AbstractMinecartEntity {

  public static final String ID = "reinforced_minecart";

  public ReinforcedMinecart(EntityType<?> type, World worldIn) {
    super(type, worldIn);
  }

  public ReinforcedMinecart(World worldIn, double x, double y, double z) {
    super(CartRegistry.E_REINFORCED_MINECART.get(), worldIn, x, y, z);
  }

  public ReinforcedMinecart(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
    this(CartRegistry.E_REINFORCED_MINECART.get(), worldIn);
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  @Override
  public ItemStack getCartItem() {
    return new ItemStack(CartRegistry.I_REINFORCED_MINECART.get());
  }

  @Override
  public boolean canCollide(Entity entityIn) {
    if (this.isPassenger(entityIn)) {
      return true;
    }
    return false;
    //    return BoatEntity.func_242378_a(this, entityIn);
  }

  @Override
  public boolean canBePushed() {
    //    ModMain.LOGGER.info("can super.canBePushed()? " + super.canBePushed());
    return super.canBePushed();
  }

  @Override
  public void applyEntityCollision(Entity entityIn) {
    if (this.isPassenger(entityIn)) {
      super.applyEntityCollision(entityIn);
      return;
    }
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    ActionResultType ret = super.processInitialInteract(player, hand);
    if (ret.isSuccessOrConsume() || player.isSecondaryUseActive() || this.isBeingRidden()) {
      return ret;
    }
    if (player.startRiding(this)) {
      return ActionResultType.CONSUME;
    }
    return ret;
  }

  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      if (this.isBeingRidden()) {
        this.removePassengers();
      }
      if (this.getRollingAmplitude() == 0) {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(50.0F);
        this.markVelocityChanged();
      }
    }
  }

  @Override
  public boolean hasDisplayTile() {
    return false;
  }

  @Override
  protected float getSpeedFactor() {
    if (!this.isBeingRidden()) {
      return super.getSpeedFactor();
    }
    return super.getSpeedFactor() * 1.4F;
  }

  @Override
  public boolean canBeRidden() {
    return true;
  }
}
