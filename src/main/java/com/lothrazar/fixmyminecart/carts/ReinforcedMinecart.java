package com.lothrazar.fixmyminecart.carts;

import com.lothrazar.fixmyminecart.CartRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class ReinforcedMinecart extends AbstractMinecart {

  public static final String ID = "reinforced_minecart";

  public ReinforcedMinecart(EntityType<?> type, Level worldIn) {
    super(type, worldIn);
  }

  public ReinforcedMinecart(Level worldIn, double x, double y, double z) {
    super(CartRegistry.E_REINFORCED_MINECART.get(), worldIn, x, y, z);
  }

  public ReinforcedMinecart(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
    this(CartRegistry.E_REINFORCED_MINECART.get(), worldIn);
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  @Override
  public Item getDropItem() {
    return CartRegistry.I_REINFORCED_MINECART.get();
  }

  @Override
  public ItemStack getPickResult() {
    return new ItemStack(CartRegistry.I_REINFORCED_MINECART.get());
  }

  @Override
  public boolean canCollideWith(Entity entityIn) {
    if (this.hasPassenger(entityIn)) {
      return true;
    }
    return false;
    //    return BoatEntity.canVehicleCollide(this, entityIn);
  }

  @Override
  public boolean isPushable() {
    //    ModMain.LOGGER.info("can super.canBePushed()? " + super.canBePushed());
    return super.isPushable();
  }

  @Override
  public void push(Entity entityIn) {
    if (this.hasPassenger(entityIn)) {
      super.push(entityIn);
      return;
    }
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    InteractionResult ret = super.interact(player, hand);
    if (ret.consumesAction() || player.isSecondaryUseActive() || this.isVehicle()) {
      return ret;
    }
    if (player.startRiding(this)) {
      return InteractionResult.CONSUME;
    }
    return ret;
  }

  @Override
  public void activateMinecart(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      if (this.isVehicle()) {
        this.ejectPassengers();
      }
      if (this.getHurtTime() == 0) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(50.0F);
        this.markHurt();
      }
    }
  }

  @Override
  public boolean hasCustomDisplay() {
    return false;
  }

  @Override
  protected float getBlockSpeedFactor() {
    if (!this.isVehicle()) {
      return super.getBlockSpeedFactor();
    }
    return super.getBlockSpeedFactor() * 1.4F;
  }

  @Override
  public boolean canBeRidden() {
    return true;
  }
}
